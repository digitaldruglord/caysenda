package com.nomi.caysenda.services;


import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.entity.UserEntity;
import com.nomi.caysenda.entity.delivery.DeliveryCurrencyEntity;
import com.nomi.caysenda.entity.delivery.DeliveryEntity;
import com.nomi.caysenda.entity.delivery.DeliveryOrderEntity;
import com.nomi.caysenda.repositories.delivery.DeliveryOrderRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.servlet.ViewResolver;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface MyMailSender {
    void send(MimeMessageHelper messageHelper);
    void send(SimpleMailMessage mailMessage);
    void send(SimpleMailMessage... mailMessage);
    void send(MimeMessage... mailMessage);
    void send(MimeMessage mailMessage);
    MimeMessageHelper getMimeMessageHelper();
    MimeMessage getMimeMessage();
    String payment(OrderEntity orderEntity);
    void sendEmailForgotPassword(UserEntity userEntity);
    void sendEmailDeliveryOrder(DeliveryOrderEntity orderEntity);
    void sendEmailDelivery(DeliveryEntity orderEntity);
    void sendEmailDeliveryCurrency(DeliveryCurrencyEntity orderEntity);
}
