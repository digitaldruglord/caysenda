package com.nomi.caysenda.lazada.services.xml;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.Map;
@Root(name = "Product")
@Getter
@Setter
public class LazadaProductXml {
    @Element(name = "PrimaryCategory")
    Integer primaryCategory;
    @Element(name = "SPUId",required = false)
    String SPUId;
    @Element(name = "AssociatedSku",required = false)
    String associatedSku;
    @Element(name = "Attributes")
    LazadaAttributesXml attributes;
    @ElementList(name = "Images")
    List<String> images;
    @ElementList(name = "Skus")
    List<LazadaSkuXml> skus;

}
