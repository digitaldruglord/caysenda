package com.nomi.caysenda.lazada.services.xml;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Product")
@Getter
@Setter
public class LazadaUpdateProductXml {
    @Element(name = "ItemId")
    String itemId;
    @ElementList(name = "Skus",inline = true)
    List<LazadaUpdateProductSkuXml> skus;

}
