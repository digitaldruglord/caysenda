package com.nomi.caysenda.controller.impl;

import com.nomi.caysenda.controller.UserController;
import com.nomi.caysenda.controller.requests.user.UserUpdateRequest;
import com.nomi.caysenda.dto.AddressDTO;
import com.nomi.caysenda.dto.CategoryDTO;
import com.nomi.caysenda.dto.OrderDTO;
import com.nomi.caysenda.entity.OrderDetailtEntity;
import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.entity.UserEntity;
import com.nomi.caysenda.exceptions.user.UserRegisterException;
import com.nomi.caysenda.ghn.service.GHNService;
import com.nomi.caysenda.repositories.OrderDetailtRepository;
import com.nomi.caysenda.security.CustomUserDetail;
import com.nomi.caysenda.security.SecurityUtils;
import com.nomi.caysenda.services.AddressService;
import com.nomi.caysenda.services.CategoryService;
import com.nomi.caysenda.services.OrderService;
import com.nomi.caysenda.services.UserService;
import com.nomi.caysenda.utils.UploadFileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/tai-khoan")
public class UserControllerImpl implements UserController {
    @Autowired OrderService orderService;
    @Autowired OrderDetailtRepository detailtRepository;
    @Autowired AddressService addressService;
    @Autowired UserService userService;
    @Autowired CategoryService categoryService;
    @Autowired GHNService ghnService;

    @Override
    public ModelAndView user() {
        ModelAndView view = new ModelAndView("user/pages/profile");
        List<AddressDTO> list = addressService.findAllAddressDTOById();
        UserEntity userEntity = userService.findById(SecurityUtils.getPrincipal().getUserId()).orElse(null);
        view.addObject("user",userEntity);
        view.addObject("list", list);
        return view;
    }

    @Override
    public ModelAndView updateUser(UserUpdateRequest updateRequest) {
        ModelAndView view = new ModelAndView("user/pages/profile");
        try {
            userService.update(updateRequest);

        } catch (UserRegisterException e) {
            view.addObject("success", false);
        }
        List<AddressDTO> list = addressService.findAllAddressDTOById();
        UserEntity userEntity = userService.findById(SecurityUtils.getPrincipal().getUserId()).orElse(null);
        view.addObject("user",userEntity);
        view.addObject("list", list);
        return view;
    }

    @Override
    public ModelAndView login() {
        ModelAndView view = new ModelAndView("user/index");
        return view;
    }

    @Override
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ModelAndView("redirect:/");
    }

    @Override
    public ModelAndView orders(Integer page, Integer pageSize, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("user/pages/order");
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        Pageable pageable = PageRequest.of(page != null ? page - 1 : 0, pageSize != null ? pageSize : 20, Sort.by(Sort.Direction.DESC, "id"));
        Page<OrderDTO> orderDTOS = orderService.findAllForOrderDTOByUserId(userDetail.getUserId(), pageable);
        if (userDetail != null) {
            view.addObject("orders", orderDTOS.getContent());
        }
        view.addObject("params", request.getParameterMap());
        view.addObject("slug", "/tai-khoan/hoa-don");
        view.addObject("page", page != null ? page : 1);
        view.addObject("totalPages", orderDTOS.getTotalPages());
        return view;
    }

    @Override
    public ModelAndView orderDetailt(Integer orderId) {
        ModelAndView view = new ModelAndView("user/pages/order-detailt");
        OrderEntity orderEntity = orderService.findById(orderId);
        List<OrderDetailtEntity> detailts = detailtRepository.findAllByOrderEntity_Id(orderEntity.getId());
        view.addObject("order", orderEntity);
        view.addObject("detailts", detailts);
        view.addObject("orderStatus", orderService.getStatus().get(orderEntity.getStatus()));
        view.addObject("tracking", ghnService.tracking(orderId));
        return view;
    }

    @Override
    public ModelAndView address() {
        ModelAndView view = new ModelAndView("user/pages/address");
        List<AddressDTO> list = addressService.findAllAddressDTOById();
        view.addObject("list", list);
        return view;
    }

    @Override
    public ModelAndView downloadImage() {
        ModelAndView view = new ModelAndView("user/pages/downloadImage");
        List<String> files = new ArrayList<>();
        List<CategoryDTO> categoryDTOS = categoryService.findAllByDomainForCategoryDTO();
        File s = UploadFileUtils.getPath("static/");
        File folder = new File(s.getPath() + "/zip");
        files.addAll(Arrays.stream(folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String fileName = FilenameUtils.getBaseName(file.getName());
                if (fileName.indexOf("image")>0){
                    String slug = fileName.substring(0,fileName.indexOf("image")-1);
                    if (categoryService.isExistsBySlugAndDomain(slug)){
                        return Pattern.compile("image.zip").matcher(file.getPath()).find();
                    }else {
                        return  false;
                    }
                }else {
                    return false;
                }


            }

        })).map(file -> FilenameUtils.getName(file.getPath())).collect(Collectors.toList()));

        view.addObject("files", files);
        return view;
    }

    @Override
    public ModelAndView downloadPriceQuote() {
        ModelAndView view = new ModelAndView("user/pages/download-price-quote");
        List<String> files = new ArrayList<>();

        File s = UploadFileUtils.getPath("static/");
        File folder = new File(s.getPath() + "/excel");
        files.addAll(Arrays.stream(folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String fileName = FilenameUtils.getBaseName(file.getName());
                if (fileName.indexOf("bao-gia")>0){
                    String slug = fileName.substring(0,fileName.indexOf("bao-gia")-1);
                    if (categoryService.isExistsBySlugAndDomain(slug)){
                        return Pattern.compile("bao-gia.xlsx").matcher(file.getPath()).find();
                    }else {
                        return  false;
                    }
                }else {
                    return false;
                }


            }
        })).map(file -> FilenameUtils.getName(file.getPath())).collect(Collectors.toList()));

        view.addObject("files", files);
        return view;
    }

    @Override
    public ModelAndView downloadPriceQuoteWithThumbnail() {
        ModelAndView view = new ModelAndView("user/pages/download-price-quote-with-thumbnail");
        List<String> files = new ArrayList<>();

        File s = UploadFileUtils.getPath("static/");
        File folder = new File(s.getPath() + "/excel");
        files.addAll(Arrays.stream(folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String fileName = FilenameUtils.getBaseName(file.getName());
                if (fileName.indexOf("bao-gia")>0){
                    String slug = fileName.substring(0,fileName.indexOf("bao-gia")-1);
                    if (categoryService.isExistsBySlugAndDomain(slug)){
                        return Pattern.compile("bao-gia-kem-hinh-anh.xlsx").matcher(file.getPath()).find();
                    }else {
                        return  false;
                    }
                }else {
                    return false;
                }


            }
        })).map(file -> FilenameUtils.getName(file.getPath())).collect(Collectors.toList()));

        view.addObject("files", files);
        return view;
    }

    @Override
    public ModelAndView downloadImages() {
        ModelAndView view = new ModelAndView("user/pages/downloadImages");
        List<String> files = new ArrayList<>();

        File s = UploadFileUtils.getPath("static/");
        File folder = new File(s.getPath() + "/zip");
        files.addAll(Arrays.stream(folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String fileName = FilenameUtils.getBaseName(file.getName());
                if (fileName.indexOf("images")>0){
                    String slug = fileName.substring(0,fileName.indexOf("images")-1);
                    if (categoryService.isExistsBySlugAndDomain(slug)){
                        return Pattern.compile("images.zip").matcher(file.getPath()).find();
                    }else {
                        return  false;
                    }
                }else {
                    return false;
                }


            }
        })).map(file -> FilenameUtils.getName(file.getPath())).collect(Collectors.toList()));

        view.addObject("files", files);
        return view;
    }

    @Override
    public ModelAndView downloadImageSKU() {
        ModelAndView view = new ModelAndView("user/pages/downloadimage-sku");
        List<String> files = new ArrayList<>();

        File s = UploadFileUtils.getPath("static/");
        File folder = new File(s.getPath() + "/zip");
        files.addAll(Arrays.stream(folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String fileName = FilenameUtils.getBaseName(file.getName());
                if (fileName.indexOf("image-sku")>0){
                    String slug = fileName.substring(0,fileName.indexOf("image-sku")-1);
                    if (categoryService.isExistsBySlugAndDomain(slug)){
                        return Pattern.compile("image-sku.zip").matcher(file.getPath()).find();
                    }else {
                        return  false;
                    }
                }else {
                    return false;
                }

            }
        })).map(file -> FilenameUtils.getName(file.getPath())).collect(Collectors.toList()));

        view.addObject("files", files);
        return view;
    }

    @Override
    public ModelAndView downloadImagesSKU() {
        ModelAndView view = new ModelAndView("user/pages/download-images-sku");
        List<String> files = new ArrayList<>();

        File s = UploadFileUtils.getPath("static/");
        File folder = new File(s.getPath() + "/zip");
        files.addAll(Arrays.stream(folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String fileName = FilenameUtils.getBaseName(file.getName());
                if (fileName.indexOf("images-sku")>0){
                    String slug = fileName.substring(0,fileName.indexOf("images-sku")-1);
                    if (categoryService.isExistsBySlugAndDomain(slug)){
                        return Pattern.compile("images-sku.zip").matcher(file.getPath()).find();
                    }else {
                        return  false;
                    }
                }else {
                    return false;
                }

            }
        })).map(file -> FilenameUtils.getName(file.getPath())).collect(Collectors.toList()));

        view.addObject("files", files);
        return view;
    }

    @Override
    public ModelAndView changePassword() {
        ModelAndView view = new ModelAndView("user/pages/password");
        return view;
    }

    @Override
    public ModelAndView updateChangePassword(String passwordOld, String passwordNew, String passwordNewConfirm) {
        ModelAndView view = new ModelAndView("user/pages/password");
        view.addObject("alert", userService.changePassword(passwordOld, passwordNew, passwordNewConfirm));
        return view;

    }

    @Override
    public ResponseEntity<InputStreamResource> downloadExcel(Integer id, HttpServletRequest request, HttpServletResponse response) {
        HttpHeaders responseHeader = new HttpHeaders();
        try {

            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeader.set("Content-disposition", "attachment; filename="+id+".xlsx");
            byte[] bytes = orderService.exportOrderToExcel(id);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(null, responseHeader, HttpStatus.NOT_FOUND);

        }
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadImages(Integer id, HttpServletRequest request, HttpServletResponse response) {
        HttpHeaders responseHeader = new HttpHeaders();
        try {

            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeader.set("Content-disposition", "attachment; filename="+id+".zip");
            byte[] bytes = orderService.downloadImageFromOrder(id);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(null, responseHeader, HttpStatus.NOT_FOUND);

        }
    }
}
