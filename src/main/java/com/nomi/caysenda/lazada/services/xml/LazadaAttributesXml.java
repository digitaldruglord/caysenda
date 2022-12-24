package com.nomi.caysenda.lazada.services.xml;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Attributes")
@Getter
@Setter
public class LazadaAttributesXml {
    @Element
    String name;
    @Element(required = false)
    String short_description;
    @Element
    String brand;
    @Element
    String model;
    @Element
    String kid_years;
    @Element(required = false)
    String video;
    @Element
    String delivery_option_sof;

}
