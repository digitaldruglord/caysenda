package com.nomi.caysenda.lazada.services.xml;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Request")
@Getter
@Setter
public class LazadaUpdateProductRequestXml {
    @Element(name = "Product")
    LazadaUpdateProductXml productXml;
}
