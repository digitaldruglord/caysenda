package com.nomi.caysenda.services.delivery.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomi.caysenda.api.admin.delivery.model.*;
import com.nomi.caysenda.api.web.models.ChangePasswordRequest;
import com.nomi.caysenda.api.web.models.ContactFormRequest;
import com.nomi.caysenda.api.web.models.UserDeliveryRegister;
import com.nomi.caysenda.dto.UserDTO;
import com.nomi.caysenda.entity.ContactFormDeliveryEntity;
import com.nomi.caysenda.entity.delivery.*;
import com.nomi.caysenda.repositories.ContactFormRepository;
import com.nomi.caysenda.repositories.UserDeliveryRepository;
import com.nomi.caysenda.repositories.delivery.*;
import com.nomi.caysenda.security.JwtTokenProvider;
import com.nomi.caysenda.services.MyMailSender;
import com.nomi.caysenda.services.delivery.DeliveryService;
import com.nomi.caysenda.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired UserDeliveryRepository userDeliveryRepository;
    @Autowired ContactFormRepository contactFormRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired JwtTokenProvider jwtTokenProvider;
    @Autowired DeliveryOrderRepository deliveryOrderRepository;
    @Autowired DeliveryOrderStatusRepository deliveryOrderStatusRepository;
    @Autowired DeliveryCurrencyRepository currencyRepository;
    @Autowired DeliveryRepository deliveryRepository;
    @Autowired DeliveryStatusRepository deliveryStatusRepository;
    @Autowired MyMailSender myMailSender;

    @Override
    public Map register(UserDeliveryRegister deliveryRegister) {
        Map map = new HashMap();
        if (!deliveryRegister.getConfirmPassword().equals(deliveryRegister.getPassword())){
            map.put("message","Vui l??ng x??c nh???n l???i m???t kh???u");
            map.put("success",false);
            return map;
        }
        UserDeliveryEntity userDeliveryEntity = new UserDeliveryEntity();
        userDeliveryEntity.setUserName(deliveryRegister.getUserName());
        userDeliveryEntity.setPhoneNumber(deliveryRegister.getPhoneNumber());
        userDeliveryEntity.setPassword(passwordEncoder.encode(deliveryRegister.getPassword()));
        try{
            userDeliveryRepository.save(userDeliveryEntity);
        }catch ( DataIntegrityViolationException e){
            map.put("message","C??c th??ng tin email t??i kho???n s??? ??i???n tho???i ???? t???n t???i tr??n h??? th???ng");
            map.put("success",false);
            return map;
        }
        map.put("success",true);
        map.put("message","T???o t??i kho???n th??nh c??ng");
        return map;
    }

    @Override
    public Map login(String userName, String password) {
        UserDeliveryEntity userDeliveryEntity = userDeliveryRepository.findByUserName(userName);
        if (userDeliveryEntity!=null){
            if (passwordEncoder.matches(password,userDeliveryEntity.getPassword())){
                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(userDeliveryEntity.getUserName());
                userDTO.setPhonenumber(userDeliveryEntity.getPhoneNumber());
                String token = jwtTokenProvider.generateToken(userDTO);
                return Map.of("token",token,"message","????ng nh???p th??nh c??ng","success",true);
            }else {
                return Map.of("message","Th??ng tin kh??ng h???p l???","success",false);
            }
        }else {
            return Map.of("message","Th??ng tin kh??ng h???p l???","success",false);
        }

    }

    @Override
    public Map changePassword(ChangePasswordRequest request) {
        UserDeliveryEntity userDeliveryEntity = getUserDelivery(request.getToken());
        Map map = new HashMap();
        map.put("success",false);
        map.put("message","Thay ?????i m???t kh???u kh??ng th??nh c??ng");
        if (userDeliveryEntity!=null){
            if (request.getConfirmNewPassword().trim().equals(request.getNewPassword().trim())){
                if (passwordEncoder.matches(request.getOldPassword(),userDeliveryEntity.getPassword())){
                    userDeliveryEntity.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    userDeliveryRepository.save(userDeliveryEntity);
                    map.put("success",true);
                    map.put("message","Thay ?????i m???t kh???u th??nh c??ng");
                }
            }
        }
        return map;
    }

    @Override
    public ContactFormDeliveryEntity createForm(ContactFormRequest formRequest) {
        UserDeliveryEntity deliveryEntity = getUserDelivery(formRequest.getToken());
        ContactFormDeliveryEntity formDeliveryEntity = new ContactFormDeliveryEntity();
        formDeliveryEntity.setType(formRequest.getType());
        formDeliveryEntity.setUserDeliveryEntity(deliveryEntity);
        formDeliveryEntity.setData(SpringUtils.convertObjectToJson(formRequest.getData()));

        return contactFormRepository.save(formDeliveryEntity);
    }

    @Override
    public DeliveryOrderEntity createOrder(ContactFormRequest formRequest) {
        UserDeliveryEntity userDeliveryEntity = getUserDelivery(formRequest.getToken());
        DeliveryOrderEntity deliveryOrderEntity = new DeliveryOrderEntity();
        if (userDeliveryEntity!=null){
            /** create delivery order entity */
            String fullname = (String) formRequest.getData().get("fullname");
            String phoneNumber = (String) formRequest.getData().get("phoneNumber");
            String productName = (String) formRequest.getData().get("productName");
            String packageQuantity = (String) formRequest.getData().get("packageQuantity");
            String link = (String) formRequest.getData().get("link");
            String address = (String) formRequest.getData().get("address");
            deliveryOrderEntity.setFullName(fullname);
            deliveryOrderEntity.setPhoneNumber(phoneNumber);
            deliveryOrderEntity.setProductName(productName);
            deliveryOrderEntity.setQuantity(packageQuantity!=null?Integer.valueOf(packageQuantity):0);
            deliveryOrderEntity.setLink(link);
            deliveryOrderEntity.setAddress(address);
            deliveryOrderEntity.setUserDeliveryEntity(userDeliveryEntity);
            deliveryOrderEntity.setCreateDate(new Date());
            deliveryOrderEntity  = deliveryOrderRepository.save(deliveryOrderEntity);
            /** add status */
            DeliveryOrderStatus status = new DeliveryOrderStatus();
            status.setStatus("PROCESSING");
            status.setNote("???? ?????t ????n h??ng");
            status.setCreateDate(new Date());
            status.setDeliveryOrderEntity(deliveryOrderEntity);
            status = deliveryOrderStatusRepository.save(status);
            deliveryOrderEntity.setStatusList(List.of(status));
            myMailSender.sendEmailDeliveryOrder(deliveryOrderEntity);
        }

        return deliveryOrderEntity;
    }

    @Override
    public DeliveryOrderEntity updateOrder(UpdateDeliveryOrderRequest map) {
        /** get params */

        /** update */
        DeliveryOrderEntity deliveryOrderEntity = deliveryOrderRepository.findById(map.getId()).orElse(null);
        if (deliveryOrderEntity!=null){
            deliveryOrderEntity.setZhId(map.getZhId());
            deliveryOrderEntity.setViId(map.getViId());
            deliveryOrderEntity.setAmount(map.getAmount());
            deliveryOrderEntity.setCost(map.getCost());
            deliveryOrderEntity.setFee(map.getFee());
            deliveryOrderEntity.setPaid(map.getPaid());
            deliveryOrderEntity = deliveryOrderRepository.save(deliveryOrderEntity);
        }
        return deliveryOrderEntity;
    }

    @Override
    public DeliveryOrderStatus addDeliveryStatus(AddDeliveryOrderStatusRequest request) {
        DeliveryOrderEntity deliveryOrderEntity = deliveryOrderRepository.findById(request.getId()).orElse(null);
        DeliveryOrderStatus status = new DeliveryOrderStatus();
        status.setStatus(request.getStatus());
        status.setNote(request.getNote());
        status.setCreateDate(new Date());
        status.setDeliveryOrderEntity(deliveryOrderEntity);
        return deliveryOrderStatusRepository.save(status);
    }

    @Override
    public Long deliveryOrderCount() {
        return deliveryOrderRepository.deliveryOrderCount().stream().mapToLong(value -> value).sum();
    }

    @Override
    public DeliveryCurrencyEntity createCurrency(ContactFormRequest formRequest) {
        UserDeliveryEntity userDeliveryEntity = getUserDelivery(formRequest.getToken());
        DeliveryCurrencyEntity deliveryCurrencyEntity = null;

        /** create delivery order entity */
        String fullname = (String) formRequest.getData().get("fullname");
        String phoneNumber = (String) formRequest.getData().get("phoneNumber");
        Long amount = formRequest.getData().get("amount")!=null?Long.valueOf(String.valueOf(formRequest.getData().get("amount"))):0;
        deliveryCurrencyEntity = new DeliveryCurrencyEntity();
        deliveryCurrencyEntity.setFullName(fullname);
        deliveryCurrencyEntity.setPhoneNumber(phoneNumber);
        deliveryCurrencyEntity.setAmount(amount);
        deliveryCurrencyEntity.setStatus("PROCESSING");
        deliveryCurrencyEntity.setCreateDate(new Date());
        if (userDeliveryEntity!=null){
            deliveryCurrencyEntity.setUserDeliveryEntity(userDeliveryEntity);
        }
        deliveryCurrencyEntity = currencyRepository.save(deliveryCurrencyEntity);
        myMailSender.sendEmailDeliveryCurrency(deliveryCurrencyEntity);
        return deliveryCurrencyEntity;
    }

    @Override
    public Page<DeliveryCurrencyEntity> findAllCurrency(String token, Pageable pageable) {
        UserDeliveryEntity userDeliveryEntity = getUserDelivery(token);
        if (userDeliveryEntity!=null){
            return currencyRepository.findAllByUserDeliveryEntity_Id(userDeliveryEntity.getId(),pageable);
        }
        return null;
    }

    @Override
    public Page<DeliveryCurrencyEntity> findAllCurrency(Pageable pageable) {
        return currencyRepository.findAll(pageable);
    }


    @Override
    public DeliveryCurrencyEntity findCurrencyById(String token, Integer id) {
        UserDeliveryEntity userDeliveryEntity = getUserDelivery(token);
        if (userDeliveryEntity!=null){
            return currencyRepository.findByIdAndUserDeliveryEntity_Id(id,userDeliveryEntity.getId());
        }
        return null;

    }

    @Override
    public DeliveryCurrencyEntity updateCurrency(UpdateDeliveryCurrencyRequest request) {
        DeliveryCurrencyEntity deliveryCurrencyEntity = currencyRepository.findById(request.getId()).orElse(null);
        if (deliveryCurrencyEntity!=null){
            if (request.getAmount()!=null) deliveryCurrencyEntity.setAmount(request.getAmount());
            if (request.getStatus()!=null) deliveryCurrencyEntity.setStatus(request.getStatus());
        }
        return currencyRepository.save(deliveryCurrencyEntity);
    }

    @Override
    public DeliveryCurrencyEntity updateStatus(ChangeStatusCurrencyRequest request) {
        return null;
    }

    @Override
    public Long currencyCount() {
        return currencyRepository.countByStatus("");
    }

    @Override
    public DeliveryEntity findDeliveryById(Integer id) {
        return deliveryRepository.findById(id).orElse(null);
    }

    @Override
    public DeliveryEntity findDeliveryById(String token, Integer id) {
        UserDeliveryEntity userDeliveryEntity = getUserDelivery(token);
        if (userDeliveryEntity!=null){
            return deliveryRepository.findByIdAndUserDeliveryEntity_Id(id,userDeliveryEntity.getId());
        }
        return null;
    }

    @Override
    public Page<DeliveryEntity> findAllDelivery(Pageable pageable) {
        return deliveryRepository.findAll(pageable);
    }

    @Override
    public Page<DeliveryEntity> getDelivery(String token, Pageable pageable) {
        UserDeliveryEntity userDeliveryEntity = getUserDelivery(token);
        if (userDeliveryEntity!=null){
            return deliveryRepository.findAllByUserDeliveryEntity_Id(userDeliveryEntity.getId(),pageable);
        }
        return null;
    }

    @Override
    public DeliveryEntity createDelivery(ContactFormRequest formRequest) {
        UserDeliveryEntity userDeliveryEntity = getUserDelivery(formRequest.getToken());
        DeliveryEntity deliveryEntity = new DeliveryEntity();
        if (userDeliveryEntity!=null){
            /** create delivery order entity */

            ObjectMapper mapper = new ObjectMapper();
            CreateDeliveryRequest request = mapper.convertValue(formRequest.getData(),CreateDeliveryRequest.class);
            deliveryEntity.setFullName(request.getFullName());
            deliveryEntity.setPhoneNumber(request.getPhoneNumber());
            deliveryEntity.setProductName(request.getProductName());
            deliveryEntity.setQuantity(request.getQuantity());
            deliveryEntity.setVolume(request.getVolume());
            deliveryEntity.setWeight(request.getWeight());
            deliveryEntity.setAddress(request.getAddress());
            deliveryEntity.setUserDeliveryEntity(userDeliveryEntity);

            deliveryEntity.setCreateDate(new Date());
            deliveryEntity  = deliveryRepository.save(deliveryEntity);
            /** add status */
            DeliveryStatusEntity status = new DeliveryStatusEntity();
            status.setStatus("PROCESSING");
            status.setNote("???? ?????t ????n h??ng");
            status.setCreateDate(new Date());
            status.setDeliveryEntity(deliveryEntity);
            status = deliveryStatusRepository.save(status);
            deliveryEntity.setStatusList(List.of(status));
            myMailSender.sendEmailDelivery(deliveryEntity);
        }

        return deliveryEntity;
    }

    @Override
    public DeliveryEntity updateDelivery(UpdateDeliveryRequest map) {
        DeliveryEntity deliveryEntity = deliveryRepository.findById(map.getId()).orElse(null);
        if (deliveryEntity!=null){
            if (map.getAmount()!=null) deliveryEntity.setAmount(map.getAmount());
            if (map.getCost()!=null) deliveryEntity.setCost(map.getCost());
            if (map.getFee()!=null) deliveryEntity.setFee(map.getFee());
            if (map.getPaid()!=null) deliveryEntity.setPaid(map.getPaid());
            if (map.getViId()!=null) deliveryEntity.setViId(map.getViId());
            if (map.getZhId()!=null) deliveryEntity.setZhId(map.getZhId());
            if (map.getVolume()!=null) deliveryEntity.setVolume(map.getVolume());
            if (map.getWeight()!=null) deliveryEntity.setWeight(map.getWeight());
            return deliveryRepository.save(deliveryEntity);
        }
        return null;
    }


    @Override
    public DeliveryStatusEntity addDeliveryStatus(AddDeliveryStatusRequest request) {
        DeliveryEntity deliveryOrderEntity = deliveryRepository.findById(request.getId()).orElse(null);
        DeliveryStatusEntity status = new DeliveryStatusEntity();
        status.setStatus(request.getStatus());
        status.setNote(request.getNote());
        status.setCreateDate(new Date());
        status.setDeliveryEntity(deliveryOrderEntity);
        return deliveryStatusRepository.save(status);
    }

    @Override
    public Long deliveryCount() {
        return deliveryRepository.deliveryCount().stream().mapToLong(value -> value).sum();
    }

    @Override
    public List<ContactFormDeliveryEntity> findAllByUserAndType() {
        return null;
    }

    @Override
    public Map<String,String> getStatus() {
        Map status = new HashMap();
        status.put("PROCESSING","??ang x??? l??");
        status.put("PACKING","??ang ????ng g??i");
        status.put("SHIPPING","??ang v???n chuy???n");
        status.put("AWAITCROSSEDBORDER","Ch??? th??ng quan");
        status.put("CROSSEDBORDER","???? th??ng quan");
        status.put("INLANDSHIPPING","V???n chuy???n n???i ?????a");
        status.put("SUCCESS","???? ho??n th??nh");
        return status;
    }

    @Override
    public DeliveryOrderEntity findOrderById(Integer id) {
        return deliveryOrderRepository.findById(id).orElse(null);
    }


    @Override
    public DeliveryOrderEntity findOrderById(String token, Integer id) {
        UserDeliveryEntity userDeliveryEntity = getUserDelivery(token);

        return deliveryOrderRepository.findByIdAndUserDeliveryEntity_Id(id,userDeliveryEntity.getId());
    }

    @Override
    public Page<DeliveryOrderEntity> findAllOrder(Pageable pageable) {
        return deliveryOrderRepository.findAll(pageable);
    }

    @Override
    public List getDeliveryOrder(String token) {
        UserDeliveryEntity userDeliveryEntity = getUserDelivery(token);
        if (userDeliveryEntity!=null){
            Pageable pageable = PageRequest.of(0,1000, Sort.by(Sort.Direction.DESC,"id"));
            return  deliveryOrderRepository.findAllByUserDeliveryEntity_Id(userDeliveryEntity.getId(),pageable);
        }

        return null;
    }

    public UserDeliveryEntity getUserDelivery(String token){
        String userName = jwtTokenProvider.getUserIdFromJWT(token);
        if (userName==null ) return null;
        UserDeliveryEntity userEntity = userDeliveryRepository.findByUserName(userName);
        return userEntity;
    }
}
