package com.ottrade.ottrade.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class XmlUtils {

    /**
     * XML 문자열을 파싱하여 DTO 리스트로 변환하는 제네릭 메소드
     *
     * @param xml         파싱할 XML 문자열
     * @param itemTagName 각 아이템을 감싸는 태그 이름 (e.g., "item", "hsSgnSrchRsltVo")
     * @param mapper      Element를 원하는 DTO 타입으로 변환하는 함수
     * @return DTO 리스트
     * @throws RuntimeException XML 파싱 실패 시
     */
    public static <T> List<T> parseXml(String xml, String itemTagName, Function<Element, T> mapper) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            NodeList nodeList = doc.getElementsByTagName(itemTagName);

            List<T> result = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                result.add(mapper.apply((Element) nodeList.item(i)));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("XML 파싱에 실패했습니다.", e);
        }
    }

    /**
     * Element에서 특정 태그의 값을 문자열로 추출
     *
     * @param el  부모 Element
     * @param tag 추출할 태그 이름
     * @return 태그의 텍스트 값 (없으면 빈 문자열)
     */
    public static String getTagValue(Element el, String tag) {
        Node node = el.getElementsByTagName(tag).item(0);
        return node != null ? node.getTextContent().trim() : "";
    }

    /**
     * Element에서 특정 태그의 값을 long으로 파싱
     *
     * @param el  부모 Element
     * @param tag 추출할 태그 이름
     * @return 파싱된 long 값 (파싱 실패 시 0L)
     */
    public static long parseLong(Element el, String tag) {
        try {
            return Long.parseLong(getTagValue(el, tag).replaceAll(",", "").trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}