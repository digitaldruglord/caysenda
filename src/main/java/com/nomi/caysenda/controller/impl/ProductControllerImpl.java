package com.nomi.caysenda.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomi.caysenda.controller.ProductController;
import com.nomi.caysenda.dialect.utils.ProductUtilsDialect;
import com.nomi.caysenda.dto.ProductDTO;
import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.entity.PageEntity;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.exceptions.PageNotFountException;
import com.nomi.caysenda.options.model.PriceOption;
import com.nomi.caysenda.repositories.AddressProvinceRepository;
import com.nomi.caysenda.services.*;
import com.nomi.caysenda.utils.AddressUtils;
import com.nomi.caysenda.utils.ProductUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Controller
@RequestMapping("")
public class ProductControllerImpl implements ProductController {
    @Autowired ProductService productService;
    @Autowired CategoryService categoryService;
    @Autowired CartService cartService;
    @Autowired PageService pageService;
    @Autowired Environment env;
    @Autowired SettingService settingService;
    @Autowired ImageService imageService;
    @Autowired KeywordService keywordService;
    @Autowired AddressProvinceRepository provinceRepository;

    @Override
    public ModelAndView productsView(Integer page, String sort, Integer pageSize, String keyword, String catSlug, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("product-pages/product-list/index");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "topFlag"));
        switch (sort==null?"":sort){
            case "minPrice": orders.add(new Sort.Order(Sort.Direction.ASC,settingService.getSortField())); break;
            case "new": orders.add(new Sort.Order(Sort.Direction.ASC,"id"));break;
            case "maxPrice": orders.add(new Sort.Order(Sort.Direction.DESC,settingService.getSortField())); break;
            default:orders.add(new Sort.Order(Sort.Direction.ASC,"id"));
        }

        Pageable pageable = PageRequest.of(page!=null?page-1:0,pageSize!=null?pageSize:20, Sort.by(orders));
        view.addObject("categorycount",categoryService.findAllAndCount());
        Page<ProductDTO> pageProduct = null;
        if (keyword!=null && catSlug==null){
            pageProduct = productService.search(keyword,null,pageable);
            keywordService.save(keyword);
        }else if (keyword!=null && catSlug!=null && !catSlug.equals("all")){
            pageProduct = productService.search(keyword,catSlug,pageable);
            keywordService.save(keyword);
        }else {
            pageProduct = productService.findAllForProductDTO(pageable);
        }
        Page<ProductDTO> rand = productService.findRandForProductDTO(PageRequest.of(0,5));
        List<CategoryEntity> categories = categoryService.findAllByParent(0);
        view.addObject("categories",categories);
        view.addObject("products",pageProduct.getContent());
        view.addObject("rand",rand.getContent());
        view.addObject("totalPages",pageProduct.getTotalPages());
        view.addObject("page",page!=null?page:1);
        view.addObject("pageSize",pageSize!=null?pageSize:20);
        view.addObject("slug","/san-pham");
        view.addObject("params",request.getParameterMap());

        return view;
    }

	@Override
	public ModelAndView hotProducts(Integer page, String sort, Integer pageSize, String keyword, String catSlug, HttpServletRequest request) {
        /** category*/
        ModelAndView view = new ModelAndView("product-pages/product-list/index");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "topFlag"));
        switch (sort==null?"":sort){
            case "minPrice": orders.add(new Sort.Order(Sort.Direction.ASC,settingService.getSortField())); break;
            case "new": orders.add(new Sort.Order(Sort.Direction.ASC,"id"));break;
            case "maxPrice": orders.add(new Sort.Order(Sort.Direction.DESC,settingService.getSortField())); break;
            default:orders.add(new Sort.Order(Sort.Direction.ASC,"id"));
        }
        Pageable pageable = PageRequest.of(page!=null?page-1:0,pageSize!=null?pageSize:20,Sort.by(orders));
        Page<ProductDTO> pageProduct = null;
        if (keyword!=null){
            pageProduct = productService.search(keyword,"hot-product",pageable);
            keywordService.save(keyword);
        }else {
            pageProduct = productService.findByHotProductForProductDTO(pageable);
        }

        Page<ProductDTO> rand = productService.findRandForProductDTO(PageRequest.of(0,5));;
        List<CategoryEntity> categories = categoryService.findAllByParent(0);

        view.addObject("categories",categories);
        view.addObject("products",pageProduct.getContent());
        view.addObject("rand",rand.getContent());
        view.addObject("totalPages",pageProduct.getTotalPages());
        view.addObject("page",page!=null?page:1);
        view.addObject("pageSize",pageSize!=null?pageSize:20);
        view.addObject("slug","/san-pham-hot");
        view.addObject("params",request.getParameterMap());
        view.addObject("categoryData",null);
        view.addObject("parent",null);
        return view;
	}

	@Override
    public ModelAndView detailt(String categorySlug, String productSlug, HttpServletRequest request) throws JsonProcessingException, PageNotFountException {
        ProductUtilsDialect productUtilsDialect = new ProductUtilsDialect();
        PriceOption priceOption = (PriceOption) request.getAttribute("priceSetting");
        ModelAndView view = new ModelAndView("product-pages/product-detailt/index");
        ProductEntity productEntity = productService.findBySlugVi(productSlug);
        if (productEntity==null) throw new PageNotFountException();
        Optional<CategoryEntity> optional = categoryService.findById(productEntity.getCategoryDefault());
        Long countIncart = cartService.countByProduct(productEntity.getId());
        Boolean isRetailt = ProductUtils.isRetailt(productEntity,priceOption);
        Long max = productUtilsDialect.getPriceByTypeNF(priceOption.getPriceDefault(),productEntity,"max");
        Long min = productUtilsDialect.getPriceByTypeNF(priceOption.getPriceDefault(),productEntity,"min");
        Long vip1 = productUtilsDialect.getPriceByTypeNF(priceOption.getPrice1(),productEntity);
        Long vip2 = productUtilsDialect.getPriceByTypeNF(priceOption.getPrice2(),productEntity);
        Long vip3 = productUtilsDialect.getPriceByTypeNF(priceOption.getPrice3(),productEntity);

        Map map = new HashMap();
        map.put("id",productEntity.getId());
        map.put("min",min);
        map.put("max",max);
        map.put("incart",countIncart);
        map.put("isRetailt", isRetailt);
        map.put("vip1",vip1);
        map.put("vip2",vip2);
        map.put("vip3",vip3);
        map.put("conditiondefault",productEntity.getConditiondefault());
        map.put("condition1",productEntity.getCondition1());
        map.put("condition2",productEntity.getCondition2());
        map.put("condition3",productEntity.getCondition3());
        map.put("isExistGroup",false);
        if (productEntity.getVariantGroup()!=null){
            if (productEntity.getVariantGroup().size()>0){
                map.put("isExistGroup",true);
            }
        }
        view.addObject("categoryDefault",optional.get());
        view.addObject("product",productEntity);
        view.addObject("link",env.getProperty("spring.domain")+"/"+optional.get().getSlug()+"/"+productEntity.getSlugVi());
        view.addAllObjects(map);
        String jsonProduct = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(map);
        view.addObject("jsonproduct",jsonProduct);
        return view;
    }

    @Override
    public ModelAndView productsView(String slug, Integer page, String keyword, String sort, Integer pageSize, HttpServletRequest request) throws PageNotFountException {
        /** page */
        if (pageService.existsBySlug(slug)){
            ModelAndView view = new ModelAndView("post-pages/page-detailt/index");
            PageEntity pageEntity = pageService.findBySlug(slug);
            pageEntity.setCss(pageEntity.getCss().replace("&#39;",""));
            if (pageEntity.getType().equals("simpleproduct")){
                view.addObject("provinces", AddressUtils.convertEntityToDTO(provinceRepository.findAll(),null,null));
               view.setViewName("product-pages/page-product/index");
            }
            view.addObject("data",pageEntity);
            return view;
        }

        /** category*/
        ModelAndView view = new ModelAndView("product-pages/product-list/index");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "topFlag"));
        switch (sort==null?"":sort){
            case "minPrice": orders.add(new Sort.Order(Sort.Direction.ASC,settingService.getSortField())); break;
            case "new": orders.add(new Sort.Order(Sort.Direction.ASC,"id"));break;
            case "maxPrice": orders.add(new Sort.Order(Sort.Direction.DESC,settingService.getSortField())); break;
            default:orders.add(new Sort.Order(Sort.Direction.ASC,"id"));
        }
        Pageable pageable = PageRequest.of(page!=null?page-1:0,pageSize!=null?pageSize:20,Sort.by(orders));
        Page<ProductDTO> pageProduct = null;
        if (keyword!=null){
            pageProduct = productService.search(keyword,slug,pageable);
            keywordService.save(keyword);
        }else {
            pageProduct = productService.findByCategorySlugForProductDTO(slug,pageable);
        }
        CategoryEntity categoryEntity = categoryService.findBySlug(slug).orElse(null);
        if (pageProduct.getContent().size()<=0 && categoryEntity==null) throw new PageNotFountException();

        Page<ProductDTO> rand = productService.findRandForProductDTO(PageRequest.of(0,5));
        CategoryEntity parent = categoryService.findById(categoryEntity.getParent()).orElse(null);
        List<CategoryEntity> categories = categoryService.findAllByParent(categoryEntity.getId());
        if (categories.size()<=0) {
            categories = categoryService.findAllByParent(categoryEntity.getParent());
        }

        view.addObject("categories",categories);
        view.addObject("products",pageProduct.getContent());
        view.addObject("rand",rand.getContent());
        view.addObject("totalPages",pageProduct.getTotalPages());
        view.addObject("page",page!=null?page:1);
        view.addObject("pageSize",pageSize!=null?pageSize:20);
        view.addObject("slug","/"+slug);
        view.addObject("params",request.getParameterMap());
        view.addObject("categoryData",categoryEntity);
        view.addObject("parent",parent);
        return view;
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadImages(Integer id, HttpServletRequest request, HttpServletResponse response) {
        HttpHeaders responseHeader = new HttpHeaders();
        try {
            ProductEntity productDTO = productService.findById(id);
            if (productDTO!=null){
                responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                responseHeader.set("Content-disposition", "attachment; filename="+id+".zip");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);

                for (String srcFile : productDTO.getGallery()) {
                    byte[] bytes = imageService.getInputImage(srcFile);
                    ZipEntry zipEntry = new ZipEntry(FilenameUtils.getName(srcFile));
                    zipOut.putNextEntry(zipEntry);
                    zipOut.write(bytes);
                }
                zipOut.close();
                InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                byteArrayOutputStream.close();
                InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
                return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(null, responseHeader, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(null, responseHeader, HttpStatus.NOT_FOUND);

        }
    }
}
