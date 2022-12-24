package com.nomi.caysenda.exports;

import com.nomi.caysenda.exports.exceptions.NotExistsDataException;
import com.nomi.caysenda.services.ProgressService;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
public class ExportExcelFileBuilder {
	ExportExcelResult data;
	private XSSFWorkbook ouputbook;
	private XSSFSheet outSheet;
	private ProgressService progressService;
	private final String PROCESS_CODE = "EXPORT_EXCEL";
	public ExportExcelFileBuilder() {
	}

	public ExportExcelFileBuilder(ExportExcelResult results, ProgressService progressService) {
		this.data = results;
		this.progressService = progressService;
	}

	public void builder() throws NotExistsDataException, IOException {
		if (data != null) {
			ouputbook = new XSSFWorkbook();
			outSheet = ouputbook.createSheet(data.getSheetName());
			builderHeader();
			builderContent();
			File folder =  ResourceLoader.getPath("static/excel/");
			File file = new File(folder.getPath() + "/" +  data.getFileName() + "_" + new Date().getTime() + ".xlsx");

			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream outputStream = new FileOutputStream(file);
			ouputbook.write(outputStream);
			ouputbook.close();
			outputStream.close();
		} else {
			throw new NotExistsDataException("Not exists data");
		}
	}

	private void builderHeader() {
		AtomicReference<Integer> rowIndex = new AtomicReference<>(data.getStartRow());
		if (data.subHeader != null && data.subHeader.size() > 0) {
			data.subHeader.forEach(exportExcelHeaders -> {
				Row cells = outSheet.createRow(rowIndex.getAndSet(rowIndex.get() + 1));
				exportExcelHeaders.headers.forEach(exportExcelHeader -> {
					Cell cell = cells.createCell(exportExcelHeader.getIndex());
					cell.setCellValue(exportExcelHeader.getName());
				});
			});
		}

		if (data.mainHeader != null && data.mainHeader.getHeaders().size() > 0) {
			Row cells = outSheet.createRow(rowIndex.getAndSet(rowIndex.get() + 1));
			data.mainHeader.getHeaders().forEach(exportExcelHeader -> {
				Cell cell = cells.createCell(exportExcelHeader.getIndex());
				cell.setCellValue(exportExcelHeader.getName());
			});
		}
	}

	private void builderContent() {
		ExportExcelBody body = data.getBody();
		List<ExportExcelHeaders> subHeader = data.subHeader;
		List<ExportExcelHeader> header = data.mainHeader.getHeaders();
		AtomicReference<Integer> index = new AtomicReference<>((subHeader != null ? subHeader.size() : 0) + data.getStartRow() + 1);
		Integer count = 0;
		setProgress(count);
		if (body.size > 0) {
			for (Map map: body.results) {
				if (!isRunning()) break;
				Row cells = outSheet.createRow(index.getAndSet(index.get() + 1));
				header.forEach(exportExcelHeader -> {
					if (map.get(exportExcelHeader.getKey()) != null) {
						Cell cell = cells.createCell(exportExcelHeader.getIndex());
						setValue(cell, map.get(exportExcelHeader.getKey()));
					}
				});
				setProgress(count);
				count++;
			}

		}
	}
	private boolean isRunning() {
		return progressService.findByCode(PROCESS_CODE).getRunning();
	}

	private void setProgress(Integer current) {
		System.out.println(current + " = " +data.body.size);
		if (current < data.body.size -1) {
			progressService.save(current, data.body.size, PROCESS_CODE, true);
		} else {
			progressService.save(current, data.body.size, PROCESS_CODE, false);
		}
	}
	private void setValue(Cell cell, Object value) {
		if (value instanceof String) {
			cell.setCellValue(String.valueOf(value));
		} else if (value instanceof Integer) {
			cell.setCellValue(Integer.valueOf(String.valueOf(value)));
		} else if (value instanceof Float) {
			cell.setCellValue(Float.valueOf(String.valueOf(value)));
		} else if (value instanceof Double) {
			cell.setCellValue(Double.valueOf(String.valueOf(value)));
		} else  {
			cell.setCellValue(String.valueOf(value));
		}
	}
}
