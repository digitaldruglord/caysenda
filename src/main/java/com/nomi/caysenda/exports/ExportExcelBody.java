package com.nomi.caysenda.exports;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Getter
@Setter
public class ExportExcelBody {
	Integer size = 0;
	List<Map> results = new ArrayList<>();

	public ExportExcelBody() {
	}

	public ExportExcelBody(List<Map> results, Integer size) {
		this.size = size;
		this.results = results;
	}
}
