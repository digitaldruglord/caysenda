package com.nomi.caysenda.exports;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportExcelHeader {
	String key;
	String name;
	Integer index;

	public ExportExcelHeader(String key, String name, Integer index) {
		this.key = key;
		this.name = name;
		this.index = index;
	}
}
