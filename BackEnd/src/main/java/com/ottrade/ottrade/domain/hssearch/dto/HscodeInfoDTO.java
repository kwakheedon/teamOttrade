package com.ottrade.ottrade.domain.hssearch.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "item")
public class HscodeInfoDTO {

    @JacksonXmlProperty(localName = "hsSgn")
    private String hsSgn;

    @JacksonXmlProperty(localName = "txrt")
    private String txrt;

    @JacksonXmlProperty(localName = "korePrnm")
    private String korePrnm;
}
