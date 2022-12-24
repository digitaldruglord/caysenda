package com.nomi.caysenda.lazada.services.xml;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Setter
@Root(name = "MultiWarehouseInventory")
public class LazadaMultiWarehouseInventoryXml {
    @Element(name = "WarehouseCode")
    String warehouseCode;
    @Element(name = "Quantity")
    Integer quantity;
}
