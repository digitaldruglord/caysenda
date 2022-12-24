package com.nomi.caysenda.lazada.services.xml;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Sku")
@Getter
@Setter
public class LazadaSkuXml {
    @Element
    String SellerSku;
    @Element(required = false)
    String color_family;
    @Element(required = false)
    String size;
    @Element
    Integer quantity;
    @Element
    Long price;
    @Element(required = false)
    Float package_length;
    @Element(required = false)
    Float package_height;
    @Element(required = false)
    Float package_weight;
    @Element(required = false)
    Float package_width;
    @Element(required = false)
    String package_content;
    @ElementList(name = "Images",required = false)
    List<String> images;

}
