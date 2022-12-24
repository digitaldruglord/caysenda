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
public class LazadaUpdateProductSkuXml {

    @Element(name = "SkuId")
    String SkuId;
    @Element(name = "SellerSku")
    String sellerSku;
    @Element(name = "Price")
    Double price;
    @Element(name = "SalePrice")
    Double salePrice;
    @Element(name = "SaleStartDate",required = false)
    String saleStartDate;
    @Element(name = "SaleEndDate",required = false)
    String saleEndDate;
    @ElementList(name = "MultiWarehouseInventories",inline = true,required = false)
    List<LazadaMultiWarehouseInventoryXml> warehouseInventoryXmls;
}
