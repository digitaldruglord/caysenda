package com.nomi.caysenda.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomi.caysenda.api.admin.model.order.request.*;
import com.nomi.caysenda.controller.requests.order.BuynowRequest;
import com.nomi.caysenda.controller.responses.payment.PaymentResponse;
import com.nomi.caysenda.dto.*;
import com.nomi.caysenda.dto.cart.CartCategoryDTO;
import com.nomi.caysenda.dto.cart.CartDTO;
import com.nomi.caysenda.dto.cart.CartProductDTO;
import com.nomi.caysenda.dto.cart.CartVariantDTO;
import com.nomi.caysenda.entity.*;
import com.nomi.caysenda.extension.model.request.UpdateTrackingRequest;
import com.nomi.caysenda.ghn.service.GHNService;
import com.nomi.caysenda.lazada.repositories.LazadaOrderRepository;
import com.nomi.caysenda.options.model.PriceOption;
import com.nomi.caysenda.options.model.ShipSetting;
import com.nomi.caysenda.repositories.*;
import com.nomi.caysenda.security.CustomUserDetail;
import com.nomi.caysenda.security.SecurityUtils;
import com.nomi.caysenda.services.*;
import com.nomi.caysenda.utils.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.threeten.bp.ZoneOffset;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired AddressService addressService;
	@Autowired TemplateEngine templateEngine;
	@Autowired AddressProvinceRepository provinceRepository;
	@Autowired AddressDictrictRepository dictrictRepository;
	@Autowired AddressWardRepository wardRepository;
	@Autowired AddressRedisService addressRedisService;
	@Autowired CartService cartService;
	@Autowired OrderRepository orderRepository;
	@Autowired OrderDetailtRepository detailtRepository;
	@Autowired UserRepository userRepository;
	@Autowired MyMailSender mailSender;
	@Autowired GiaoHangTietKiemService giaoHangTietKiemService;
	@Autowired ProductService productService;
	@Autowired Environment env;
	@Autowired ProviderRepository providerRepository;
	@Autowired SettingService settingService;
	@Autowired VariantRepository variantRepository;
	@Autowired CategoryService categoryService;
	@Autowired TrackingOrderRepository trackingOrderRepository;
	@Autowired OptionService optionService;
	@Autowired RestTemplate restTemplate;
	@Autowired GHNService ghnService;
	@Autowired LazadaOrderRepository lazadaOrderRepository;

	@Override
	public List<PrintOrderDTO> getDataPrint(List<Integer> ids) {
		List<PrintOrderDTO> data = new ArrayList<>();
		List<OrderEntity> orders = orderRepository.findAllById(ids);
		orders.forEach(orderEntity -> {
			PrintOrderDTO printOrderDTO = new PrintOrderDTO();
			printOrderDTO.setOrderId(orderEntity.getId());
			printOrderDTO.setFullName(orderEntity.getBillingFullName());
			printOrderDTO.setPhoneNumber(orderEntity.getBillingPhoneNumber());
			printOrderDTO.setEmail(orderEntity.getBillingEmail());
			printOrderDTO.setFullAddress(getFullAddress(orderEntity.getBillingAddress(), orderEntity.getBillingCity(), orderEntity.getBillingDistrict(), orderEntity.getBillingWards()));
			printOrderDTO.setNote(orderEntity.getNote());
			printOrderDTO.setCreateDate(orderEntity.getCreateDate());
			printOrderDTO.setStatus(getStatus().get(orderEntity.getStatus()));
			printOrderDTO.setMethod(getMethod().get(orderEntity.getMethod()));
			printOrderDTO.setShip(orderEntity.getShip());
			printOrderDTO.setProductAmount(orderEntity.getProductAmount());
			printOrderDTO.setOrderAmount(orderEntity.getOrderAmount());
			printOrderDTO.setRefunded(orderEntity.getRefunded());
			printOrderDTO.setDiscountType(orderEntity.getDiscountType());
			printOrderDTO.setDiscountValue(orderEntity.getDiscountValue());
			printOrderDTO.setCod(orderEntity.getOrderAmount() - orderEntity.getPaid());
			printOrderDTO.setOrderCode(orderEntity.getOrderCode() != null ? orderEntity.getOrderCode() : String.valueOf(orderEntity.getId()));
			printOrderDTO.setPaid(orderEntity.getPaid());

			List<PrintOrderDetailDTO> detailts = detailtRepository.findAllByOrderId(orderEntity.getId());
			List<Integer> list = new ArrayList<>();

			detailts.stream().sorted((o1, o2) -> Integer.parseInt(o2.getTopFlag()) - Integer.parseInt(o1.getTopFlag())).forEach(printOrderDetailDTO -> {
				if (!list.contains(printOrderDetailDTO.getProductId())) {
					list.add(printOrderDetailDTO.getProductId());
				}
			});

			Map<Integer, List<PrintOrderDetailDTO>> groups = detailts.stream().filter(printOrderDetailDTO -> printOrderDetailDTO.getProductId() != null).collect(groupingBy(PrintOrderDetailDTO::getProductId,toList()));
			List<PrintOrderDetailDTO> newList = new ArrayList<>();
			list.forEach(integer -> {
				if (integer != null) {
					newList.addAll(groups.get(integer));
				}
			});

			newList.addAll(detailts.stream().filter(printOrderDetailDTO -> printOrderDetailDTO.getProductId() == null).collect(toList()));
			printOrderDTO.setDetailts(newList);
			data.add(printOrderDTO);
		});
		return data;
	}

	@Override
	public OrderEntity findById(Integer id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Override
	public OrderEntity merge(Integer from, Integer to) {
		OrderEntity				fromOrder       = findById(from);
		List<OrderDetailtEntity> detailtFrom    = findAllDetailtByOrderId(from);
		OrderEntity              toOrder        = findById(to);
		List<OrderDetailtEntity> detailtTo      = findAllDetailtByOrderId(to);
		// tracking
		List<TrackingOrderEntity> tracks		= getTracking(from);

		tracks.forEach(trackingOrderEntity -> {
			trackingOrderEntity.setOrderTracking(toOrder);
			trackingOrderRepository.save(trackingOrderEntity);
		});

		detailtFrom.forEach(orderDetailtEntity -> {
			List<OrderDetailtEntity> list = detailtTo.stream().filter(orderDetailtEntity1 -> orderDetailtEntity1.getProductId() != null && orderDetailtEntity.getVariantId() != null).filter(orderDetailtEntity1 -> orderDetailtEntity1.getProductId().equals(orderDetailtEntity.getProductId()) && orderDetailtEntity1.getVariantId().equals(orderDetailtEntity.getVariantId())).collect(toList());

			if (list.size() > 0) {
				list.get(0).setQuantity(list.get(0).getQuantity() + orderDetailtEntity.getQuantity());
				detailtRepository.delete(orderDetailtEntity);
				detailtRepository.save(list.get(0));
			} else {
				orderDetailtEntity.setOrderEntity(toOrder);
				detailtTo.add(orderDetailtEntity);
				detailtRepository.save(orderDetailtEntity);
			}
		});

		toOrder.setProductAmount(detailtTo.stream().mapToLong(orderDetailtEntity -> orderDetailtEntity.getQuantity() * orderDetailtEntity.getPrice()).sum());
		toOrder.setShip(toOrder.getShip() + fromOrder.getShip());
		toOrder.setRefunded(toOrder.getRefunded() + fromOrder.getRefunded());
		toOrder.setIncurredCost(toOrder.getIncurredCost() + fromOrder.getIncurredCost());
		toOrder.setPaid(toOrder.getPaid() + fromOrder.getPaid());
		toOrder.setDiscountValue(toOrder.getDiscountValue() + fromOrder.getDiscountValue());
		toOrder.setOrderAmount(toOrder.getProductAmount() + toOrder.getShip() - toOrder.getDiscountValue().longValue());

		orderRepository.delete(fromOrder);

		return orderRepository.save(toOrder);
	}

	@Override
	public Page<OrderAdminDTO> findAllForOrderAdminDTO(String status, Boolean trash, Integer host, String keyword, String from, String to, Pageable pageable) {
		status = status != null && !status.equals("") ? status : null;
		host = host != null && host.equals("") ? host : null;
		keyword = keyword != null && !keyword.equals("") ? keyword : null;
		from = from != null && !from.equals("") ? from : null;
		to = to != null && !to.equals("") ? to : null;
		Date fromDate = null;
		Date toDate = null;
		if (from != null && !from.equals("") && to != null && !to.equals("")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			LocalDateTime dateTimeFrom = LocalDateTime.parse(from + " 00:00", formatter);
			LocalDateTime dateTimeTo = LocalDateTime.parse(to + " 00:00", formatter);
			fromDate = Date.from(dateTimeFrom.with(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
			toDate = Date.from(dateTimeTo.with(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
		}
		Page<OrderAdminDTO> data = orderRepository.findAllCriteria(status, trash, host, keyword, fromDate, toDate, pageable);
		data.getContent().forEach(orderAdminDTO -> {
			orderAdminDTO.setFullAddress(getFullAddress(orderAdminDTO.getBillingAddress(), orderAdminDTO.getBillingCity(), orderAdminDTO.getBillingDistrict(), orderAdminDTO.getBillingWards()));
			Long cost = orderAdminDTO.getCost() != null ? orderAdminDTO.getCost() : 0;
			Long profit = orderAdminDTO.getProductAmount() - cost - orderAdminDTO.getIncurredCost();
			orderAdminDTO.setProfit(profit);
			orderAdminDTO.setCod(orderAdminDTO.getOrderAmount() - orderAdminDTO.getPaid());

			Double profitMargin = Double.valueOf(0);
			if (profit > 0) {
				profitMargin = profit.doubleValue() / orderAdminDTO.getProductAmount();
			}
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			orderAdminDTO.setProfitMargin(Float.valueOf(decimalFormat.format(profitMargin)));
			orderAdminDTO.setTracking(trackingOrderRepository.findAllByOrderTracking_Id(orderAdminDTO.getId()));
			orderAdminDTO.setGhn(ghnService.findAllByOrderId(orderAdminDTO.getId()));
			orderAdminDTO.setLazadaOrder(lazadaOrderRepository.findByOrderId(orderAdminDTO.getId()));
		});
		return data;
	}


	@Override
	public Page<OrderAdminDTO> findAllForOrderAdminDTO(String keyword, Boolean trash, Pageable pageable) {
		Page<OrderAdminDTO> list = orderRepository.findAllForOrderAdminDTO(keyword != null ? "%" + keyword + "%" : "%%", trash, pageable);
		list.getContent().forEach(orderAdminDTO -> {
			orderAdminDTO.setFullAddress(getFullAddress(orderAdminDTO.getBillingAddress(), orderAdminDTO.getBillingCity(), orderAdminDTO.getBillingDistrict(), orderAdminDTO.getBillingWards()));
			Long profit = orderAdminDTO.getProductAmount() - orderAdminDTO.getCost() - orderAdminDTO.getIncurredCost();
			orderAdminDTO.setProfit(profit);
			orderAdminDTO.setCod(orderAdminDTO.getOrderAmount() - orderAdminDTO.getPaid());
			Double profitMargin = Double.valueOf(0);
			if (profit > 0) {
				profitMargin = profit.doubleValue() / orderAdminDTO.getProductAmount();
			}
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			orderAdminDTO.setProfitMargin(Float.valueOf(decimalFormat.format(profitMargin)));
		});
		return list;
	}

	@Override
	public Page<OrderAdminDTO> findAllForOrderAdminDTOByStatus(String keyword, Boolean trash, String status, Pageable pageable) {
		Page<OrderAdminDTO> list = orderRepository.findAllForOrderAdminDTOByStatus(keyword != null ? "%" + keyword + "%" : "%%", trash, status, pageable);
		list.getContent().forEach(orderAdminDTO -> {
			orderAdminDTO.setFullAddress(getFullAddress(orderAdminDTO.getBillingAddress(), orderAdminDTO.getBillingCity(), orderAdminDTO.getBillingDistrict(), orderAdminDTO.getBillingWards()));
			Long profit = orderAdminDTO.getProductAmount() - orderAdminDTO.getCost() - orderAdminDTO.getIncurredCost();
			orderAdminDTO.setProfit(profit);
			orderAdminDTO.setCod(orderAdminDTO.getOrderAmount() - orderAdminDTO.getPaid());
			Double profitMargin = Double.valueOf(0);
			if (profit > 0) {
				profitMargin = profit.doubleValue() / orderAdminDTO.getProductAmount();
			}
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			orderAdminDTO.setProfitMargin(Float.valueOf(decimalFormat.format(profitMargin)));
		});
		return list;
	}

	@Override
	public Page<OrderAdminDTO> findAllForOrderAdminDTO(String keyword, Boolean trash, Integer host, Pageable pageable) {
		Page<OrderAdminDTO> list = orderRepository.findAllForOrderAdminDTO(keyword != null ? "%" + keyword + "%" : "%%", trash, host, pageable);
		list.getContent().forEach(orderAdminDTO -> {
			orderAdminDTO.setFullAddress(getFullAddress(orderAdminDTO.getBillingAddress(), orderAdminDTO.getBillingCity(), orderAdminDTO.getBillingDistrict(), orderAdminDTO.getBillingWards()));
			Long profit = orderAdminDTO.getProductAmount() - orderAdminDTO.getCost() - orderAdminDTO.getIncurredCost();
			orderAdminDTO.setProfit(profit);
			orderAdminDTO.setCod(orderAdminDTO.getOrderAmount() - orderAdminDTO.getPaid());
			Double profitMargin = Double.valueOf(0);
			if (profit > 0) {
				profitMargin = profit.doubleValue() / orderAdminDTO.getProductAmount();
			}
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			orderAdminDTO.setProfitMargin(Float.valueOf(decimalFormat.format(profitMargin)));
		});
		return list;
	}

	@Override
	public Page<OrderAdminDTO> findAllForOrderAdminDTOByStatus(String keyword, Boolean trash, String status, Integer host, Pageable pageable) {
		Page<OrderAdminDTO> list = orderRepository.findAllForOrderAdminDTOByStatus(keyword != null ? "%" + keyword + "%" : "%%", trash, status, host, pageable);
		list.getContent().forEach(orderAdminDTO -> {
			orderAdminDTO.setFullAddress(getFullAddress(orderAdminDTO.getBillingAddress(), orderAdminDTO.getBillingCity(), orderAdminDTO.getBillingDistrict(), orderAdminDTO.getBillingWards()));
			Long profit = orderAdminDTO.getProductAmount() - orderAdminDTO.getCost() - orderAdminDTO.getIncurredCost();
			orderAdminDTO.setProfit(profit);
			orderAdminDTO.setCod(orderAdminDTO.getOrderAmount() - orderAdminDTO.getPaid());
			Double profitMargin = Double.valueOf(0);
			if (profit > 0) {
				profitMargin = profit.doubleValue() / orderAdminDTO.getProductAmount();
			}
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			orderAdminDTO.setProfitMargin(Float.valueOf(decimalFormat.format(profitMargin)));
		});
		return list;
	}


	@Override
	public Page<OrderDTO> findAllForOrderDTOByUserId(Integer userId, Pageable pageable) {
		Page<OrderDTO> orders = orderRepository.findAllForOrderDTOByUserId(userId, pageable);
		orders.getContent().forEach((orderDTO -> {
			orderDTO.setStatus(getStatus().get(orderDTO.getStatus()));
			orderDTO.setGhnOrderEntities(ghnService.findAllByOrderId(orderDTO.getId()));
		}));
		return orders;
	}

	@Override
	public PaymentResponse confirm(String method, String note) {
		CustomUserDetail userDetail = SecurityUtils.getPrincipal();
		if (!cartService.checkPayment()) {
			return new PaymentResponse(false, "Vui lòng chọn sản phẩm trước khi thanh toán", "", null);
		}
		AddressDTO addressDTO = null;
		if (userDetail != null) {
			addressDTO = addressService.findAddressDefault(userDetail.getUserId());
		} else {
			addressDTO = addressRedisService.findAddressDefault();
		}
		if (addressDTO == null) {
			Context context = new Context();
			context.setVariable("provinces", AddressUtils.convertEntityToDTO(provinceRepository.findAll(), null, null));
			context.setVariable("address", new AddressDTO());
			context.setVariable("ref", "payment");
			return new PaymentResponse(false, "Chưa có địa chỉ", "address-not-found", templateEngine.process("fragment/address/modal-create", context));
		}
		OrderEntity orderEntity = save(method, note);
		mailSender.payment(orderEntity);
		if (userDetail != null) {
			return new PaymentResponse(true, "/tai-khoan/hoa-don/" + orderEntity.getId(), "payment-acccess");
		} else {
			return new PaymentResponse(true, "/don-hang/" + orderEntity.getId(), "payment-acccess");
		}


	}

	@Override
	public OrderEntity save(OrderEntity orderEntity) {

		return orderRepository.save(orderEntity);
	}

	@Override
	public OrderEntity save(AdminOrderRequest orderRequest) {
		OrderEntity orderEntity = new OrderEntity();
		CustomUserDetail userDetail = SecurityUtils.getPrincipal();
		String statusOld = null;
		if (orderRequest.getId() != null) {
			orderEntity = orderRepository.findById(orderRequest.getId()).orElse(new OrderEntity());
			statusOld = orderEntity.getStatus();
		} else {
			if (userDetail!=null){
				UserEntity userEntity = userRepository.findById(userDetail.getUserId()).orElse(null);
				orderEntity.setUserOrder(userEntity);
			}
		}
		ProviderEntity providerEntity = providerRepository.findByHost(env.getProperty("spring.domain"));
		/** Order */
		orderEntity.setBillingFullName(orderRequest.getBillingFullName());
		orderEntity.setBillingPhoneNumber(orderRequest.getBillingPhoneNumber());
		orderEntity.setBillingEmail(orderRequest.getBillingEmail());
		orderEntity.setOrderComment("");
		orderEntity.setBillingAddress(orderRequest.getBillingAddress());
		orderEntity.setBillingCity(orderRequest.getBillingCity());
		orderEntity.setBillingDistrict(orderRequest.getBillingDistrict());
		orderEntity.setBillingWards(orderRequest.getBillingWards());
		orderEntity.setMethod(orderRequest.getMethod() != null ? orderRequest.getMethod() : "COD");
		orderEntity.setNote(orderRequest.getNote());
		orderEntity.setShip(orderRequest.getShip() != null ? orderRequest.getShip() : 0);
		orderEntity.setPaid(orderRequest.getPaid() != null ? orderRequest.getPaid() : 0);
		orderEntity.setProductAmount(orderRequest.getDetailts().stream().mapToLong(adminOrderDetailt -> adminOrderDetailt.getQuantity() * adminOrderDetailt.getPrice()).sum());
		orderEntity.setStatus(orderRequest.getStatus() != null ? orderRequest.getStatus() : "processing");
		orderEntity.setRefunded(orderRequest.getRefunded() != null ? orderRequest.getRefunded() : 0);
		orderEntity.setIncurredCost(orderRequest.getIncurredCost() != null ? orderRequest.getIncurredCost() : 0);
		orderEntity.setCost(getCost(orderRequest));
		Double discount = Double.valueOf(0);
		if (orderRequest.getDiscountType().equals("PERCENT")) {
			discount = (orderEntity.getProductAmount() * orderRequest.getDiscountValue()) / 100;
		} else {
			discount = orderRequest.getDiscountValue();
		}
		orderEntity.setOrderAmount(orderEntity.getProductAmount() + orderEntity.getShip() - discount.longValue());
		orderEntity.setTrash(false);
		orderEntity.setDiscountType(orderRequest.getDiscountType() != null ? orderRequest.getDiscountType() : "PERCENT");
		orderEntity.setDiscountValue(orderRequest.getDiscountValue() != null ? orderRequest.getDiscountValue() : 0);
		orderEntity.setProviderOrder(List.of(providerEntity));
		orderEntity = orderRepository.save(orderEntity);
		if (orderRequest.getId() == null) {
			LocalDateTime localDateTime = LocalDateTime.now();
			StringBuilder codeBuilder = new StringBuilder();
			codeBuilder.append(localDateTime.getMonthValue() < 10 ? "0" + localDateTime.getMonthValue() : localDateTime.getMonthValue());
			codeBuilder.append(localDateTime.getDayOfMonth() < 10 ? "0" + localDateTime.getDayOfMonth() : localDateTime.getDayOfMonth());
			codeBuilder.append(orderEntity.getId());
			orderEntity.setOrderCode(codeBuilder.toString());
			orderRepository.save(orderEntity);
		}
		/** Check delete variant */
		if (orderEntity.getId() != null) {
			checkDeleteVariant(orderRequest.getDetailts(), detailtRepository.findAllByOrderEntity_Id(orderEntity.getId()));
		}
		List<OrderDetailtEntity> detailts = new ArrayList<>();

		for (AdminOrderDetailt adminOrderDetailt : orderRequest.getDetailts()) {
			ProductEntity productEntity = adminOrderDetailt.getProductId() != null ? productService.findById(adminOrderDetailt.getProductId()) : null;
			VariantEntity variantEntity = adminOrderDetailt.getVariantId() != null ? variantRepository.findById(adminOrderDetailt.getVariantId()).orElse(null) : null;
			OrderDetailtEntity detailtEntity = new OrderDetailtEntity();
			if (adminOrderDetailt.getId() != null) {
				detailtEntity = detailtRepository.findById(adminOrderDetailt.getId()).orElse(new OrderDetailtEntity());
			} else {
				detailtEntity.setOrderEntity(orderEntity);
				if (productEntity != null && variantEntity != null) {
					/** group */
					GroupVariantEntity groupVariantEntity = ProductUtils.getGroupByGroupSku(productEntity, variantEntity.getParent());
					CategoryEntity categoryEntity = categoryService.findById(productEntity.getCategoryDefault()).orElse(null);

					if (groupVariantEntity != null) {
						detailtEntity.setGroupName(groupVariantEntity.getName());
						detailtEntity.setGroupZhName(groupVariantEntity.getZhName());
						detailtEntity.setGroupSku(groupVariantEntity.getSkuGroup());
					}
					/** end group */
					detailtEntity.setUnit(productEntity.getUnit());
					detailtEntity.setLinkZh(productEntity.getLink());
					if (categoryEntity!=null){
						detailtEntity.setSlug("/"+categoryEntity.getSlug()+"/"+productEntity.getSlugVi());
					}

				}

			}
			if (productEntity != null && variantEntity != null) {
				detailtEntity.setNameZh(productEntity.getNameZh());
				detailtEntity.setProductThumbnail(productEntity.getThumbnail());
				detailtEntity.setVariantThumbnail(variantEntity.getThumbnail());
				detailtEntity.setWeight(variantEntity.getWeight() != null ? variantEntity.getWeight() : 0);
				detailtEntity.setSku(variantEntity.getSkuVi());
				detailtEntity.setVariantNameZh(variantEntity.getNameZh());
			} else {
				detailtEntity.setWeight(Float.valueOf(2));
			}

			detailtEntity.setQuantity(adminOrderDetailt.getQuantity());
			detailtEntity.setCost(adminOrderDetailt.getCost());
			detailtEntity.setName(adminOrderDetailt.getName());
			detailtEntity.setVariantName(adminOrderDetailt.getVariantName());
			detailtEntity.setOwe(adminOrderDetailt.getOwe());
			detailtEntity.setVariantId(adminOrderDetailt.getVariantId());
			detailtEntity.setProductId(adminOrderDetailt.getProductId());
			detailtEntity.setPrice(adminOrderDetailt.getPrice());

			if (productEntity != null && variantEntity != null) {
				detailtEntity.setCategoryId(productEntity.getCategoryDefault());
				detailtEntity.setNameZh(productEntity.getNameZh());
				detailtEntity.setVariantNameZh(variantEntity.getNameZh());
				GroupVariantEntity groupVariantEntity = ProductUtils.getGroupByGroupSku(productEntity, variantEntity.getParent());
				if (groupVariantEntity != null) {
					detailtEntity.setGroupName(groupVariantEntity.getName());
					detailtEntity.setGroupZhName(groupVariantEntity.getZhName());
					detailtEntity.setGroupSku(groupVariantEntity.getSkuGroup());
				}

			}

			if (statusOld != null) {
				onChangeStock(orderEntity.getStatus(), statusOld, adminOrderDetailt.getVariantId(), adminOrderDetailt.getQuantity());
			} else {
				onChangeStock(orderEntity.getStatus(), adminOrderDetailt.getVariantId(), adminOrderDetailt.getQuantity());
			}
			detailts.add(detailtRepository.save(detailtEntity));
		}

		return orderEntity;
	}

	private Long getCost(AdminOrderRequest orderRequest) {
		if (orderRequest.getCost() != null) {
			return orderRequest.getCost();
		} else {
			AtomicReference<Long> cost = new AtomicReference<>(Long.valueOf(0));
			orderRequest.getDetailts().forEach(adminOrderDetailt -> {
				VariantEntity variantEntity = variantRepository.findById(adminOrderDetailt.getVariantId()).orElse(null);
				if (variantEntity != null && variantEntity.getCost() != null) {
					cost.updateAndGet(v -> v + variantEntity.getCost());
				}
			});
			return cost.get();
		}

	}

	void checkDeleteVariant(List<AdminOrderDetailt> adminDetailt, List<OrderDetailtEntity> entityDetailt) {
		List<Integer> ids = new ArrayList<>();
		for (OrderDetailtEntity detailtEntity : entityDetailt) {
			Boolean check = true;
			for (AdminOrderDetailt orderDetailt : adminDetailt) {
				if (orderDetailt.getId() != null && detailtEntity.getId().equals(orderDetailt.getId())) {
					check = false;
				}
			}
			if (check) ids.add(detailtEntity.getId());
		}
		if (ids.size() > 0) {
			detailtRepository.deleteAll(detailtRepository.findAllById(ids));
		}

	}

	@Override
	public OrderEntity save(String method, String note) {
		OrderEntity orderEntity = new OrderEntity();
		ProviderEntity providerEntity = providerRepository.findByHost(env.getProperty("spring.domain"));
		CustomUserDetail userDetail = SecurityUtils.getPrincipal();
		CartDTO cartDTO = null;
		AddressDTO addressDTO = null;
		PriceOption priceOption = settingService.getPriceSetting();
		if (userDetail != null) {
			addressDTO = addressService.findAddressDefault(userDetail.getUserId());
			UserEntity userEntity = userRepository.findById(userDetail.getUserId()).orElse(null);
			orderEntity.setUserOrder(userEntity);
		} else {
			addressDTO = addressRedisService.findAddressDefault();
		}
		cartDTO = cartService.getCart();
		/** Order */

		orderEntity.setBillingFullName(addressDTO.getFullname());
		orderEntity.setBillingPhoneNumber(addressDTO.getPhoneNumber());
		orderEntity.setBillingEmail(addressDTO.getEmail());
		orderEntity.setOrderComment("");
		orderEntity.setBillingAddress(addressDTO.getAddress());
		orderEntity.setBillingCity(addressDTO.getProvince());
		orderEntity.setBillingDistrict(addressDTO.getDictrict());
		orderEntity.setBillingWards(addressDTO.getWard());
		orderEntity.setMethod(method);
		orderEntity.setNote(note);
		orderEntity.setShip(fee(
				cartDTO.getCategories().stream().mapToDouble(cartCategoryDTO ->
						cartCategoryDTO.getProducts().stream().mapToDouble(productDTO ->
								productDTO.getWeight()).sum()).sum(),
				orderEntity.getBillingAddress(),
				orderEntity.getBillingWards(),
				orderEntity.getBillingDistrict()
				, orderEntity.getBillingCity()));
		orderEntity.setPaid(Long.valueOf(0));
		orderEntity.setProductAmount(cartDTO.getCategories().stream().mapToLong(cartCategoryDTO -> cartCategoryDTO.getProducts().stream().mapToLong(productDTO -> productDTO.getAmountActive()).sum()).sum());
		orderEntity.setStatus("processing");
		orderEntity.setRefunded(Long.valueOf(0));
		orderEntity.setIncurredCost(Long.valueOf(0));
		orderEntity.setOrderAmount(orderEntity.getProductAmount() + orderEntity.getShip() - orderEntity.getIncurredCost());
		orderEntity.setTrash(false);
		orderEntity.setDiscountType("PERCENT");
		orderEntity.setDiscountValue(Double.valueOf(0));
		orderEntity.setCost(getCost(cartDTO));
		orderEntity.setProviderOrder(List.of(providerEntity));
		orderEntity = orderRepository.save(orderEntity);
		StringBuilder codeBuilder = new StringBuilder();
		LocalDateTime localDateTime = LocalDateTime.now();
		codeBuilder.append(localDateTime.getMonthValue() < 10 ? "0" + localDateTime.getMonthValue() : localDateTime.getMonthValue());
		codeBuilder.append(localDateTime.getDayOfMonth() < 10 ? "0" + localDateTime.getDayOfMonth() : localDateTime.getDayOfMonth());
		codeBuilder.append(orderEntity.getId());
		orderEntity.setOrderCode(codeBuilder.toString());
		orderRepository.save(orderEntity);
		/** detailts */
		List<OrderDetailtEntity> detailts = new ArrayList<>();
		for (CartCategoryDTO cartCategoryDTO : cartDTO.getCategories()) {
			for (CartProductDTO cartProductDTO : cartCategoryDTO.getProducts()) {
				for (CartVariantDTO cartVariantDTO : cartProductDTO.getVariants()) {
					if (cartVariantDTO.getActive() && cartVariantDTO.getQuantity() > 0) {
						OrderDetailtEntity detailtEntity = new OrderDetailtEntity();
						detailtEntity.setQuantity(cartVariantDTO.getQuantity());
						detailtEntity.setCost(cartVariantDTO.getCost() != null ? cartVariantDTO.getCost() : 0);
						detailtEntity.setName(cartProductDTO.getName());
						detailtEntity.setVariantName(cartVariantDTO.getName());
						detailtEntity.setProductThumbnail(cartProductDTO.getThumbnail());
						detailtEntity.setVariantThumbnail(cartVariantDTO.getThumbnail());
						detailtEntity.setOrderEntity(orderEntity);
						detailtEntity.setOwe(cartVariantDTO.getQuantity());
						detailtEntity.setVariantId(cartVariantDTO.getVariantId());
						detailtEntity.setProductId(cartProductDTO.getProductId());
						detailtEntity.setSlug("/" + cartCategoryDTO.getSlug() + "/" + cartProductDTO.getSlug());
						detailtEntity.setLinkZh(cartProductDTO.getLinkZh());
						detailtEntity.setWeight(cartVariantDTO.getWeight() != null ? cartVariantDTO.getWeight() : 0);
						detailtEntity.setSku(cartVariantDTO.getSku());
						detailtEntity.setNameZh(cartProductDTO.getNameZh());
						detailtEntity.setVariantNameZh(cartVariantDTO.getNameZh());
						detailtEntity.setGroupName(cartVariantDTO.getGroupName());
						detailtEntity.setGroupZhName(cartVariantDTO.getGroupZhName());
						detailtEntity.setGroupSku(cartVariantDTO.getGroupSku());
						detailtEntity.setCategoryId(cartProductDTO.getCategoryId());
						detailtEntity.setUnit(cartProductDTO.getUnit());
						detailts.add(detailtEntity);
						if (cartProductDTO.getRetail()) {
							detailtEntity.setPrice(ProductUtils.getTypePrice(priceOption.getPriceDefault(), cartVariantDTO.getVip1(), cartVariantDTO.getVip2(), cartVariantDTO.getVip3(), cartVariantDTO.getVip4(), cartVariantDTO.getPriceDefault()));
						} else {
							detailtEntity.setPrice(
									ProductUtils.getPrice(
											cartProductDTO.getRange(),
											cartVariantDTO.getQuantity(),
											ProductUtils.getTypePrice(cartProductDTO, priceOption.getPrice1()),
											ProductUtils.getTypePrice(cartProductDTO, priceOption.getPrice2()),
											ProductUtils.getTypePrice(cartProductDTO, priceOption.getPrice3())
									).getPrice());
						}
						onChangeStock(orderEntity.getStatus(), cartVariantDTO.getVariantId(), cartVariantDTO.getQuantity());
						detailts.add(detailtRepository.save(detailtEntity));
					}

				}
			}
		}

		orderEntity.setDetailts(detailts);
		cartService.removeAllActive();
		return orderEntity;
	}

	private Long getCost(CartDTO cartDTO) {
		AtomicReference<Long> cost = new AtomicReference<>(Long.valueOf(0));
		cartDTO.getCategories().forEach(cartCategoryDTO -> {
			cartCategoryDTO.getProducts().forEach(cartProductDTO -> {
				cartProductDTO.getVariants().forEach(cartVariantDTO -> {
					if (cartVariantDTO.getCost() != null) {
						cost.updateAndGet(v -> v + cartVariantDTO.getCost() * cartVariantDTO.getQuantity());
					}
				});
			});
		});
		return cost.get();
	}

	private void onChangeStock(String status, Integer variantId, Integer stockOnchange) {
		List<String> statusReduce = List.of("pendding", "success", "processing", "shipping", "awaitingadditionaldelivery", "partiallypaid", "paid", "refunded");
		List<String> statusIncrease = List.of("cancel", "failed");
		if (statusReduce.contains(status)) {
			productService.reduceStock(variantId, stockOnchange);
		}
	}

	private void onChangeStock(String status, String statusOld, Integer variantId, Integer stockOnchange) {
		List<String> statusReduce = List.of("pendding", "success", "processing", "shipping", "awaitingadditionaldelivery", "partiallypaid", "paid", "refunded");
		List<String> statusIncrease = List.of("cancel", "failed");
		if (statusReduce.contains(status)) {
			if (statusIncrease.contains(statusOld)) {
				productService.increaseStock(variantId, stockOnchange);
			}
		} else {
			if (statusReduce.contains(statusOld)) {
				productService.reduceStock(variantId, stockOnchange);
			}
		}
	}

	@Override
	public void delete(Integer orderId) {

	}

	@Override
	public void deleteByIds(List<Integer> ids) {
		List<OrderEntity> orders = orderRepository.findAllById(ids);
		orderRepository.deleteAll(orders);
	}

	@Override
	public Map<String, String> getMethod() {
		String host = env.getProperty("spring.domain");
		if (host.equals("https://nomi.vn")) {
			return Map.of(
					"PAY50PERCENT", "Chuyển khoản trước 50%",
					"COD", "Nhận hàng thanh toán",
					"TRANSFER", "Thanh toán chuyển khoản"
			);
		} else if (host.equals("https://caysenda.vn")) {
			return Map.of(
					"TRANSFER", "Thanh toán chuyển khoản",
					"PAY50PERCENT", "Chuyển khoản trước 50%"
			);
		} else {
			return Map.of(
					"PAY50PERCENT", "Chuyển khoản trước 50%",
					"COD", "Nhận hàng thanh toán",
					"TRANSFER", "Thanh toán chuyển khoản"
			);
		}

	}

	@Override
	public Map<String, String> getStatus() {
		Map<String, String> defaultStatus = new HashMap<>();
		defaultStatus.put("pendding", "Chờ thanh toán");
		defaultStatus.put("purchased", "Đã chốt đơn");
		defaultStatus.put("waitingreceived", "Đợi hàng về");
		defaultStatus.put("deliveredall", "Đã phát hết");
		defaultStatus.put("customerreceived", "Khách đã nhận");
		defaultStatus.put("cancel", "Hủy đơn hàng");
		defaultStatus.put("success", "Đã hoàn thành");
		defaultStatus.put("processing", "Đang xử lý");
		defaultStatus.put("failed", "Thất bại");
		defaultStatus.put("shipping", "Đang vận chuyển");
		defaultStatus.put("awaitingadditionaldelivery", "Chờ Phát Bổ Sung");
		defaultStatus.put("partiallypaid", "Đã thanh một phần");
		defaultStatus.put("paid", "Đã thanh toán");
		defaultStatus.put("refunded", "Đã hoàn lại tiền");
		return defaultStatus;
	}

	@Override
	public List<Map> changeStatusByIds(List<Integer> ids, String status) {
		List<OrderEntity> orders = orderRepository.findAllById(ids);
		List<String> cashFlow = List.of("pendding","partiallypaid","paid");
		List<Map> list = new ArrayList<>();

		if (orders != null) {
			orders.forEach(orderEntity -> {
				if (cashFlow.contains(status)){
					orderEntity.setCashflowstatus(status);
				}else {
					orderEntity.setStatus(status);
					if (status.equals("success")) {
						orderEntity.setPaid(orderEntity.getOrderAmount());
					}
				}
				if (orderEntity.getStatus().equals("customerreceived") && orderEntity.getCashflowstatus()!=null && orderEntity.getCashflowstatus().equals("paid")){
					orderEntity.setStatus("success");
				}
				list.add(Map.of("id",orderEntity.getId(),"status",orderEntity.getStatus(),"cashflow",orderEntity.getCashflowstatus()!=null?orderEntity.getCashflowstatus():""));
				orderRepository.save(orderEntity);
				mailSender.payment(orderEntity);
			});
		}
		return list;
	}

	@Override
	public void addToTrashByIds(List<Integer> ids) {
		List<OrderEntity> orders = orderRepository.findAllById(ids);
		if (orders != null) {
			orders.forEach(orderEntity -> {
				orderEntity.setTrash(true);
				orderRepository.save(orderEntity);
			});
		}
	}

	@Override
	public Long fee(Double weight, String address, String ward, String dictrict, String province) {
		ShipSetting shipSetting = settingService.getShipseting();
		Map result = null;
//        if (address !=null && ward!=null && dictrict!=null && province!=null){
//            result = giaoHangTietKiemService.getFeeShip(province,dictrict,address, String.valueOf(weight));
//        }else {
//            result = giaoHangTietKiemService.getFeeShip(String.valueOf(weight));
//        }
		Long fee = Long.valueOf(0);
		Double weightG = weight; // g
		Double weightKg = weightG / 1000;// kg
		if (weightKg <= 5) {
			fee = shipSetting.getFee();
		} else {
			Double extraFee = (weightKg - 5) * shipSetting.getExtraFee();
			fee = shipSetting.getFee() + extraFee.longValue();
		}

		if (shipSetting.getEnable()) {
			Long discount = Long.valueOf(0);
			if (shipSetting.getShipType().equals("PERCENT")) {
				Double amount = (fee.doubleValue() * shipSetting.getShipValue()) / 100;
				discount = amount.longValue();
			} else {
				discount = shipSetting.getShipValue().longValue();
			}
			fee = fee - discount;
			fee = fee > 0 ? fee : 0;
		}
		return fee;
	}

	@Override
	public List<OrderDetailtEntity> findAllDetailtByOrderId(Integer id) {
		return detailtRepository.findAllByOrderEntity_Id(id);
	}

	@Override
	public List<AdminOrderDetailDTO> findDetailForAdmin(Integer id) {

		return detailtRepository.findDetailtForAdmin(id);
	}

	@Override
	public List<OrderEntity> restoreByIds(List<Integer> ids) {
		if (ids == null) return null;
		List<OrderEntity> orderEntities = orderRepository.findAllById(ids);
		orderEntities.forEach(orderEntity -> {
			orderEntity.setTrash(false);
			orderRepository.save(orderEntity);
		});
		return orderEntities;
	}

	public String getFullAddress(String address, String province, String dictrict, String ward) {
		StringBuilder builder = new StringBuilder(address != null ? address : "");
		if (province!=null && dictrict!=null && ward!=null){
			AddressProviceEntity proviceEntity = provinceRepository.findById(province).orElse(null);
			AddressDictrictEntity dictrictEntity = dictrictRepository.findById(dictrict).orElse(null);
			AddressWardsEntity wardsEntity = wardRepository.findById(ward).orElse(null);

			if (wardsEntity != null) builder.append(",").append(wardsEntity.getName());
			if (dictrictEntity != null) builder.append(",").append(dictrictEntity.getName());
			if (proviceEntity != null) builder.append(",").append(proviceEntity.getName());
		}

		return builder.toString();
	}

	@Override
	public void update(AdminOrderQuickviewRequest quickviewRequest) {
		OrderEntity orderEntity = orderRepository.findById(quickviewRequest.getId()).orElse(null);
		if (orderEntity != null) {
			if (quickviewRequest.getShip() != null) orderEntity.setShip(quickviewRequest.getShip());
			if (quickviewRequest.getIncurredCost() != null)
				orderEntity.setIncurredCost(quickviewRequest.getIncurredCost());
			if (quickviewRequest.getPaid() != null) orderEntity.setPaid(quickviewRequest.getPaid());
			if (quickviewRequest.getNote() != null) orderEntity.setNote(quickviewRequest.getNote());
			if (quickviewRequest.getAdminNote() != null) orderEntity.setAdminNote(quickviewRequest.getAdminNote());
			Double discount = Double.valueOf(0);
			if (orderEntity.getDiscountType().equals("PERCENT")) {
				discount = (orderEntity.getProductAmount() * orderEntity.getDiscountValue()) / 100;
			} else {
				discount = orderEntity.getDiscountValue();
			}
			if (quickviewRequest.getCost() != null) orderEntity.setCost(quickviewRequest.getCost());
			Long orderAmount = orderEntity.getProductAmount() + orderEntity.getShip() - discount.longValue();
			orderEntity.setOrderAmount(orderAmount);
			orderRepository.save(orderEntity);
		}

	}

	@Override
	public Long countByStatus(String status) {
		if (status.equals("incomplate")){
			return orderRepository.countByStatusNotAndTrash("success", false);
		}else {
			return orderRepository.countByStatusAndTrash(status, false);
		}

	}

	@Override
	public List<ReportOrderDTO> statisticByArea(String filter, Integer year, Integer host, String status) {
		Map<String, Date> map = SpringUtils.getDateBetween(filter, year);
		List<ReportOrderDTO> orderDTOS = orderRepository.statisticByArea(map.get("from"), map.get("to"), host, status);
		orderDTOS.forEach(reportOrderDTO -> {
			if (reportOrderDTO.getProvince()!=null){
				AddressProviceEntity proviceEntity = provinceRepository.findById(reportOrderDTO.getProvince()).orElse(null);
				if (proviceEntity != null) {
					reportOrderDTO.setProvince(proviceEntity.getName());
				}
				Long profit = reportOrderDTO.getGrossRevenue() - reportOrderDTO.getShip() - reportOrderDTO.getCost() - reportOrderDTO.getIncurredCost();
				Double percentProfit = profit.doubleValue() / reportOrderDTO.getNetRevenue().doubleValue();
				DecimalFormat decimalFormat = new DecimalFormat("#.##");
				reportOrderDTO.setProfitPercent(Float.valueOf(decimalFormat.format(percentProfit * 100)));
				reportOrderDTO.setProfit(profit);
			}

		});
		return orderDTOS;
	}

	@Override
	public List<Map> statictisProduct(AdminOrderRequest orderRequest) {
		List<Map> categories = new ArrayList<>();
		Map<Integer, List<AdminOrderDetailt>> categoryGroup = orderRequest.getDetailts().stream().filter(adminOrderDetailt -> adminOrderDetailt.getCategoryId() != null).collect(groupingBy(AdminOrderDetailt::getCategoryId));
		List<AdminOrderDetailt> categoryGroupNull = orderRequest.getDetailts().stream().filter(adminOrderDetailt -> adminOrderDetailt.getCategoryId() == null).collect(Collectors.toList());
		categoryGroup.entrySet().forEach(categoryEntry -> {
			Map map = new HashMap<>();
			CategoryEntity categoryEntity = categoryService.findById(categoryEntry.getKey()).orElse(null);
			Map<Integer, List<AdminOrderDetailt>> productGroup = categoryEntry.getValue().stream().collect(groupingBy(AdminOrderDetailt::getProductId));

			map.put("categoryId", categoryEntry.getKey());
			if (categoryEntity != null) {
				map.put("name", categoryEntity.getName());
				map.put("slug", categoryEntity.getSlug());
				map.put("countproduct", productGroup.entrySet().size());
				map.put("countvariant", categoryEntry.getValue().size());
				map.put("totalquantity", categoryEntry.getValue().stream().mapToInt(value -> value.getQuantity()).sum());
			} else {
				map.put("name", "Chuyên mục mã " + categoryEntry.getKey() + " đã bị xóa");
				map.put("slug", categoryEntity.getSlug());
				map.put("countproduct", productGroup.entrySet().size());
				map.put("countvariant", categoryEntry.getValue().size());
				map.put("totalquantity", categoryEntry.getValue().stream().mapToInt(value -> value.getQuantity()).sum());
			}
//            List<Map> products = new ArrayList<>();
//            productGroup.entrySet().forEach(productEntry->{
//                Map productMap = new HashMap();
//                ProductEntity productEntity = productService.findById(productEntry.getKey());
//                productMap.put("id",productEntry.getKey());
//                if (productEntity!=null){
//                    productMap.put("name",productEntity.getNameVi());
//                }
//                productMap.put("count",productEntry.getValue().size());
//                products.add(productMap);
//            });
//            map.put("products",products);
			categories.add(map);
		});
		if (categoryGroupNull != null && categoryGroupNull.size() > 0) {
			Integer count = categoryGroupNull.stream().mapToInt(value -> value.getQuantity()).sum();
			categories.add(Map.of(
					"name", "Các sản phẩm dịch vụ không xác địng",
					"countproduct", 0,
					"countvariant", 0,
					"totalquantity", count

			));
		}
		return categories;
	}

	@Override
	public List<TrackingOrderEntity> findTrackingById(String id) {
		return trackingOrderRepository.findAllByOrOrderCode(id);
	}

	@Override
	public List<TrackingOrderEntity> getTracking(Integer orderId) {
		return trackingOrderRepository.findAllByOrderTracking_Id(orderId);
	}

	@Override
	public void updateTrackingOrder(List<TrackingOrderRequest> list) {
		if (list != null) {
			list.forEach(trackingOrderRequest -> {
				TrackingOrderEntity trackingOrderEntity = null;
				if (trackingOrderRequest.getId() != null) {
					trackingOrderEntity = trackingOrderRepository.findById(trackingOrderRequest.getId()).orElse(null);
				}

				if (trackingOrderEntity == null) {
					trackingOrderEntity = new TrackingOrderEntity();
					OrderEntity orderEntity = orderRepository.findById(trackingOrderRequest.getOrderId()).orElse(null);
					trackingOrderEntity.setOrderTracking(orderEntity);
				}
				trackingOrderEntity.setName(trackingOrderRequest.getName());
				trackingOrderEntity.setLadingCode(trackingOrderRequest.getLadingCode());
				trackingOrderEntity.setDateOrder(trackingOrderRequest.getDateOrder());
				trackingOrderEntity.setOrderCode(trackingOrderRequest.getOrderCode());
				trackingOrderEntity.setCarrier(trackingOrderRequest.getCarrier());
				trackingOrderEntity.setPackageNumber(trackingOrderRequest.getPackageNumber());
				trackingOrderEntity.setNote(trackingOrderRequest.getNote());
				trackingOrderEntity.setProductAmount(trackingOrderRequest.getProductAmount());
				trackingOrderEntity.setStatus(trackingOrderRequest.getStatus());
				trackingOrderEntity.setReceiptDate(trackingOrderRequest.getReceiptDate());
				trackingOrderEntity.setQuantity(trackingOrderRequest.getQuantity());
				trackingOrderEntity.setModifiedDate(new Date());

				trackingOrderEntity.setReceived(trackingOrderRequest.getReceived()!=null?trackingOrderRequest.getReceived():false);
				trackingOrderEntity.setPackageReceived(trackingOrderRequest.getPackageReceived());
				if (trackingOrderRequest.getPackageReceived()!=null && trackingOrderRequest.getPackageReceived()>0){
					trackingOrderEntity.setReceived(true);
					trackingOrderEntity.setViStatusCode("received");
				}
				trackingOrderRepository.save(trackingOrderEntity);
			});
		}

	}

	@Override
	public void updateTrackPackage(Integer trackId, Integer trackPackage) {
		TrackingOrderEntity trackingOrderEntity = trackingOrderRepository.findById(trackId).orElse(null);
		if (trackingOrderEntity!=null){
			trackingOrderEntity.setPackageNumber(trackPackage);
			trackingOrderRepository.save(trackingOrderEntity);
		}
	}

	@Override
	public List<TrackingOrderEntity> findAllTrackingByStatus(String status) {
		LocalDateTime localDateTime = LocalDateTime.now();
		Date from   = Date.from(localDateTime.minusMonths(2).with(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());

		return trackingOrderRepository.findAllByStatusZhNot(status, from);
	}

	@Override
	public void updateTrackingOrder(UpdateTrackingRequest trackingRequest) {
		TrackingOrderEntity trackingOrderEntity = trackingOrderRepository.findById(trackingRequest.getId()).orElse(null);
		if (trackingOrderEntity != null) {
			if (trackingRequest.getStatusZh() != null && !trackingRequest.getStatusZh().equals(""))
				trackingOrderEntity.setStatusZh(trackingRequest.getStatusZh());
			if (trackingOrderEntity.getQuantity()!=null && trackingOrderEntity.getQuantity()>0){
				if (trackingRequest.getProductAmount() != null && !trackingRequest.getProductAmount().equals(""))
					trackingOrderEntity.setProductAmount(trackingRequest.getProductAmount());
			}else {
//                trackingOrderEntity.setProductAmount("0");
			}

			if (trackingRequest.getLadingCode() != null && !trackingRequest.getLadingCode().equals("")) trackingOrderEntity.setLadingCode(trackingRequest.getLadingCode());
			if (trackingRequest.getDateOrder() != null) trackingOrderEntity.setDateOrder(trackingRequest.getDateOrder());
			if (trackingRequest.getReceiptDate() != null) trackingOrderEntity.setReceiptDate(trackingRequest.getReceiptDate());
			if (trackingRequest.getCarrier() != null && !trackingRequest.getCarrier().equals("")) trackingOrderEntity.setCarrier(trackingRequest.getCarrier());
			trackingOrderEntity.setStatusZh(trackingRequest.getStatusZh());

			switch (trackingRequest.getStatusZh() != null ? trackingRequest.getStatusZh() : "") {
				case "TRANSPORT":
					trackingOrderEntity.setStatus("Đang vận chuyển");
					break;
				case "DELIVERING":
					trackingOrderEntity.setStatus("Đang giao đến kho");
					break;
				case "SIGN":
					trackingOrderEntity.setStatus("Đã ký nhận hàng");
					break;
				default:
					trackingOrderEntity.setStatus("Chưa có thông tin giao hàng");
					break;
			}
			trackingOrderRepository.save(trackingOrderEntity);
		}
	}

	@Override
	public void deleteTrackingOrder(Integer id) {
		TrackingOrderEntity trackingOrderEntity = trackingOrderRepository.findById(id).orElse(null);
		if (trackingOrderEntity != null) {
			trackingOrderRepository.delete(trackingOrderEntity);
		}
	}

	@Override
	public byte[] generateExcelTrack(List<Integer> ids) throws IOException {
		byte[] bytes = null;
		List<OrderEntity> orders;
		if (ids.size()<=0){
			orders = orderRepository.findAll( Sort.by(Sort.Direction.DESC,"id"));
		}else {
			orders = orderRepository.findAllByIdsWithSort(ids,Sort.by(Sort.Direction.DESC,"id"));
		}

		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		if (orders.size()>0){

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			XSSFWorkbook ouputbook = new XSSFWorkbook();
			XSSFSheet outSheet = ouputbook.createSheet("THEO DOI DON HANG");
			CreationHelper createHelper = ouputbook.getCreationHelper();
			/** generate header */
			Row headerCells = outSheet.createRow(0);
			headerCells.createCell(new CellAddress("A0").getColumn()).setCellValue("Ngày khách đặt");
			headerCells.createCell(new CellAddress("B0").getColumn()).setCellValue("Mã đơn hàng");
			headerCells.createCell(new CellAddress("C0").getColumn()).setCellValue("Tên khách hàng");
			headerCells.createCell(new CellAddress("D0").getColumn()).setCellValue("Số lượng");
			headerCells.createCell(new CellAddress("E0").getColumn()).setCellValue("Sản phẩm");
			headerCells.createCell(new CellAddress("F0").getColumn()).setCellValue("Tiền hàng");
			headerCells.createCell(new CellAddress("G0").getColumn()).setCellValue("Ngày đặt");
			headerCells.createCell(new CellAddress("H0").getColumn()).setCellValue("Mã đơn hàng TQ");
			headerCells.createCell(new CellAddress("I0").getColumn()).setCellValue("Vận đơn");
			headerCells.createCell(new CellAddress("J0").getColumn()).setCellValue("Hãng vận chuyển");
			headerCells.createCell(new CellAddress("K0").getColumn()).setCellValue("Ngày nhập kho");
			headerCells.createCell(new CellAddress("L0").getColumn()).setCellValue("Trạng thái");
			headerCells.createCell(new CellAddress("M0").getColumn()).setCellValue("Số kiện");
			headerCells.createCell(new CellAddress("N0").getColumn()).setCellValue("Ghi chú");
			headerCells.createCell(new CellAddress("O0").getColumn()).setCellValue("Đã nhận");
			/** auto size*/
			outSheet.autoSizeColumn(new CellAddress("B1").getColumn(), true);
			outSheet.autoSizeColumn(new CellAddress("C1").getColumn(), true);
			outSheet.autoSizeColumn(new CellAddress("D1").getColumn(), true);
			outSheet.autoSizeColumn(new CellAddress("E1").getColumn(), true);
			outSheet.autoSizeColumn(new CellAddress("F1").getColumn(), true);
			Integer i = 1;
			List<String> checkExists = new ArrayList<>();
			for (OrderEntity orderEntity:orders){
				List<TrackingOrderEntity> tracks = trackingOrderRepository.findAllByOrderTracking_Id(orderEntity.getId());
				/** generate content */
				for (TrackingOrderEntity track:tracks){

					Row cells = outSheet.createRow(i);
					String createDate = orderEntity.getCreateDate()!=null?format.format(orderEntity.getCreateDate()):"";
					String orderCode = orderEntity.getOrderCode();
					String customerName = orderEntity.getBillingFullName();
					Integer quantity = track.getQuantity()!=null?track.getQuantity():0;
					String productName = track.getName()!=null?track.getName():"";
					String productAmout = track.getProductAmount()!=null?track.getProductAmount():"";
					String dateOrder = track.getDateOrder()!=null?format.format(track.getDateOrder()):"";
					String trackCode = track.getOrderCode()!=null?track.getOrderCode():"";
					String laddingCode = track.getLadingCode()!=null?track.getLadingCode():"";
					String carrier= track.getCarrier()!=null?track.getCarrier():"";
					String getReceiptDate = track.getReceiptDate()!=null?format.format(track.getReceiptDate()):"";
					String status = track.getStatus()!=null?track.getStatus():"";
					Integer packageNumber = track.getPackageNumber()!=null?track.getPackageNumber():0;
					String note = track.getNote()!=null?track.getNote():"";
					Integer packageReceived = track.getPackageReceived()!=null?track.getPackageReceived():0;
					cells.createCell(new CellAddress("A"+i).getColumn()).setCellValue(createDate);
					cells.createCell(new CellAddress("B"+i).getColumn()).setCellValue(orderCode);
					cells.createCell(new CellAddress("C"+i).getColumn()).setCellValue(customerName);
					cells.createCell(new CellAddress("D"+i).getColumn()).setCellValue(quantity);
					cells.createCell(new CellAddress("E"+i).getColumn()).setCellValue(productName);
					cells.createCell(new CellAddress("F"+i).getColumn()).setCellValue(productAmout);
					cells.createCell(new CellAddress("G"+i).getColumn()).setCellValue(dateOrder);
					cells.createCell(new CellAddress("H"+i).getColumn()).setCellValue(trackCode);
					String check = checkExists.stream().filter(s -> s.equals(laddingCode)).collect(Collectors.joining());
					if (check!=null && !check.equals("")){

					}else {
						checkExists.add(laddingCode);
						Cell laddingCell = cells.createCell(new CellAddress("I"+i).getColumn());
						laddingCell.setCellValue(laddingCode);
						Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
						link.setAddress("https://trade.1688.com/order/new_step_order_detail.htm?orderId="+trackCode);
						laddingCell.setHyperlink(link);
					}

					cells.createCell(new CellAddress("J"+i).getColumn()).setCellValue(carrier);
					cells.createCell(new CellAddress("K"+i).getColumn()).setCellValue(getReceiptDate);
					switch (track.getStatusZh() != null ? track.getStatusZh() : "") {
						case "TRANSPORT":
							cells.createCell(new CellAddress("L"+i).getColumn()).setCellValue("正在路上");
							break;
						case "DELIVERING":
							cells.createCell(new CellAddress("L"+i).getColumn()).setCellValue("正在路上");
							break;
						case "SIGN":
							cells.createCell(new CellAddress("L"+i).getColumn()).setCellValue("已签收");
							break;
						default:
							cells.createCell(new CellAddress("L"+i).getColumn()).setCellValue("还查不到");
							break;
					}

					cells.createCell(new CellAddress("M"+i).getColumn()).setCellValue(packageNumber);
					cells.createCell(new CellAddress("N"+i).getColumn()).setCellValue(note);
					cells.createCell(new CellAddress("O"+i).getColumn()).setCellValue(packageReceived);
					i++;
				}
			}

			ouputbook.write(outputStream);
			bytes = outputStream.toByteArray();
			ouputbook.close();
			outputStream.close();
		}

		return bytes;
	}

	@Override
	public Double exchangeRateTracking() {
		Double value = optionService.getData("EXCHANGERATETRACKING", Double.class);
		if (value == null) value = Double.valueOf(3800);
		return value;
	}

	@Override
	public StatictisTracking statictisTracking(String month, Integer year) {
		Map<String, Date> dateBetween = SpringUtils.getDateBetween(month, year);
		List<TrackingOrderEntity> trackingOrderEntities = trackingOrderRepository.findAllByCreateDateBetween(dateBetween.get("from"), dateBetween.get("to"));
		Double totalCost = Double.valueOf(0);
		Integer totalPackage = 0;
		for (TrackingOrderEntity trackingOrderEntity : trackingOrderEntities) {
			if (trackingOrderEntity.getProductAmount() != null && !trackingOrderEntity.getProductAmount().equals("")) {
				Double aDouble = Double.parseDouble(trackingOrderEntity.getProductAmount().replace(",", ""));
				totalCost += aDouble;
			}
			if (trackingOrderEntity.getPackageNumber() != null) {
				totalPackage += trackingOrderEntity.getPackageNumber();
			} else {
				totalPackage += 0;
			}
		}
		return new StatictisTracking(Precision.round(totalCost, 2), totalPackage);

	}

	@Override
	public void updateExchangeRateTracking(Double value) {
		optionService.update(value, "EXCHANGERATETRACKING");
	}

	@Override
	public byte[] exportOrderToExcel(Integer orderId) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		XSSFWorkbook ouputbook = new XSSFWorkbook();
		XSSFSheet outSheet = ouputbook.createSheet("HOADON");
		OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
		generateHeaderExportOrderToExcel(outSheet.createRow(0));
		/** auto size*/

		outSheet.autoSizeColumn(new CellAddress("B1").getColumn(), true);
		outSheet.autoSizeColumn(new CellAddress("C1").getColumn(), true);
		outSheet.autoSizeColumn(new CellAddress("D1").getColumn(), true);
		outSheet.autoSizeColumn(new CellAddress("E1").getColumn(), true);
		outSheet.autoSizeColumn(new CellAddress("F1").getColumn(), true);
		Integer i = 1;
		if (orderEntity != null) {
			List<OrderDetailtEntity> detailts = detailtRepository.findAllByOrderEntity_Id(orderId);
			for (OrderDetailtEntity detailtEntity : detailts) {
				Row cells = outSheet.createRow(i++);
				addPicture(detailtEntity.getVariantThumbnail() != null ? detailtEntity.getVariantThumbnail() : detailtEntity.getProductThumbnail(), cells, ouputbook, outSheet);
				Cell productNameCell = cells.createCell(new CellAddress("B1").getColumn());
				Cell quantityCell = cells.createCell(new CellAddress("C1").getColumn());
				Cell priceCell = cells.createCell(new CellAddress("D1").getColumn());
				Cell totals = cells.createCell(new CellAddress("E1").getColumn());
				Cell oweCell = cells.createCell(new CellAddress("F1").getColumn());

				productNameCell.setCellValue(detailtEntity.getName() + "-" + detailtEntity.getVariantName());
				quantityCell.setCellValue(detailtEntity.getQuantity());
				priceCell.setCellValue(detailtEntity.getPrice());
				totals.setCellValue(detailtEntity.getPrice() * detailtEntity.getQuantity());
				oweCell.setCellValue(detailtEntity.getOwe());

			}
			/** Tổng sản phẩm*/
			Row cells = outSheet.createRow(i++);
			CellRangeAddress cellRangeAddress = new CellRangeAddress(cells.getRowNum(), cells.getRowNum(), new CellAddress("A1").getColumn(), new CellAddress("D1").getColumn());
			outSheet.addMergedRegionUnsafe(cellRangeAddress);
			CellRangeAddress cellValueRangeAddress = new CellRangeAddress(cells.getRowNum(), cells.getRowNum(), new CellAddress("E1").getColumn(), new CellAddress("F1").getColumn());
			outSheet.addMergedRegionUnsafe(cellValueRangeAddress);
			Cell labelCell = cells.createCell(0);
			labelCell.setCellValue("Tổng sản phẩm");
			Cell valueCell = cells.createCell(new CellAddress("E1").getColumn());
			valueCell.setCellValue(orderEntity.getProductAmount());
			/** Phí vận chuyển*/
			cells = outSheet.createRow(i++);
			cellRangeAddress = new CellRangeAddress(cells.getRowNum(), cells.getRowNum(), new CellAddress("A1").getColumn(), new CellAddress("D1").getColumn());
			outSheet.addMergedRegionUnsafe(cellRangeAddress);
			cellValueRangeAddress = new CellRangeAddress(cells.getRowNum(), cells.getRowNum(), new CellAddress("E1").getColumn(), new CellAddress("F1").getColumn());
			outSheet.addMergedRegionUnsafe(cellValueRangeAddress);
			cells.createCell(0).setCellValue("Phí vận chuyển");
			cells.createCell(new CellAddress("E1").getColumn()).setCellValue(orderEntity.getShip());
			/** Tổng đơn hàng*/
			cells = outSheet.createRow(i++);
			cellRangeAddress = new CellRangeAddress(cells.getRowNum(), cells.getRowNum(), new CellAddress("A1").getColumn(), new CellAddress("D1").getColumn());
			outSheet.addMergedRegionUnsafe(cellRangeAddress);
			cellValueRangeAddress = new CellRangeAddress(cells.getRowNum(), cells.getRowNum(), new CellAddress("E1").getColumn(), new CellAddress("F1").getColumn());
			outSheet.addMergedRegionUnsafe(cellValueRangeAddress);
			cells.createCell(0).setCellValue("Tổng đơn hàng");
			cells.createCell(new CellAddress("E1").getColumn()).setCellValue(orderEntity.getOrderAmount());
			/** Đã thanh toán*/
			cells = outSheet.createRow(i++);
			cellRangeAddress = new CellRangeAddress(cells.getRowNum(), cells.getRowNum(), new CellAddress("A1").getColumn(), new CellAddress("D1").getColumn());
			outSheet.addMergedRegionUnsafe(cellRangeAddress);
			cellValueRangeAddress = new CellRangeAddress(cells.getRowNum(), cells.getRowNum(), new CellAddress("E1").getColumn(), new CellAddress("F1").getColumn());
			outSheet.addMergedRegionUnsafe(cellValueRangeAddress);
			cells.createCell(0).setCellValue("Đã thanh toán");
			cells.createCell(new CellAddress("E1").getColumn()).setCellValue(orderEntity.getPaid());
		}
		ouputbook.write(outputStream);
		byte[] bytes = outputStream.toByteArray();
		ouputbook.close();
		outputStream.close();
		return bytes;
	}

	@Override
	public byte[] downloadImageFromOrder(Integer orderId) throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		List<OrderDetailtEntity> detailts = detailtRepository.findAllByOrderEntity_Id(orderId);
		if (detailts.size() > 0) {
			ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);
			List<Integer> ids = detailts.stream().map(orderDetailtEntity -> orderDetailtEntity.getProductId()).collect(Collectors.toList());
			List<ProductEntity> products = productService.findAllByIds(ids);
			for (ProductEntity productEntity : products) {
				for (String s : productEntity.getGallery()) {
					byte[] bytes = getInputImage(s);
					if (bytes != null) {
						ZipEntry zipEntry = new ZipEntry(productEntity.getNameVi() + "/" + FilenameUtils.getName(s));
						try {
							zipOut.putNextEntry(zipEntry);
							zipOut.write(bytes);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}
			}
			zipOut.close();
		}
		byte[] bytes = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();
		return bytes;
	}

	@Override
	public List<OrderWithTrackingDTO> findAllByLaddingCode(String laddingCode) {
		List<OrderWithTrackingDTO> orders = orderRepository.findALlByLaddingCode(laddingCode);
		orders.forEach(order -> {
			order.setFullAddress(getFullAddress(order.getBillingAddress(),order.getBillingCity(),order.getBillingDistrict(),order.getBillingWards()));
		});
		return orders;

	}

	@Override
	public Boolean trackReceiveProduct(Integer id, Integer packageReceived) {
		TrackingOrderEntity trackingOrderEntity = trackingOrderRepository.findById(id).orElse(null);
		if (trackingOrderEntity!=null){
			trackingOrderEntity.setViStatusCode("received");
			trackingOrderEntity.setPackageReceived(packageReceived);
			trackingOrderEntity.setReceived(true);
			trackingOrderRepository.save(trackingOrderEntity);
			List<TrackingOrderEntity> list = orderRepository.findALlByLaddingCode(trackingOrderEntity.getLadingCode(),List.of(id));
			list.forEach(trackingOrderEntity1 -> {
				trackingOrderEntity1.setReceived(true);
				trackingOrderEntity1.setViStatusCode("received");
				trackingOrderRepository.save(trackingOrderEntity1);
			});
			return true;
		}
		return false;
	}

	@Override
	public OrderEntity buynow(BuynowRequest buynowRequest) {
		if (buynowRequest.getProductId()!=null){
			ProductEntity productEntity = productService.findById(buynowRequest.getProductId());
			CategoryEntity categoryEntity = categoryService.findById(productEntity.getCategoryDefault()).orElse(null);
			ProviderEntity providerEntity = providerRepository.findByHost(env.getProperty("spring.domain"));
			if (productEntity!=null){
				Optional<VariantEntity> variantEntity = variantRepository.findById(buynowRequest.getVariantId());
				if (variantEntity.isPresent()){
					OrderEntity orderEntity = new OrderEntity();
					orderEntity.setBillingFullName(buynowRequest.getFullname());
					orderEntity.setBillingPhoneNumber(buynowRequest.getPhone());
					orderEntity.setOrderComment("");
					orderEntity.setBillingAddress(buynowRequest.getAddress());
					orderEntity.setBillingCity(buynowRequest.getProvince());
					orderEntity.setBillingDistrict(buynowRequest.getDictrict());
					orderEntity.setBillingWards(buynowRequest.getWard());
					orderEntity.setMethod("COD");
					orderEntity.setNote(buynowRequest.getNote());
					orderEntity.setShip(Long.valueOf(0));
					orderEntity.setPaid(Long.valueOf(0));
					orderEntity.setProductAmount(variantEntity.get().getPrice());
					orderEntity.setStatus("processing");
					orderEntity.setRefunded(Long.valueOf(0));
					orderEntity.setIncurredCost(Long.valueOf(0));
					orderEntity.setOrderAmount(orderEntity.getProductAmount() + orderEntity.getShip() - orderEntity.getIncurredCost());
					orderEntity.setTrash(false);
					orderEntity.setDiscountType("PERCENT");
					orderEntity.setDiscountValue(Double.valueOf(0));
					orderEntity.setCost(variantEntity.get().getCost());
					orderEntity.setProviderOrder(List.of(providerEntity));
					orderEntity = orderRepository.save(orderEntity);
					StringBuilder codeBuilder = new StringBuilder();
					LocalDateTime localDateTime = LocalDateTime.now();
					codeBuilder.append(localDateTime.getMonthValue() < 10 ? "0" + localDateTime.getMonthValue() : localDateTime.getMonthValue());
					codeBuilder.append(localDateTime.getDayOfMonth() < 10 ? "0" + localDateTime.getDayOfMonth() : localDateTime.getDayOfMonth());
					codeBuilder.append(orderEntity.getId());
					orderEntity.setOrderCode(codeBuilder.toString());
					/** detailts */
					List<OrderDetailtEntity> detailts = new ArrayList<>();
					OrderDetailtEntity detailtEntity = new OrderDetailtEntity();
					detailtEntity.setQuantity(1);
					detailtEntity.setCost(variantEntity.get().getCost()!=null?variantEntity.get().getCost() : 0);
					detailtEntity.setName(variantEntity.get().getNameZh());
					detailtEntity.setVariantName(variantEntity.get().getNameVi());
					detailtEntity.setProductThumbnail(productEntity.getThumbnail());
					detailtEntity.setVariantThumbnail(variantEntity.get().getThumbnail());
					detailtEntity.setOrderEntity(orderEntity);
					detailtEntity.setOwe(1);
					detailtEntity.setVariantId(variantEntity.get().getId());
					detailtEntity.setProductId(productEntity.getId());
					detailtEntity.setSlug("/" + categoryEntity.getSlug() + "/" + productEntity.getSlugVi());
					detailtEntity.setLinkZh(productEntity.getLink());
					detailtEntity.setWeight(variantEntity.get().getWeight() != null ? variantEntity.get().getWeight() : 0);
					detailtEntity.setSku(variantEntity.get().getSkuVi());
					detailtEntity.setNameZh(productEntity.getNameZh());
					detailtEntity.setVariantNameZh(variantEntity.get().getNameZh());
					GroupVariantEntity groupVariantEntity = ProductUtils.getGroupByGroupSku(productEntity,variantEntity.get().getSkuVi());
					if (groupVariantEntity!=null){
						detailtEntity.setGroupName(groupVariantEntity.getName());
						detailtEntity.setGroupZhName(groupVariantEntity.getZhName());
						detailtEntity.setGroupSku(groupVariantEntity.getSkuGroup());
					}

					detailtEntity.setCategoryId(productEntity.getCategoryDefault());
					detailtEntity.setUnit(productEntity.getUnit());
					detailtEntity.setPrice(variantEntity.get().getPrice());

					onChangeStock(orderEntity.getStatus(), variantEntity.get().getId(), 1);
					detailts.add(detailtRepository.save(detailtEntity));
					orderEntity.setDetailts(detailts);
					return  orderEntity;
				}

			}
		}

		return null;
	}

	@Override
	public OrderEntity split(AdminOrderSplitRequest iSplit) {
		OrderEntity orderEntity = orderRepository.findById(iSplit.getOrderId()).get();
		List<OrderDetailtEntity> detailts = detailtRepository.findAllById(iSplit.getIds());
		// create new order
		orderEntity.setId(null);
		orderEntity.setShip(Long.valueOf(0));
		orderEntity.setPaid(Long.valueOf(0));
		orderEntity.setProductAmount(detailts.stream().mapToLong(value -> value.getPrice() * value.getQuantity()).sum());
		orderEntity.setRefunded(Long.valueOf(0));
		orderEntity.setIncurredCost(Long.valueOf(0));
		orderEntity.setOrderAmount(orderEntity.getProductAmount() + orderEntity.getShip() - orderEntity.getIncurredCost());
		orderEntity.setCost(detailts.stream().mapToLong(value -> value.getCost() * value.getQuantity()).sum());
		orderEntity = orderRepository.save(orderEntity);
		for(OrderDetailtEntity orderDetailtEntity: detailts) {
			orderDetailtEntity.setOrderEntity(orderEntity);
		}

		SimpleDateFormat format = new SimpleDateFormat("MMdd");
		orderEntity.setOrderCode(format.format(new Date()) + orderEntity.getId());
		orderEntity = orderRepository.save(orderEntity);

		detailts = detailtRepository.saveAll(detailts);
		orderEntity.setDetailts(detailts);

		//  update old order
		OrderEntity oldOrder = orderRepository.findById(iSplit.getOrderId()).get();
		List<OrderDetailtEntity> oldDetailts = detailtRepository.findAllByOrderEntity_Id(iSplit.getOrderId());
		oldOrder.setProductAmount(oldDetailts.stream().mapToLong(value -> value.getPrice() * value.getQuantity()).sum());
		oldOrder.setOrderAmount(oldOrder.getProductAmount() + oldOrder.getShip() - oldOrder.getIncurredCost());
		oldOrder.setCost(oldDetailts.stream().mapToLong(value -> value.getCost() * value.getQuantity()).sum());
		orderRepository.save(oldOrder);
		return orderEntity;
	}

	private void generateHeaderExportOrderToExcel(Row cells) {
		cells.createCell(new CellAddress("A1").getColumn()).setCellValue("Hình ảnh");
		cells.createCell(new CellAddress("B1").getColumn()).setCellValue("Tên sản phẩm");
		cells.createCell(new CellAddress("C1").getColumn()).setCellValue("Số lượng");
		cells.createCell(new CellAddress("D1").getColumn()).setCellValue("Giá");
		cells.createCell(new CellAddress("E1").getColumn()).setCellValue("Tổng");
		cells.createCell(new CellAddress("F1").getColumn()).setCellValue("Nợ");

	}

	private Picture addPicture(String url, Row row, XSSFWorkbook workbook, XSSFSheet sheet) throws Exception {
		/*** image * */
		//Get the contents of an InputStream as a byte[].
		byte[] input = getInputImage(url);
		if (input != null) {
			InputStream imageInput = new ByteArrayInputStream(input);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] bytes = IOUtils.toByteArray(imageInput);
			BufferedImage bufferedImage = ImageUtils.cropAndResize(bytes, 200);
			ImageIO.write(bufferedImage, FilenameUtils.getExtension(url), byteArrayOutputStream);
			bytes = byteArrayOutputStream.toByteArray();

			//Adds a picture to the workbook
			int pictureIdx = workbook.addPicture(bytes, workbook.PICTURE_TYPE_PNG);
			//close the input stream
			imageInput.close();
			//Returns an object that handles instantiating concrete classes
			CreationHelper helper = workbook.getCreationHelper();
			//Creates the top-level drawing patriarch.
			Drawing drawing = sheet.createDrawingPatriarch();
			//Create an anchor that is attached to the worksheet
			ClientAnchor anchor = helper.createClientAnchor();
			anchor.setCol1(new CellAddress("A" + row.getRowNum()).getColumn());
			anchor.setRow1(row.getRowNum()); //Row 3
			anchor.setCol2(new CellAddress("B" + row.getRowNum()).getColumn());
			anchor.setRow2(row.getRowNum() + 1); //Row 4
			Picture pict = drawing.createPicture(anchor, pictureIdx);
			Cell cell = row.createCell(new CellAddress("A" + row.getRowNum()).getColumn());
			//set width to n character widths = count characters * 256
			int widthUnits = 40 * 20;
			sheet.setColumnWidth(cell.getColumnIndex(), widthUnits);
			//set height to n points in twips = n * 20
			short heightUnits = 40 * 20;
			cell.getRow().setHeight(heightUnits);
			/*
			 *end image
			 * */
			byteArrayOutputStream.close();
			return pict;
		}

		return null;
	}

	private byte[] getInputImage(String url) {
		UrlValidator urlValidator = new UrlValidator();
		if (urlValidator.isValid(url)) {
			try {
				ResponseEntity<byte[]> inputImage = restTemplate.getForEntity(url, byte[].class);
				if (inputImage.getStatusCodeValue() == 200) {
					return inputImage.getBody();
				}
			} catch (HttpClientErrorException httpClientErrorException) {
				httpClientErrorException.printStackTrace();
			}
		} else {
			InputStream inputStream = null;
			try {
				File s = UploadFileUtils.getPath("static/");
				File folder = new File(s.getPath() + "/upload");
				File image = new File(folder.getPath() + "/" + FilenameUtils.getName(url));
				inputStream = new FileInputStream(image);
				return inputStream.readAllBytes();
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			}
		}

		return null;
	}
}
