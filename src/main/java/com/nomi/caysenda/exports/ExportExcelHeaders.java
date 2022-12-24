package com.nomi.caysenda.exports;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExportExcelHeaders {
	List<ExportExcelHeader> headers;

	public ExportExcelHeaders() {
		headers = new ArrayList<>();
	}

	public ExportExcelHeaders(List<ExportExcelHeader> headers) {
		this.headers = headers;
	}
}
