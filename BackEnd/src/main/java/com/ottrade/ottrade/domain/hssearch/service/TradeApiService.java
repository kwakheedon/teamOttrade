package com.ottrade.ottrade.domain.hssearch.service;

import com.ottrade.ottrade.domain.hssearch.dto.*;
import com.ottrade.ottrade.domain.log.repository.SearchLogRepository;
import com.ottrade.ottrade.domain.log.service.LogService;
import com.ottrade.ottrade.util.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.annotation.PreDestroy;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TradeApiService {

    private static final Logger logger = LoggerFactory.getLogger(TradeApiService.class);
    private final RestTemplate restTemplate;
    private final SearchLogRepository searchLogRepository;
    private final LogService logService;

    @Value("${data.trade.api.key}")
    private String serviceKey;

    private final ExecutorService executor = Executors.newFixedThreadPool(50);

    // 순환 참조 해결을 위해 @Lazy 어노테이션 사용
    public TradeApiService(SearchLogRepository searchLogRepository, @Lazy LogService logService) {
        this.searchLogRepository = searchLogRepository;
        this.logService = logService;
        this.restTemplate = new RestTemplate();
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

    public TradeTop3ResultDTO fetchTradeStatsByLog(String hsSgn, @Nullable String userCntyCd, LocalDateTime searchedAt) {
        return fetchTop3TradeStats(hsSgn, userCntyCd, searchedAt.toLocalDate());
    }

    // 컨트롤러에서 호출하는 오버로딩 메소드
    public TradeTop3ResultDTO fetchTop3TradeStats(String hsSgn, @Nullable String cntyCd, @Nullable String korePrnm, @Nullable Long userId) {
        // 로그인 사용자일 경우 조회 이력 저장
        if (userId != null && korePrnm != null) {
            logService.saveHsCodeSearchLog(hsSgn, userId, korePrnm);
        }
        return fetchTop3TradeStats(hsSgn, cntyCd, LocalDate.now());
    }

    // GPT 서비스에서 호출하는 기존 메소드
    public TradeTop3ResultDTO fetchTop3TradeStats(String hsSgn, @Nullable String userCntyCd) {
        return fetchTop3TradeStats(hsSgn, userCntyCd, LocalDate.now());
    }

    private TradeTop3ResultDTO fetchTop3TradeStats(String hsSgn, @Nullable String userCntyCd, LocalDate referenceDate) {
        logger.info("[fetchTop3TradeStats] hsSgn={}, userCntyCd={}, referenceDate={}", hsSgn, userCntyCd, referenceDate);
        Map<String, List<YearlyTradeDataDTO>> yearlyCache = new ConcurrentHashMap<>();

        List<String> ftaCountries = List.of(
                "AU", "BN", "KH", "CN", "IN", "ID", "JP", "LA", "MY", "MM", "NZ", "PH", "SG", "TH", "VN",
                "CA", "CL", "CO", "CR", "SV", "HN", "NI", "PA", "PE", "US",
                "AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "GR", "HU", "IS", "IE", "IT", "LV", "LI", "LT", "LU", "MT", "NL", "NO", "PL", "PT", "RO", "SK", "SI", "ES", "SE", "CH", "GB",
                "IL", "TR"
        );

        List<CompletableFuture<List<ItemDTO>>> yearFutures = ftaCountries.stream()
                .map(cnty -> CompletableFuture.supplyAsync(() -> fetchLastYearTrade(hsSgn, cnty, referenceDate), executor))
                .collect(Collectors.toList());

        List<ItemDTO> allItems = yearFutures.stream().map(CompletableFuture::join).flatMap(List::stream).collect(Collectors.toList());
        List<ItemDTO> grouped = groupByCountryAndSum(allItems);

        List<TradeTopCountryDTO> topExpDlr = buildTopN(grouped, hsSgn, ItemDTO::getExpDlr, 3, yearlyCache, referenceDate);
        List<TradeTopCountryDTO> topExpWgt = buildTopN(grouped, hsSgn, ItemDTO::getExpWgt, 3, yearlyCache, referenceDate);
        List<TradeTopCountryDTO> topImpDlr = buildTopN(grouped, hsSgn, ItemDTO::getImpDlr, 3, yearlyCache, referenceDate);
        List<TradeTopCountryDTO> topImpWgt = buildTopN(grouped, hsSgn, ItemDTO::getImpWgt, 3, yearlyCache, referenceDate);

        if (userCntyCd != null && !userCntyCd.isBlank()) {
            List<ItemDTO> userCountryLastYearItems = fetchLastYearTrade(hsSgn, userCntyCd, referenceDate);
            ItemDTO userCountryAggregated;
            if (!userCountryLastYearItems.isEmpty()) {
                userCountryAggregated = groupByCountryAndSum(userCountryLastYearItems).get(0);
            } else {
                userCountryAggregated = new ItemDTO("-", 0, 0, hsSgn, 0, 0, userCntyCd, userCntyCd, userCntyCd, String.valueOf(referenceDate.getYear() - 1), userCntyCd);
            }
            List<YearlyTradeDataDTO> yearlyData = yearlyCache.computeIfAbsent(userCntyCd,
                    countryCode -> fetchGroupedTradeList(hsSgn, countryCode, referenceDate));
            TradeTopCountryDTO userCountryDto = new TradeTopCountryDTO(userCountryAggregated, 0, yearlyData);
            topExpDlr = addToListIfNotPresent(topExpDlr, userCountryDto);
            topExpWgt = addToListIfNotPresent(topExpWgt, userCountryDto);
            topImpDlr = addToListIfNotPresent(topImpDlr, userCountryDto);
            topImpWgt = addToListIfNotPresent(topImpWgt, userCountryDto);
        }

        return new TradeTop3ResultDTO(topExpDlr, topExpWgt, topImpDlr, topImpWgt);
    }

    private List<TradeTopCountryDTO> buildTopN(
            List<ItemDTO> items, String hsSgn, ToLongFunction<ItemDTO> extractor, int n,
            Map<String, List<YearlyTradeDataDTO>> yearlyCache, LocalDate referenceDate) {
        List<ItemDTO> topItems = items.stream()
                .filter(i -> extractor.applyAsLong(i) > 0)
                .sorted(Comparator.comparingLong(extractor).reversed())
                .limit(n)
                .collect(Collectors.toList());

        List<CompletableFuture<TradeTopCountryDTO>> futures = new ArrayList<>();
        for (int i = 0; i < topItems.size(); i++) {
            final int rank = i + 1;
            ItemDTO dto = topItems.get(i);
            futures.add(CompletableFuture.supplyAsync(() -> {
                List<YearlyTradeDataDTO> yearly = yearlyCache.computeIfAbsent(dto.getStatCd(),
                        countryCode -> fetchGroupedTradeList(hsSgn, countryCode, referenceDate));
                return new TradeTopCountryDTO(dto, rank, yearly);
            }, executor));
        }
        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    private List<ItemDTO> fetchLastYearTrade(String hsSgn, String cntyCd, LocalDate referenceDate) {
        int year = referenceDate.getYear() - 1;
        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://apis.data.go.kr/1220000/nitemtrade/getNitemtradeList")
                .queryParam("serviceKey", serviceKey)
                .queryParam("strtYymm", year + "01")
                .queryParam("endYymm", year + "12")
                .queryParam("hsSgn", hsSgn)
                .queryParam("cntyCd", cntyCd)
                .build(true).encode().toUri();
        String xmlBody = restTemplate.getForEntity(uri, String.class).getBody();
        return parseTradeXml(xmlBody);
    }

    public List<YearlyTradeDataDTO> fetchGroupedTradeList(String hsSgn, String cntyCd, LocalDate referenceDate) {
        Map<String, List<ItemDTO>> map = new LinkedHashMap<>();
        int nowYear = referenceDate.getYear();
        for (int i = 1; i <= 6; i++) {
            int y = nowYear - i;
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://apis.data.go.kr/1220000/nitemtrade/getNitemtradeList")
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("strtYymm", y + "01")
                    .queryParam("endYymm", y + "12")
                    .queryParam("hsSgn", hsSgn)
                    .queryParam("cntyCd", cntyCd)
                    .build(true).encode().toUri();
            String xmlBody = restTemplate.getForEntity(uri, String.class).getBody();
            List<ItemDTO> list = parseTradeXml(xmlBody);
            for (ItemDTO it : list) {
                String ky = it.getYear();
                if (ky == null || ky.isBlank() || ky.equals("총계")) continue;
                if (ky.contains(".")) ky = ky.split("\\.")[0];
                map.computeIfAbsent(ky, __ -> new ArrayList<>()).add(it);
            }
        }
        return map.entrySet().stream()
                .map(e -> new YearlyTradeDataDTO(e.getKey(),
                        e.getValue().stream().mapToLong(ItemDTO::getExpDlr).sum(),
                        e.getValue().stream().mapToLong(ItemDTO::getExpWgt).sum(),
                        e.getValue().stream().mapToLong(ItemDTO::getImpDlr).sum(),
                        e.getValue().stream().mapToLong(ItemDTO::getImpWgt).sum()))
                .collect(Collectors.toList());
    }

    private List<ItemDTO> groupByCountryAndSum(List<ItemDTO> list) {
        return list.stream()
                .filter(this::isValidCountry)
                .collect(Collectors.groupingBy(ItemDTO::getStatCd))
                .entrySet().stream()
                .map(e -> {
                    String countryCode = e.getKey();
                    List<ItemDTO> countryItems = e.getValue();

                    long sumExpDlr = countryItems.stream().mapToLong(ItemDTO::getExpDlr).sum();
                    long sumExpWgt = countryItems.stream().mapToLong(ItemDTO::getExpWgt).sum();
                    long sumImpDlr = countryItems.stream().mapToLong(ItemDTO::getImpDlr).sum();
                    long sumImpWgt = countryItems.stream().mapToLong(ItemDTO::getImpWgt).sum();

                    ItemDTO sampleItem = countryItems.get(0);

                    return new ItemDTO(
                            "-",
                            sumExpDlr, sumExpWgt,
                            sampleItem.getHsCd(),
                            sumImpDlr, sumImpWgt,
                            countryCode,
                            sampleItem.getStatCdCntnKor1(),
                            sampleItem.getStatKor(),
                            sampleItem.getYear(),
                            countryCode
                    );
                })
                .collect(Collectors.toList());
    }

    private List<TradeTopCountryDTO> addToListIfNotPresent(List<TradeTopCountryDTO> list, TradeTopCountryDTO dtoToAdd) {
        boolean isPresent = list.stream().anyMatch(dto -> dto.getStatCd().equalsIgnoreCase(dtoToAdd.getStatCd()));
        if (!isPresent) {
            List<TradeTopCountryDTO> newList = new ArrayList<>(list);
            newList.add(dtoToAdd);
            return newList;
        }
        return list;
    }

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

    private boolean isValidCountry(ItemDTO it) {
        return it.getStatCd() != null && !it.getStatCd().isBlank() && !it.getStatCd().equals("-")
                && it.getStatCdCntnKor1() != null && !it.getStatCdCntnKor1().isBlank() && !it.getStatCdCntnKor1().equals("-");
    }

    public List<YearlyTradeDataDTO> fetchGroupedTradeList(String hsSgn, String cntyCd) {
        return fetchGroupedTradeList(hsSgn, cntyCd, LocalDate.now());
    }

    public GroupedTradeDataDTO fetchGroupedTradeData(String hsSgn, String cntyCd) {
        List<YearlyTradeDataDTO> yearlyItems = fetchGroupedTradeList(hsSgn, cntyCd);
        List<ItemDTO> lastYearItems = fetchLastYearTrade(hsSgn, cntyCd, LocalDate.now());

        String countryName = "정보 없음";
        String hsCode = hsSgn;
        long totalLastYearExpDlr = 0L;
        long totalLastYearExpWgt = 0L;
        long totalLastYearImpDlr = 0L;
        long totalLastYearImpWgt = 0L;

        if (!lastYearItems.isEmpty()) {
            ItemDTO sampleItem = lastYearItems.get(0);
            countryName = sampleItem.getStatCdCntnKor1();
            hsCode = sampleItem.getHsCd();
            for (ItemDTO item : lastYearItems) {
                totalLastYearExpDlr += item.getExpDlr();
                totalLastYearExpWgt += item.getExpWgt();
                totalLastYearImpDlr += item.getImpDlr();
                totalLastYearImpWgt += item.getImpWgt();
            }
        }
        return new GroupedTradeDataDTO(cntyCd, countryName, hsCode, totalLastYearExpDlr, totalLastYearExpWgt, totalLastYearImpDlr, totalLastYearImpWgt, yearlyItems);
    }
}