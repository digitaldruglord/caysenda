package com.nomi.caysenda;

import com.nomi.caysenda.exceptions.cart.AddToCartException;
import com.nomi.caysenda.exceptions.product.ProductException;


import com.nomi.caysenda.exports.*;
import com.nomi.caysenda.exports.exceptions.NotExistsDataException;
import com.nomi.caysenda.extension.services.ExtensionService;
import com.nomi.caysenda.ghn.api.GHN;
import com.nomi.caysenda.lazada.services.LazadaProductService;
import com.nomi.caysenda.lazada.services.LazadaSystemService;
import com.nomi.caysenda.redis.model.RedisProgress;
import com.nomi.caysenda.repositories.CartRepository;
import com.nomi.caysenda.repositories.ProductRepository;
import com.nomi.caysenda.repositories.ProviderRepository;
import com.nomi.caysenda.repositories.TrackingOrderRepository;
import com.nomi.caysenda.services.*;
import com.nomi.caysenda.services.delivery.DeliveryService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
class CaysendaApplicationTests {
	@Autowired
	CartRepository cartRepository;
	@Autowired
	CartService cartService;
	@Autowired
	TrackingOrderRepository trackingOrderRepository;
	@Autowired
	OrderService orderService;
	@Autowired
	ExtensionService extensionService;
	@Autowired
	ProviderRepository providerRepository;
	@Autowired
	Environment env;
	@Autowired
	CategoryService categoryService;
	@Autowired
	LazadaSystemService lazadaSystemService;
	@Autowired
	LazadaProductService lazadaProductService;
	@Autowired
	GHN ghn;
	@Autowired
	DeliveryService deliveryService;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ProgressService progressService;
	@Autowired
	TaskExecutor taskExecutor;
	@Autowired
	EntityManager entityManager;
	@Test
	void cropImage() throws Exception {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("post_comments")
		.registerStoredProcedureParameter(
				1,
				Long.class,
				ParameterMode.IN
		)
		.registerStoredProcedureParameter(
				2,
				Class.class,
				ParameterMode.OUT
		)
		.setParameter(1, 1L);

		query.execute();
		List<Object[]> postComments = query.getResultList();




//		List<Map> results = productRepository.exportSapo(236);
//		ExportExcelResult result = new ExportExcelResult();
//		result.setFileName("TestSapo");
//		result.setSheetName("ngon");
//
//		ExportExcelHeaders mainHeader = new ExportExcelHeaders();
//		mainHeader.getHeaders().add(new ExportExcelHeader("SLUG","Đường dẫn / Alias",0));
//		mainHeader.getHeaders().add(new ExportExcelHeader("NAME","Tên sản phẩm",1));
//		mainHeader.getHeaders().add(new ExportExcelHeader("CONTENT","Nội dung",2));
//		mainHeader.getHeaders().add(new ExportExcelHeader("SUPPLIER","Nhà cung cấp",3));
//		mainHeader.getHeaders().add(new ExportExcelHeader("CATEGORY","Loại",4));
//		mainHeader.getHeaders().add(new ExportExcelHeader("TAGS","Tags",5));
//		mainHeader.getHeaders().add(new ExportExcelHeader("DISPLAY","Hiển thị",6));
//		mainHeader.getHeaders().add(new ExportExcelHeader("ATTRIBUTE1","Thuộc tính 1(Option1 Name));",7));
//		mainHeader.getHeaders().add(new ExportExcelHeader("ATTRIBUTE1_VALUE","Giá trị thuộc tính 1(Option1 Value));",8));
//		mainHeader.getHeaders().add(new ExportExcelHeader("ATTRIBUTE2","Thuộc tính 2(Option2 Name));",9));
//		mainHeader.getHeaders().add(new ExportExcelHeader("ATTRIBUTE2_VALUE","Giá trị thuộc tính 2(Option2 Value));",10));
//		mainHeader.getHeaders().add(new ExportExcelHeader("ATTRIBUTE3","Thuộc tính 3(Option3 Name));",11));
//		mainHeader.getHeaders().add(new ExportExcelHeader("ATTRIBUTE3_VALUE","Giá trị thuộc tính 1(Option3 Value));",12));
//		mainHeader.getHeaders().add(new ExportExcelHeader("SKU","Mã (SKU));",13));
//		mainHeader.getHeaders().add(new ExportExcelHeader("INVENTORY","Quản lý kho",14));
//		mainHeader.getHeaders().add(new ExportExcelHeader("QTY","Số lượng",15));
//		mainHeader.getHeaders().add(new ExportExcelHeader("BUYALL","Cho phép tiếp tục mua khi hết hàng(continue/deny));",16));
//		mainHeader.getHeaders().add(new ExportExcelHeader("FULLFILMENT","Variant Fulfillment Service",17));
//		mainHeader.getHeaders().add(new ExportExcelHeader("PRICE","Giá",18));
//		mainHeader.getHeaders().add(new ExportExcelHeader("PRICE_COMPARE","Giá so sánh",19));
//		mainHeader.getHeaders().add(new ExportExcelHeader("DELIVERY","Yêu cầu vận chuyển",20));
//		mainHeader.getHeaders().add(new ExportExcelHeader("VAT","VAT",21));
//		mainHeader.getHeaders().add(new ExportExcelHeader("BARCODE","Mã vạch(Barcode));",22));
//		mainHeader.getHeaders().add(new ExportExcelHeader("THUMBNAIL","Ảnh đại diện",23));
//		mainHeader.getHeaders().add(new ExportExcelHeader("THUMBNAIL_NOTE","Chú thích ảnh",24));
//		mainHeader.getHeaders().add(new ExportExcelHeader("SEO_TITLE","Thẻ tiêu đề(SEO Title));",25));
//		mainHeader.getHeaders().add(new ExportExcelHeader("SEO_DESCRIPTION","Thẻ mô tả(SEO Description));",26));
//		mainHeader.getHeaders().add(new ExportExcelHeader("WEIGHT","Cân nặng",27));
//		mainHeader.getHeaders().add(new ExportExcelHeader("WEIGHT_UNIT","Đơn vị cân nặng",28));
//		mainHeader.getHeaders().add(new ExportExcelHeader("VARIANT_THUMBNAIL","Ảnh phiên bản",29));
//		mainHeader.getHeaders().add(new ExportExcelHeader("SHORT_DESCRIPTION","Mô tả ngắn",30));
//		result.setMainHeader(mainHeader);
//
//		ExportExcelBody body = new ExportExcelBody(results, results.size());
//		result.setBody(body);
//		taskExecutor.execute(() -> {
//
//		});
//		ExportExcelFileBuilder fileBuilder = new ExportExcelFileBuilder(result,progressService);
//		try {
//			fileBuilder.builder();
//		} catch (NotExistsDataException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		while (true) {
//			RedisProgress redisProgress = progressService.findByCode("EXPORT_EXCEL");
//			if (redisProgress != null) {
//				System.out.println(redisProgress.getProgress());
//				if (!redisProgress.getRunning()) break;
//			}
//
//			Thread.sleep(1000);
//		}

	}

	@Test
	void testQuery() throws AddToCartException, IOException {
		SimpleDateFormat format = new SimpleDateFormat("MMdd");

		format.format(new Date());
	}

	@Test()
	void testExtention() throws IOException, InterruptedException, ProductException {

//        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
//        List<Integer> catIds = categoryEntities.stream().map((categoryEntity -> categoryEntity.getId())).collect(Collectors.toList());
//        List<ExtensionProductEntity> extensionproducts = extensionProductRepository.findAllByShop_Id(6);
//        ExtensionShopEntity extensionShopEntity = extensionShopRepository.findById(6).get();
//       for (ExtensionProductEntity extensionProductEntity:extensionproducts){
//           AdminProductRequest adminProductRequest = new AdminProductRequest();
//           adminProductRequest.setNameZh(extensionProductEntity.getNameZh());
//           adminProductRequest.setSkuZh("ZH"+extensionShopEntity.getSku()+extensionProductEntity.getId());
//           adminProductRequest.setNameVi(extensionProductEntity.getTempName());
//           adminProductRequest.setSlugVi(ConvertStringToUrl.covertStringToURL(extensionProductEntity.getTempName()));
//           adminProductRequest.setSkuVi("VI"+extensionShopEntity.getSku()+extensionProductEntity.getId());
//           adminProductRequest.setThumbnail(extensionProductEntity.getGallery().get(0));
//           adminProductRequest.setStatus(1);
//           adminProductRequest.setContent("");
//           adminProductRequest.setConditiondefault(extensionProductEntity.getCondition1());
//           adminProductRequest.setCondition1(extensionProductEntity.getCondition2());
//           adminProductRequest.setCondition2(extensionProductEntity.getCondition3());
//           adminProductRequest.setCondition3(10000);
//           adminProductRequest.setCondition4(20000);
//           adminProductRequest.setGallery(extensionProductEntity.getGallery());
//           adminProductRequest.setCategoryDefault(3);
//           adminProductRequest.setCategories(catIds);
//           List<AdminProductVariantRequest> variants = new ArrayList<>();
//           List<ExtensionVariantEntity> variantsExtension = variantRepository.findAllByProduct_Id(extensionProductEntity.getId());
//           for (ExtensionVariantEntity extensionVariantEntity:variantsExtension){
//               AdminProductVariantRequest variantRequest = new AdminProductVariantRequest();
//               variantRequest.setNameZh(extensionVariantEntity.getNameZh());
//               variantRequest.setSkuZh("ZH"+extensionShopEntity.getSku()+extensionProductEntity.getId()+"-"+extensionVariantEntity.getId());
//               variantRequest.setNameVi(extensionVariantEntity.getTempName());
//               variantRequest.setSkuVi("VI"+extensionShopEntity.getSku()+extensionProductEntity.getId()+"-"+extensionVariantEntity.getId());
//               variantRequest.setThumbnail(extensionVariantEntity.getImg());
//               variantRequest.setPriceZh(extensionVariantEntity.getPrice());
//               variantRequest.setPrice(extensionVariantEntity.getPriceDefault());
//               variantRequest.setVip1(extensionVariantEntity.getPrice1());
//               variantRequest.setVip2(extensionVariantEntity.getPrice2());
//               variantRequest.setVip3(extensionVariantEntity.getPrice3());
//               variantRequest.setVip4(extensionVariantEntity.getPrice4());
//               variantRequest.setStock(extensionVariantEntity.getStock());
//               variants.add(variantRequest);
//           }
//           adminProductRequest.setVariants(variants);
//           productService.create(adminProductRequest);
//
//       }

//        extensionProductRepository.findByLink("https://detail.1688.com/offer/599074670992.html");
//        List<MenuOption> menus = List.of(
//                new MenuOption("test1","test1",List.of(
//                        new MenuOption("test11","test11",List.of(
//                                new MenuOption("test123","test123"),
//                                new MenuOption("test124","test124")
//                        )),
//                        new MenuOption("test12","test12")
//                )),
//                new MenuOption("test2","test2")
//        );
//        settingService.createMenu(menus,"NAVHEADER");
//       extentionService.generateExcel();
//        for (ExtensionProductEntity productEntity:extensionProductRepository.findAll()){
//            if (productEntity.getGallery().size()<=0){
//                extensionProductRepository.delete(productEntity);
//            }
//        }
	}

	@Test
	void updateProduct() {
//        productRepository.findByCategoryRandForProductDTO(2,PageRequest.of(0,10));
		/** Website info */
		/** Banner TOP*/
//        List<BannerTOP> bannerTOPS = List.of(
//                new BannerTOP("https://electro.madrasthemes.com/wp-content/uploads/2021/03/cameras-resized.png","Catch Big <br><strong>Deals</strong> on the <br>Cameras","#"),
//                new BannerTOP("https://electro.madrasthemes.com/wp-content/uploads/2021/03/cameras-resized.png","Catch Big <br><strong>Deals</strong> on the <br>Cameras","#"),
//                new BannerTOP("https://electro.madrasthemes.com/wp-content/uploads/2021/03/cameras-resized.png","Catch Big <br><strong>Deals</strong> on the <br>Cameras","#"),
//                new BannerTOP("https://electro.madrasthemes.com/wp-content/uploads/2021/03/cameras-resized.png","Catch Big <br><strong>Deals</strong> on the <br>Cameras","#")
//        );
//        optionService.update(bannerTOPS,OptionKeys.BANNERTOP);
		/** menu option */
//        List<MenuOption> menuOptions = new ArrayList<>();
//        menuOptions.add(new MenuOption("Sản phẩm","#",List.of(
//                new MenuOption("Phụ kiện tiểu cảnh","#",List.of(
//                        new MenuOption("Chậu trồng cây","#"),
//                        new MenuOption("Chậu trồng cây 1","#"),
//                        new MenuOption("Chậu trồng cây 2","#")
//                )),
//                new MenuOption("Cây sen đá","#")
//        )));
//        menuOptions.add(new MenuOption("Hướng dẫn thanh toán","#thanh-toan"));
//        menuOptions.add(new MenuOption("Báo giá bán sỉ","#ban-si"));
//        optionService.update(menuOptions,OptionKeys.NAVTOP);
		/** slide option*/
//        List<SlideHome> slideHomes = new ArrayList<>();
//        slideHomes.add(new SlideHome("Cây đẹp vãi ","Cây này đẹp mà rẻ vl ",Long.valueOf(10000),"#","/resources/upload/Cay-Rung-CX47-1.jpg"));
//        slideHomes.add(new SlideHome("Cây xấu vc","Cây này đẹp mà rẻ vl",Long.valueOf(10000),"#","/resources/upload/Cay-Rung-CX47-1.jpg"));
//        optionService.update(slideHomes, OptionKeys.SLIDEHOME);
	}

	@Test
	void findUser() {
//        ProductEntity productEntity = new ProductEntity();
//        productEntity.setNameZh("asdaf12");
//        productEntity.setSkuZh("SK21UZh");
//        productEntity.setNameVi("Phụ kiện tiểu cảnh");
//        productEntity.setSkuVi("DU1201");
//        productEntity.setSlugVi("phu-ki123en-tieu-canh");
//        productEntity.setThumbnail("/resources/upload/Ca-Hoi-Do-Dia-Trung-Hai-CA8-1.jpg");
//        productEntity.setStatus(1);
//        productEntity.setContent("Phuj kien tieu canh ngon vl luon nay");
//        productEntity.setTrash(false);
//        productEntity.setConditiondefault(1);
//        productEntity.setCondition1(10);
//        productEntity.setCondition2(20);
//        productEntity.setCondition3(50);
//        productEntity.setCondition4(100);
//        productEntity.setCategoryDefault(8);
//        productEntity.setGallery(List.of("/resources/upload/Ca-Hoi-Do-Dia-Trung-Hai-CA8-1.jpg"));
//        List<CategoryEntity> categories = categoryRepository.findAll();
//        productEntity.setCategories(categories);
//        List<VariantEntity> variants = new ArrayList<>();
//        VariantEntity variantEntity = new VariantEntity();
//        variantEntity.setNameZh("ZhNa123me");
//        variantEntity.setSkuZh("ZhS123KU");
//        variantEntity.setNameVi("ViName");
//        variantEntity.setSkuVi("ViS1ku");
//        variantEntity.setThumbnail(productEntity.getThumbnail());
//        variantEntity.setWeight(Float.valueOf(10));
//        variantEntity.setWidth(Float.valueOf(10));
//        variantEntity.setHeight(Float.valueOf(10));
//        variantEntity.setLength(Float.valueOf(10));
//        variantEntity.setStock(100);
//        variantEntity.setPrice(Long.valueOf(50000));
//        variantEntity.setVip1(Long.valueOf(40000));
//        variantEntity.setVip2(Long.valueOf(30000));
//        variantEntity.setVip3(Long.valueOf(20000));
//        variantEntity.setVip4(Long.valueOf(10000));
//        variantEntity.setProductEntity(productEntity);
//        variants.add(variantEntity);
//        productEntity.setVariants(variants);
//        productRepository.save(productEntity);
	}

	@Test
	void createuser() {
//        RoleEntity roleEntity = roleRepository.findByCode("ROLE_ADMIN");
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername("lakdak4");
//        userEntity.setEmail("lakdak4@gmail.com");
//        userEntity.setStatus(1);
//        userEntity.setPhonenumber("0942492445");
//        userEntity.setPassword(passwordEncoder.encode("123456"));
//        userEntity.setRoles(List.of(roleEntity));
//        try {
//            userRepository.saveAndFlush(userEntity);
//        }catch ( DataIntegrityViolationException e){
//
//        }
	}

	@Test
	void createRole() {
//        RoleEntity roleEntity = new RoleEntity();
//        roleEntity.setCode("ROLE_ADMIN");
//        roleEntity.setName("Quản trị viên");
//        RoleEntity roleEmployee = new RoleEntity();
//        roleEmployee.setCode("ROLE_EMPLOYEE");
//        roleEmployee.setName("Nhân viên");
//        RoleEntity roleCustomer = new RoleEntity();
//        roleCustomer.setCode("ROLE_CUSTOMER");
//        roleCustomer.setName("Khách hàng");
//        List<RoleEntity> entities = new ArrayList<>();
//        entities.add(roleEntity);
//        entities.add(roleEmployee);
//        entities.add(roleCustomer);
//        roleRepository.saveAll(entities);
	}

}
