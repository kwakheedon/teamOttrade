package com.ottrade.ottrade.domain.hssearch.service;

import com.ottrade.ottrade.domain.hssearch.dto.HscodeAggregatedDTO;
import com.ottrade.ottrade.domain.hssearch.dto.HscodeInfoDTO;
import com.ottrade.ottrade.util.XmlUtils; // XmlUtils 임포트
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class HsSearchService {

    private final RestTemplate restTemplate;

    @Value("${unipass.api.key}")
    private String apiKey;

    public List<HscodeAggregatedDTO> getAggregatedHsCodeInfo(String prnm) {
        String url = "https://unipass.customs.go.kr:38010/ext/rest/hsSgnQry/searchHsSgn"
                + "?crkyCn=" + apiKey
                + "&prnm=" + prnm
                + "&koenTp=1";

        try {
            String xml = restTemplate.getForObject(url, String.class);
            return parseAndAggregateXml(xml);
        } catch (Exception e) {
            throw new RuntimeException("UNIPASS API 호출 또는 파싱 실패", e);
        }
    }

    private List<HscodeAggregatedDTO> parseAndAggregateXml(String xml) {
        // XmlUtils.parseXml 메소드 사용
        List<HscodeInfoDTO> allItems = XmlUtils.parseXml(xml, "hsSgnSrchRsltVo", el -> {
            String hsSgn = XmlUtils.getTagValue(el, "hsSgn");
            String txrtStr = XmlUtils.getTagValue(el, "txrt");
            String korePrnm = XmlUtils.getTagValue(el, "korePrnm");

            int txrt = 0;
            try {
                txrt = (int) Double.parseDouble(txrtStr);
            } catch (NumberFormatException ignored) {}

            return new HscodeInfoDTO(hsSgn, String.valueOf(txrt), korePrnm);
        });

        // HscodeSgn 기준으로 그룹화
        Map<String, List<HscodeInfoDTO>> grouped = allItems.stream()
                .collect(Collectors.groupingBy(HscodeInfoDTO::getHsSgn));

        int totalCount = allItems.size();

        List<HscodeAggregatedDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<HscodeInfoDTO>> entry : grouped.entrySet()) {
            String hsSgn = entry.getKey();
            List<HscodeInfoDTO> dtos = entry.getValue();

            int avgTxrt = (int) dtos.stream()
                    .mapToDouble(d -> {
                        try {
                            return Double.parseDouble(d.getTxrt());
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    })
                    .average()
                    .orElse(0.0);

            int rate = (int) ((dtos.size() * 100.0) / totalCount);
            String korePrnm = dtos.get(0).getKorePrnm();

            result.add(new HscodeAggregatedDTO(hsSgn, korePrnm, avgTxrt, rate));
        }

        return result;
    }
}