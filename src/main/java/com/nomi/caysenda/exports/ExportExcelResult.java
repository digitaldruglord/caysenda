package com.nomi.caysenda.exports;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExportExcelResult {
    List<ExportExcelHeaders> subHeader;
    ExportExcelHeaders mainHeader;
    ExportExcelBody body;
    String fileName;
    String sheetName;
    Integer startRow = 0;
}
