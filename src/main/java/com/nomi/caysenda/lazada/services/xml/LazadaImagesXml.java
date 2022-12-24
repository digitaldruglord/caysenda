package com.nomi.caysenda.lazada.services.xml;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.*;

import java.util.List;

@Root(name = "Images",strict = false)
@Getter
@Setter
public class LazadaImagesXml {
    @ElementList
    List<LazadaImageXml> images;

    public LazadaImagesXml(List<LazadaImageXml> images) {
        this.images = images;
    }
}
