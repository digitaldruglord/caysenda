package com.nomi.caysenda.lazada.services.xml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Getter
@Setter
public class LazadaImageXml {
    @Element(name = "Image")
    String image;

    public LazadaImageXml(String image) {
        this.image = image;
    }
}
