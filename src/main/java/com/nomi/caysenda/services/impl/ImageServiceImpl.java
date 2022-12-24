package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.entity.ImageEntity;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.repositories.CategoryRepository;
import com.nomi.caysenda.repositories.ImageRepository;
import com.nomi.caysenda.repositories.ProductRepository;
import com.nomi.caysenda.services.ImageService;
import com.nomi.caysenda.services.ProgressService;
import com.nomi.caysenda.utils.ConvertStringToUrl;
import com.nomi.caysenda.utils.ImageUtils;
import com.nomi.caysenda.utils.UploadFileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    TaskScheduler taskScheduler;
    String PROGRESS_IMAGE_ZIP = "PROGRESS_IMAGE_ZIP";
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ProgressService progressService;

    @Override
    public Map upload(MultipartHttpServletRequest request, String type) {
        type = type!=null?type:"img";
        Iterator<String> listName = request.getFileNames();
        Map map = new HashMap();
        List<ImageEntity> list = new ArrayList<>();
        while (listName.hasNext()){
            MultipartFile multipartFile  = request.getFile(listName.next());
            try {
                String[] extentsions = new String[]{"jpg","png"};
                File s = UploadFileUtils.getPath("static/");
                File folder = new File(s.getPath()+"/upload");
                if (FilenameUtils.isExtension(multipartFile.getOriginalFilename(),extentsions)){
                    map.put("success",true);

                    if (!folder.exists()){
                        folder.mkdirs();
                    }

                    File img = new File(folder.getPath()+"/"+multipartFile.getOriginalFilename());
                    multipartFile.transferTo(img);
                    if (!imageRepository.existsByPath("/resources/upload/"+FilenameUtils.getName(multipartFile.getOriginalFilename()))){
                        ImageEntity imageEntity =  new ImageEntity();
                        imageEntity.setName(FilenameUtils.getBaseName(img.getPath()));
                        imageEntity.setAttr(FilenameUtils.getBaseName(img.getPath()));
                        imageEntity.setPath("/resources/upload/"+img.getName());
                        imageEntity.setType(type);
                        list.add(imageRepository.save(imageEntity));
                    }

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        map.put("data",list);
        return map;
    }

    @Override
    public Map uploadZip(MultipartHttpServletRequest request) throws FileNotFoundException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File s = UploadFileUtils.getPath("static/");

        File folder = new File(s.getPath()+"/upload");
        if (!folder.exists()) folder.mkdirs();
        Iterator<String> listName = request.getFileNames();
        while (listName.hasNext()) {
            MultipartFile multipartFile = request.getFile(listName.next());
            try {
                ZipInputStream zipInputStream = new ZipInputStream(multipartFile.getInputStream(), StandardCharsets.ISO_8859_1);
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (zipEntry != null) {
                    zipInputStream.available();
                    String entryName = zipEntry.getName();
                    String name = ConvertStringToUrl.covertNameFileToUrl(entryName.substring(entryName.lastIndexOf("/")+1));
                    if (FilenameUtils.isExtension(name,List.of("jpeg","jpg","png"))){
                        /**
                         * info image
                         * */
                        String[] strings = entryName.split("/");
                        String sku = null;
                        String variationSku = null;
                        if (strings.length>=3){
                            sku = strings[0];
                            variationSku = strings[1];
                        }else {
                            sku = strings[0];
                        }
                        if (!imageRepository.existsByPath("/resources/upload/"+FilenameUtils.getName(name))){
                            ImageEntity imageEntity = new ImageEntity();
                            imageEntity.setName(FilenameUtils.getBaseName(name));
                            imageEntity.setAttr(FilenameUtils.getBaseName(name));
                            imageEntity.setPath("/resources/upload/"+FilenameUtils.getName(name));
                            imageEntity.setType("img");
                            imageEntity.setProductSKU(sku);
                            imageEntity.setVariantSKU(variationSku);
                            imageRepository.save(imageEntity);
                        }

                        FileOutputStream outputStream = new FileOutputStream(folder.getPath()+"/"+FilenameUtils.getName(name));
                        BufferedImage bufferedImage = ImageUtils.compressImage(zipInputStream.readAllBytes());
                        ImageIO.write(bufferedImage,FilenameUtils.getExtension(name),outputStream);
                        /**
                         * End info
                         * */
                    }
                    zipEntry = zipInputStream.getNextEntry();
                }
                zipInputStream.closeEntry();
                zipInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Page findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<ImageEntity> findAllByName(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public Page findAllByType(String type, Pageable pageable) {
        return imageRepository.findAllByType(type,pageable);
    }

    @Override
    public Page findAllByTypeAndName(String type, String name, Pageable pageable) {
        return imageRepository.findAllByTypeAndNameLike(type, "%"+name+"%", pageable);
    }

    @Override
    public Map deleteById(Integer id) {
        ImageEntity imageEntity = imageRepository.findById(id).get();
        Map map = new HashMap();
        map.put("success",false);

            File s = UploadFileUtils.getPath("static/");
            File folder = new File(s.getPath()+"/upload");
            File img = new File(folder.getPath()+"/"+FilenameUtils.getName(imageEntity.getPath()));
            if (img.exists()){
                img.delete();
                imageRepository.delete(imageEntity);
                map.put("success",true);
            }else {
                imageRepository.delete(imageEntity);
                map.put("success",true);
            }


        return map;
    }

    @Override
    public void createZipThumbnail(Integer category) throws IOException {
        CategoryEntity categoryEntity = categoryRepository.findById(category).orElse(null);
        if (categoryEntity!=null){
            List<ProductEntity> products = productRepository.findAllByCategoryDefault(category);
            File s = UploadFileUtils.getPath("static/");
            File folder = new File(s.getPath()+"/zip");
            if (!folder.exists()) folder.mkdirs();
            progressService.save(0,100,PROGRESS_IMAGE_ZIP);
            taskScheduler.schedule(() -> {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(folder.getPath()+"/"+categoryEntity.getSlug()+"-thumbnail.zip");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                AtomicReference<Integer> index= new AtomicReference<>(0);
                Integer size = products.size();
                products.forEach(productEntity -> {
                    progressService.save(index.getAndSet(index.get() + 1),size,PROGRESS_IMAGE_ZIP);
                    byte[] bytes = getInputImage(productEntity.getThumbnail());
                    if (bytes!=null){
                        ZipEntry zipEntry = new ZipEntry(FilenameUtils.getName(productEntity.getThumbnail()));
                        try {
                            zipOut.putNextEntry(zipEntry);
                            zipOut.write(bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
                try {
                    zipOut.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    progressService.delete(PROGRESS_IMAGE_ZIP);
                }

            },new Date());
            /**foler*/


        }
    }

    @Override
    public void createZipImages(Integer category) throws IOException {
        CategoryEntity categoryEntity = categoryRepository.findById(category).orElse(null);
        if (categoryEntity!=null){
            List<ProductEntity> products = productRepository.findAllByCategoryDefault(category);

            /**foler*/
            File s = UploadFileUtils.getPath("static/");
            File folder = new File(s.getPath()+"/zip");
            if (!folder.exists()) folder.mkdirs();
            progressService.save(0,100,PROGRESS_IMAGE_ZIP);
            taskScheduler.schedule(() -> {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(folder.getPath()+"/"+categoryEntity.getSlug()+"-image-all.zip");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                AtomicReference<Integer> index= new AtomicReference<>(0);
                Integer size = products.size();
                products.forEach(productEntity -> {
                    progressService.save(index.getAndSet(index.get() + 1),size,PROGRESS_IMAGE_ZIP);
                    productEntity.getGallery().forEach(image->{
                        byte[] bytes = getInputImage(image);
                        if (bytes!=null){
                            ZipEntry zipEntry = new ZipEntry(FilenameUtils.getName(image));

                            try {
                                zipOut.putNextEntry(zipEntry);
                                zipOut.write(bytes);
                            } catch (IOException e) {

                            }
                        }
                    });
                    Document document = Jsoup.parse(productEntity.getContent());
                    Elements images =  document.getElementsByTag("img");
                    images.forEach(img->{
                      String link =  img.attr("src");
                        byte[] bytes = getInputImage(link);
                        if (bytes!=null){
                            ZipEntry zipEntry = new ZipEntry(FilenameUtils.getName(link));

                            try {
                                zipOut.putNextEntry(zipEntry);
                                zipOut.write(bytes);
                            } catch (IOException e) {

                            }
                        }
                    });

                });
                try {
                    zipOut.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    progressService.delete(PROGRESS_IMAGE_ZIP);
                }

            },new Date());

        }
    }

    @Override
    public void updateImages(MultipartHttpServletRequest request) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File s = UploadFileUtils.getPath("static/");
        File folder = new File(s.getPath()+"/upload");
        if (!folder.exists()) folder.mkdirs();
        Iterator<String> listName = request.getFileNames();
        progressService.save(0,100,PROGRESS_IMAGE_ZIP);
        taskScheduler.schedule(()->{
            while (listName.hasNext()) {
                MultipartFile multipartFile = request.getFile(listName.next());
                Long total = multipartFile.getSize();
                Long current = Long.valueOf(0);
                try {
                    ZipInputStream zipInputStream = new ZipInputStream(multipartFile.getInputStream(), StandardCharsets.ISO_8859_1);
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    while (zipEntry != null) {
                        String entryName = zipEntry.getName();
                        String name = entryName.substring(entryName.lastIndexOf("/")+1);
                        if (FilenameUtils.isExtension(name,List.of("jpeg","jpg","png"))){
                            File file = new File(folder.getPath()+"/"+FilenameUtils.getName(name));
                            if (file.exists()){ file.delete(); }
                            FileOutputStream outputStream = new FileOutputStream(folder.getPath()+"/"+FilenameUtils.getName(name));
                            byte[] bytes = zipInputStream.readAllBytes();
                            BufferedImage bufferedImage = ImageUtils.compressImage(bytes);
                            ImageIO.write(bufferedImage,FilenameUtils.getExtension(name),outputStream);
                            outputStream.close();
                            current+=zipEntry.getCompressedSize();
                            progressService.save(current.intValue(),total.intValue(),PROGRESS_IMAGE_ZIP);
                        }
                        zipEntry = zipInputStream.getNextEntry();
                    }
                    progressService.delete(PROGRESS_IMAGE_ZIP);
                    zipInputStream.closeEntry();
                    zipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        },new Date());
    }

    public byte[] getInputImage(String url) {
        UrlValidator urlValidator = new UrlValidator();
        if (urlValidator.isValid(url)){
            try {
                ResponseEntity<byte[]> inputImage = restTemplate.getForEntity(url, byte[].class);
                if (inputImage.getStatusCodeValue() == 200) {
                    return inputImage.getBody();
                }
            }catch (HttpClientErrorException httpClientErrorException){

            }
        }else {
            byte[] bytes = null;
            try {
                File s = UploadFileUtils.getPath("static/");
                File folder = new File(s.getPath()+"/upload");
                File image = new File(folder.getPath()+"/"+ FilenameUtils.getName(url));
                InputStream inputStream = new FileInputStream(image);
                bytes =  inputStream.readAllBytes();
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bytes;

        }

        return null;
    }
}
