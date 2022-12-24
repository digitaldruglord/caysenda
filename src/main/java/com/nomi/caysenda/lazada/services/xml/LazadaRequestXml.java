package com.nomi.caysenda.lazada.services.xml;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Request")
@Getter
@Setter
public class LazadaRequestXml {
    @ElementList(name = "Product",inline = true)
    List<LazadaProductXml> products;
}
