package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.entity.DictionaryEntity;
import com.nomi.caysenda.extension.repositories.DictionaryRepository;
import com.nomi.caysenda.services.DictionaryService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Service()
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    DictionaryRepository dictionaryRepository;

    @Override
    public Page<DictionaryEntity> findAll(Pageable pageable) {
        return dictionaryRepository.findAll(pageable);
    }

    @Override
    public DictionaryEntity findById(Integer id) {
        return dictionaryRepository.findById(id).orElse(null);
    }

    @Override
    public DictionaryEntity findByZhWord(String zhWord) {
        return dictionaryRepository.findByZhWord(zhWord);
    }

    @Override
    public Page<DictionaryEntity> findAllByZhWordAndViWord(String search, Pageable pageable) {
        return dictionaryRepository.findAllByZhWordLikeOrViWordLike("%"+search+"%","%"+search+"%",pageable);
    }

    @Override
    public DictionaryEntity createAndupdate(Integer id, String zhWord, String viWord) {
        DictionaryEntity dictionaryEntity = new DictionaryEntity();
        if (id!=null){
            dictionaryEntity = findById(id);
        }
        dictionaryEntity.setZhWord(zhWord);
        dictionaryEntity.setViWord(viWord);
        try {
            dictionaryEntity = dictionaryRepository.save(dictionaryEntity);
        }catch (DataIntegrityViolationException exception){
            return null;
        }
        return dictionaryEntity;
    }

    @Override
    public void deleteById(Integer id) {
        DictionaryEntity dictionaryEntity = findById(id);
        if (dictionaryEntity!=null){
            dictionaryRepository.delete(dictionaryEntity);
        }
    }

    @Override
    public void deleteByZhWord(String zhWord) {
        DictionaryEntity dictionaryEntity = findByZhWord(zhWord);
        if (dictionaryEntity!=null){
            dictionaryRepository.delete(dictionaryEntity);
        }
    }

    @Override
    public void uploadExcel(MultipartHttpServletRequest request) {
        Iterator<String> fileNames = request.getFileNames();
        while (fileNames.hasNext()){
            String filename = fileNames.next();
            MultipartFile multipartFile = request.getFile(filename);
            try {
                InputStream inputStream = multipartFile.getInputStream();
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                int i = 1;
                int rows = sheet.getLastRowNum();
                while (i<=rows) {
                    Row row = sheet.getRow(i);
                    Cell zhWordCell = row.getCell(new CellAddress("H"+i).getColumn());
                    Cell viWordCell = row.getCell(new CellAddress("I"+i).getColumn());
                    Cell viNameCell = row.getCell(new CellAddress("G"+i).getColumn());
                    Cell zhNameCell = row.getCell(new CellAddress("F"+i).getColumn());
                    if (viNameCell!=null && zhNameCell!=null){
                        String zhName = zhNameCell.getStringCellValue();
                        String viName = viNameCell.getStringCellValue();
                        DictionaryEntity dictionaryEntity = dictionaryRepository.findByZhWord(zhName);
                        if (dictionaryEntity==null) dictionaryEntity = new DictionaryEntity();
                        dictionaryEntity.setZhWord(zhName);
                        dictionaryEntity.setViWord(viName);
                        dictionaryRepository.save(dictionaryEntity);
                    }
                    if (zhWordCell!=null && viWordCell!=null){
                        String zhWord = zhWordCell.getStringCellValue();
                        String viWord = viWordCell.getStringCellValue();
                        DictionaryEntity dictionaryEntity = dictionaryRepository.findByZhWord(zhWord);
                        if (dictionaryEntity==null) dictionaryEntity = new DictionaryEntity();
                        dictionaryEntity.setZhWord(zhWord);
                        dictionaryEntity.setViWord(viWord);
                        dictionaryRepository.save(dictionaryEntity);
                    }
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
