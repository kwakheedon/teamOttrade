package com.ottrade.ottrade.domain.hssearch.service;

import com.ottrade.ottrade.domain.hssearch.dto.*;
import com.ottrade.ottrade.domain.log.repository.SearchLogRepository;
import com.ottrade.ottrade.util.XmlUtils; // XmlUtils 임포트
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.annotation.PreDestroy;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

@Service
public class TradeApiService {

    private static final Logger logger = LoggerFactory.getLogger(TradeApiService.class);
    private final RestTemplate restTemplate;
    private final SearchLogRepository searchLogRepository;

    @Value("${data.trade.api.key}")
    private String serviceKey;

    // 병렬 호출용 쓰레드풀 (50개 쓰레드)
    private final ExecutorService executor = Executors.newFixedThreadPool(50);

    public TradeApiService(SearchLogRepository searchLogRepository) {
        this.searchLogRepository = searchLogRepository;
        this.restTemplate = new RestTemplate();
        // UTF-8 처리
        List<HttpMessageConverter<?>> conv = restTemplate.getMessageConverters();
        for (int i = 0; i < conv.size(); i++) {
            if (conv.get(i) instanceof StringHttpMessageConverter) {
                conv.set(i, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            }
        }
    }

    @PreDestroy
    public void shutdownExecutor() {
        logger.info("Shutting down executor service");
        executor.shutdown();
    }

    /**
     * HS 코드 하나에 대해
     * 1) FTA 체결국 전체를 병렬 호출
     * 2) 국가별 합산
     * 3) Top3 항목별 연도별 합계(items)도 병렬 조회해 반환
     */
    public TradeTop3ResultDTO fetchTop3TradeStats(String hsSgn) {
        long startAll = System.currentTimeMillis();
        logger.info("[fetchTop3TradeStats] start hsSgn={}", hsSgn);

        // 1) FTA 체결국 리스트
        List<String> ftaCountries = List.of(
                "US","CN","VN","TH","SG","MY","ID","IN","AU","NZ","CL","PE","CO","CA","TR","MX",
                "PH","LA","KH","MM","BR","AE","GB",
                "AT","BE","BG","HR","CY","CZ","DK","EE","FI","FR","DE","GR","HU","IE","IT",
                "LV","LT","LU","MT","NL","PL","PT","RO","SK","SI","ES","SE"
        );

        // 2) 병렬로 지난 1년치 fetchLastYearTrade
        List<CompletableFuture<List<ItemDTO>>> yearFutures = ftaCountries.stream()
                .map(cnty -> CompletableFuture.supplyAsync(
                                () -> fetchLastYearTrade(hsSgn, cnty), executor
                        )
                )
                .collect(Collectors.toList());

        // 3) join 및 단일 리스트로 합치기
        List<ItemDTO> allItems = yearFutures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        logger.info("[fetchTop3TradeStats] fetched allItems size={} in {} ms",
                allItems.size(), System.currentTimeMillis() - startAll);

        // 4) 국가별 합산
        long startGroup = System.currentTimeMillis();
        List<ItemDTO> grouped = groupByCountryAndSum(allItems);
        logger.info("[fetchTop3TradeStats] groupByCountryAndSum took {} ms, countries={}",
                System.currentTimeMillis() - startGroup, grouped.size());

        // 5) Top3 각 리스트 생성
        TradeTop3ResultDTO result = new TradeTop3ResultDTO(
                buildTopN(grouped, hsSgn, ItemDTO::getExpDlr, 3),
                buildTopN(grouped, hsSgn, ItemDTO::getExpWgt, 3),
                buildTopN(grouped, hsSgn, ItemDTO::getImpDlr, 3),
                buildTopN(grouped, hsSgn, ItemDTO::getImpWgt, 3)
        );

        logger.info("[fetchTop3TradeStats] total duration={} ms",
                System.currentTimeMillis() - startAll);
        return result;
    }

    /**
     * Top N 국가 추출 + 연도별 합계(items) 병렬 호출
     */
    private List<TradeTopCountryDTO> buildTopN(
            List<ItemDTO> items,
            String hsSgn,
            ToLongFunction<ItemDTO> extractor,
            int n
    ) {
        long startBuild = System.currentTimeMillis();
        // Top N ItemDTO 추출
        List<ItemDTO> topItems = items.stream()
                .filter(i -> extractor.applyAsLong(i) > 0)
                .sorted(Comparator.comparingLong(extractor).reversed())
                .limit(n)
                .collect(Collectors.toList());

        // 병렬로 연도별 합계 조회
        List<CompletableFuture<TradeTopCountryDTO>> futures = new ArrayList<>();
        for (int i = 0; i < topItems.size(); i++) {
            final int rank = i + 1;
            ItemDTO dto = topItems.get(i);
            futures.add(CompletableFuture.supplyAsync(() -> {
                long startYearly = System.currentTimeMillis();
                List<YearlyTradeDataDTO> yearly = fetchGroupedTradeList(hsSgn, dto.getCntyCd());
                logger.info("[buildTopN] fetchGroupedTradeList country={} took {} ms",
                        dto.getCntyCd(), System.currentTimeMillis() - startYearly);
                return new TradeTopCountryDTO(dto, rank, yearly);
            }, executor));
        }

        List<TradeTopCountryDTO> result = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        logger.info("[buildTopN] built Top{} in {} ms", n,
                System.currentTimeMillis() - startBuild);
        return result;
    }

    /** 작년 1월~12월 데이터 동기 호출 */
    private List<ItemDTO> fetchLastYearTrade(String hsSgn, String cntyCd) {
        long start = System.currentTimeMillis();
        int year = LocalDate.now().getYear() - 1;
        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://apis.data.go.kr/1220000/nitemtrade/getNitemtradeList")
                .queryParam("serviceKey", serviceKey)
                .queryParam("strtYymm", year + "01")
                .queryParam("endYymm", year + "12")
                .queryParam("hsSgn", hsSgn)
                .queryParam("cntyCd", cntyCd)
                .build(true).encode().toUri();

        String xmlBody = restTemplate.getForEntity(uri, String.class).getBody();
        List<ItemDTO> result = parseTradeXml(xmlBody); // 공통 메소드 사용

        logger.info("[fetchLastYearTrade] country={} took {} ms, items={}"
                , cntyCd, System.currentTimeMillis() - start, result.size());
        return result;
    }

    /** 국가(statCd)별로 금액·중량 합산 */
    private List<ItemDTO> groupByCountryAndSum(List<ItemDTO> list) {
        return list.stream()
                .filter(this::isValidCountry)
                .collect(Collectors.groupingBy(ItemDTO::getStatCd))
                .entrySet().stream()
                .map(e -> {
                    List<ItemDTO> v = e.getValue();
                    ItemDTO s = v.get(0);
                    long sumExpD = v.stream().mapToLong(ItemDTO::getExpDlr).sum();
                    long sumExpW = v.stream().mapToLong(ItemDTO::getExpWgt).sum();
                    long sumImpD = v.stream().mapToLong(ItemDTO::getImpDlr).sum();
                    long sumImpW = v.stream().mapToLong(ItemDTO::getImpWgt).sum();
                    return new ItemDTO(
                            "-", sumExpD, sumExpW,
                            s.getHsCd(), sumImpD, sumImpW,
                            s.getStatCd(), s.getStatCdCntnKor1(), s.getStatKor(),
                            s.getYear(), s.getCntyCd()
                    );
                })
                .collect(Collectors.toList());
    }

    private boolean isValidCountry(ItemDTO it) {
        return it.getStatCd() != null
                && !it.getStatCd().isBlank()
                && !it.getStatCd().equals("-")
                && it.getStatCdCntnKor1() != null
                && !it.getStatCdCntnKor1().isBlank()
                && !it.getStatCdCntnKor1().equals("-");
    }

    /** XML → ItemDTO 파싱 (공통 유틸리티 사용) */
    private List<ItemDTO> parseTradeXml(String xml) {
        return XmlUtils.parseXml(xml, "item", el -> new ItemDTO(
                XmlUtils.getTagValue(el, "balPayments"),
                XmlUtils.parseLong(el, "expDlr"),
                XmlUtils.parseLong(el, "expWgt"),
                XmlUtils.getTagValue(el, "hsCd"),
                XmlUtils.parseLong(el, "impDlr"),
                XmlUtils.parseLong(el, "impWgt"),
                XmlUtils.getTagValue(el, "statCd"),
                XmlUtils.getTagValue(el, "statCdCntnKor1"),
                XmlUtils.getTagValue(el, "statKor"),
                XmlUtils.getTagValue(el, "year"),
                XmlUtils.getTagValue(el, "cntyCd")
        ));
    }


    /**
     * 연도별 합계 계산 (최근 6년)
     */
    public List<YearlyTradeDataDTO> fetchGroupedTradeList(String hsSgn, String cntyCd) {
        long startAll = System.currentTimeMillis();
        logger.info("[fetchGroupedTradeList] start country={}", cntyCd);

        Map<String, List<ItemDTO>> map = new LinkedHashMap<>();
        int now = LocalDate.now().getYear();

        for (int i = 1; i <= 6; i++) {
            int y = now - i;
            long startYear = System.currentTimeMillis();
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://apis.data.go.kr/1220000/nitemtrade/getNitemtradeList")
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("strtYymm", y + "01")
                    .queryParam("endYymm",  y + "12")
                    .queryParam("hsSgn",    hsSgn)
                    .queryParam("cntyCd",   cntyCd)
                    .build(true).encode().toUri();

            String xmlBody = restTemplate.getForEntity(uri, String.class).getBody();
            List<ItemDTO> list = parseTradeXml(xmlBody); // 공통 메소드 사용

            logger.info("[fetchGroupedTradeList] country={} year={} took {} ms, items={}",
                    cntyCd, y, System.currentTimeMillis() - startYear, list.size());

            for (ItemDTO it : list) {
                String ky = it.getYear();
                if (ky == null || ky.isBlank() || ky.equals("총계")) continue;
                if (ky.contains(".")) ky = ky.split("\\.")[0];
                map.computeIfAbsent(ky, __ -> new ArrayList<>()).add(it);
            }
        }

        List<YearlyTradeDataDTO> result = map.entrySet().stream()
                .map(e -> {
                    List<ItemDTO> v = e.getValue();
                    long te = v.stream().mapToLong(ItemDTO::getExpDlr).sum();
                    long tw = v.stream().mapToLong(ItemDTO::getExpWgt).sum();
                    long ie = v.stream().mapToLong(ItemDTO::getImpDlr).sum();
                    long iw = v.stream().mapToLong(ItemDTO::getImpWgt).sum();
                    return new YearlyTradeDataDTO(e.getKey(), te, tw, ie, iw);
                })
                .collect(Collectors.toList());

        logger.info("[fetchGroupedTradeList] total country={} took {} ms",
                cntyCd, System.currentTimeMillis() - startAll);
        return result;
    }
}