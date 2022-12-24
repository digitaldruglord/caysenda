package com.nomi.caysenda.shopee.services.impl;

import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.entity.GroupVariantEntity;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.entity.VariantEntity;
import com.nomi.caysenda.repositories.CategoryRepository;
import com.nomi.caysenda.repositories.ProductRepository;
import com.nomi.caysenda.services.ProgressService;
import com.nomi.caysenda.shopee.entities.ShopeeCategoryEntity;
import com.nomi.caysenda.shopee.repositories.ShopeeCategoryRepository;
import com.nomi.caysenda.shopee.services.ShopeeService;
import com.nomi.caysenda.utils.ProductUtils;
import com.nomi.caysenda.utils.UploadFileUtils;
import lombok.SneakyThrows;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.List;

@Service
public class ShopeeServiceImpl implements ShopeeService {
    @Autowired CategoryRepository categoryRepository;
    @Autowired ProductRepository productRepository;
    @Autowired ProgressService progressService;
    @Autowired TaskScheduler taskScheduler;
    @Autowired Environment env;
    @Autowired ShopeeCategoryRepository shopeeCategoryRepository;

    @Override
    public void generateShopeeExcel(Integer catId, Integer shopeeCat) throws IOException, InvalidFormatException {
        /** folder */
        File folder = UploadFileUtils.getPath("static/shopeetemplates");
        /** product */
        CategoryEntity categoryEntity = categoryRepository.findById(catId).orElse(null);
        List<ProductEntity> products = productRepository.findAllByCat(categoryEntity.getId());
        progressService.save(0,100,"PROGRESS_IMAGE_ZIP");
        taskScheduler.schedule(() -> {
            Integer length = products.size();
            /** output */
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(folder.getPath()+"/"+categoryEntity.getSlug()+".xlsx");
                /** excel */
                File fileTemplate = new File(folder.getPath()+"/shopee-template.xlsx");
                InputStream inputStream = new FileInputStream(fileTemplate);
                XSSFWorkbook inputBook = new XSSFWorkbook(inputStream);
                XSSFSheet inputSheet = inputBook.getSheetAt(1);
                int i = 5;
                int progress = 0;
                for (ProductEntity productEntity:products){
                    for (VariantEntity variantEntity:productEntity.getVariants()){
                        Row cells = inputSheet.createRow(i);
                        /** cloumns */
                        Cell cat = cells.createCell(new CellAddress("A"+i).getColumn());
                        Cell productName = cells.createCell(new CellAddress("B"+i).getColumn());
                        Cell description = cells.createCell(new CellAddress("C"+i).getColumn());
                        Cell productSku = cells.createCell(new CellAddress("D"+i).getColumn());
                        Cell productCode = cells.createCell(new CellAddress("E"+i).getColumn());
                        Cell variant1 = cells.createCell(new CellAddress("F"+i).getColumn());
                        Cell valueVariant1 = cells.createCell(new CellAddress("G"+i).getColumn());
                        Cell imageVariant = cells.createCell(new CellAddress("H"+i).getColumn());
                        Cell variant2 = cells.createCell(new CellAddress("I"+i).getColumn());
                        Cell valueVariant2 = cells.createCell(new CellAddress("J"+i).getColumn());
                        Cell price = cells.createCell(new CellAddress("K"+i).getColumn());
                        Cell stock = cells.createCell(new CellAddress("L"+i).getColumn());
                        Cell variantSku = cells.createCell(new CellAddress("M"+i).getColumn());
                        Cell image = cells.createCell(new CellAddress("N"+i).getColumn());
                        Cell weight = cells.createCell(new CellAddress("W"+i).getColumn());
                        Cell shippingunit = cells.createCell(new CellAddress("AA"+i).getColumn());
                        Cell shippingunit2 = cells.createCell(new CellAddress("AB"+i).getColumn());
                        Cell shippingunit3 = cells.createCell(new CellAddress("AC"+i).getColumn());
                        /** set values */
                        GroupVariantEntity groupVariantEntity = ProductUtils.getGroupByGroupSku(productEntity,variantEntity.getParent());
                        if (groupVariantEntity!=null){
                            variant1.setCellValue("Biến thể");
                            valueVariant1.setCellValue(groupVariantEntity.getName());
                            variant2.setCellValue("Kích thước");
                            valueVariant2.setCellValue(variantEntity.getNameVi());
                            if (groupVariantEntity.getThumbnail()!=null){
                                imageVariant.setCellValue(env.getProperty("spring.domain")+groupVariantEntity.getThumbnail());
                            }

                        }else {
                            variant1.setCellValue("Biến thể");
                            valueVariant1.setCellValue(variantEntity.getNameVi());
                            if (variantEntity!=null){
                                imageVariant.setCellValue(env.getProperty("spring.domain")+variantEntity.getThumbnail());
                            }
                        }
                        productCode.setCellValue(productEntity.getSkuVi());

                        cat.setCellValue(shopeeCat);
                        description.setCellValue(productEntity.getDescription());
                        productName.setCellValue(productEntity.getNameVi()+"-"+variantEntity.getNameVi());
                        productSku.setCellValue(productEntity.getSkuVi());
                        price.setCellValue(variantEntity.getPrice());
                        stock.setCellValue(variantEntity.getStock());
                        image.setCellValue(env.getProperty("spring.domain")+(variantEntity.getThumbnail()!=null?variantEntity.getThumbnail():productEntity.getThumbnail()));
                        int indexImage = 0;
                        for (String s:productEntity.getGallery()){
                            if (indexImage>=3) break;
                            Cell cell = cells.createCell(new CellAddress("O"+i).getColumn()+indexImage);
                            cell.setCellValue(env.getProperty("spring.domain")+(s));
                            indexImage++;
                        }
                        variantSku.setCellValue(variantEntity.getSkuVi());
                        weight.setCellValue(variantEntity.getWeight()!=null?variantEntity.getWeight():1);
                        shippingunit.setCellValue("Mở");
                        shippingunit2.setCellValue("Mở");
                        i++;
                    }
                    progressService.save(++progress,length,"PROGRESS_IMAGE_ZIP");
                }

                inputBook.write(outputStream);
                inputStream.close();
                inputBook.close();
                outputStream.close();
                progressService.delete("PROGRESS_IMAGE_ZIP");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        },new Date());




    }

    @SneakyThrows
    @Override
    public void importShopeeCategory() throws IOException {
        /** folder */
        File folder = UploadFileUtils.getPath("static/shopeetemplates");

        File fileTemplate = new File(folder.getPath() + "/shopee-template.xlsx");
        InputStream inputStream = new FileInputStream(fileTemplate);
        XSSFWorkbook inputBook = new XSSFWorkbook(inputStream);
        XSSFSheet inputSheet = inputBook.getSheetAt(3);
        int i=2;
        int length = inputSheet.getLastRowNum();
        while (i<length){
            Row cells = inputSheet.getRow(i);
            Cell categoryNameCell = cells.getCell(new CellAddress("A"+i).getColumn());
            Cell categoryIdCell = cells.getCell(new CellAddress("B"+i).getColumn());
            String categoryName = categoryNameCell.getStringCellValue();
            Double aDouble = categoryIdCell.getNumericCellValue();
            ShopeeCategoryEntity categoryEntity = new ShopeeCategoryEntity();
            categoryEntity.setName(categoryName.substring(categoryName.indexOf("-")+1,categoryName.length()).trim());
            categoryEntity.setId(aDouble.intValue());
            shopeeCategoryRepository.save(categoryEntity);
            i++;
        }

    }

    @Override
    public Page<ShopeeCategoryEntity> findALlByName(String name, Pageable pageable) {
        if (name!=null && !name.equals("")){
            return shopeeCategoryRepository.findAllByNameLike("%"+name+"%",pageable);
        }else {
            return  shopeeCategoryRepository.findAll(pageable);
        }
    }
}
