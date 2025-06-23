package com.ottrade.ottrade.domain.hssearch.service;

import com.ottrade.ottrade.domain.hssearch.dto.HscodeAggregatedDTO;
import com.ottrade.ottrade.domain.hssearch.dto.HscodeInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<HscodeAggregatedDTO> parseAndAggregateXml(String xml) throws Exception {
        Map<String, List<HscodeInfoDTO>> grouped = new HashMap<>();
        int totalCount = 0;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));
        NodeList list = doc.getElementsByTagName("hsSgnSrchRsltVo");

        for (int i = 0; i < list.getLength(); i++) {
            Element el = (Element) list.item(i);

            String hsSgn = getTagValue("hsSgn", el);
            String txrtStr = getTagValue("txrt", el);
            String korePrnm = getTagValue("korePrnm", el);

            int txrt = 0;
            try {
                txrt = (int) Double.parseDouble(txrtStr);
            } catch (NumberFormatException ignored) {}

            HscodeInfoDTO dto = new HscodeInfoDTO(hsSgn, String.valueOf(txrt), korePrnm);
            grouped.computeIfAbsent(hsSgn, k -> new ArrayList<>()).add(dto);
            totalCount++;
        }

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

    private String getTagValue(String tag, Element element) {
        Node node = element.getElementsByTagName(tag).item(0);
        return node != null ? node.getTextContent().trim() : "";
    }
}
