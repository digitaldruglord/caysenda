package com.nomi.caysenda.services.impl;

import com.google.common.hash.Hashing;
import com.nomi.caysenda.entity.OrderDetailtEntity;
import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.entity.UserEntity;
import com.nomi.caysenda.entity.delivery.DeliveryCurrencyEntity;
import com.nomi.caysenda.entity.delivery.DeliveryEntity;
import com.nomi.caysenda.entity.delivery.DeliveryOrderEntity;
import com.nomi.caysenda.options.model.EmailSetting;
import com.nomi.caysenda.options.model.WebsiteInfo;
import com.nomi.caysenda.services.MyMailSender;
import com.nomi.caysenda.services.OrderService;
import com.nomi.caysenda.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyMailSenderImpl implements MyMailSender {
    @Autowired
    JavaMailSenderImpl javaMailSender;
    @Autowired
    MailSender mailSender;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    private Environment env;
    @Autowired
    OrderService orderService;
    @Autowired
    SettingService settingService;
    @Autowired
    TaskScheduler taskScheduler;
    @Override
    public void send(MimeMessageHelper messageHelper) {
        javaMailSender.send(messageHelper.getMimeMessage());
    }

    @Override
    public void send(SimpleMailMessage mailMessage) {
        mailSender.send(mailMessage);
    }

    @Override
    public void send(SimpleMailMessage... mailMessage) {
        mailSender.send(mailMessage);
    }

    @Override
    public void send(MimeMessage... mailMessage) {
        javaMailSender.send(mailMessage);
    }

    @Override
    public void send(MimeMessage mailMessage) {
        javaMailSender.send(mailMessage);
    }

    @Override
    public MimeMessageHelper getMimeMessageHelper() {
        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(getMimeMessage(),true,"UTF-8");
        } catch (MessagingException e) {

        }
        return messageHelper;
    }

    @Override
    public MimeMessage getMimeMessage() {
        return javaMailSender.createMimeMessage();
    }

    @Override
    public String payment(OrderEntity orderEntity) {
        List<String> ignore = List.of("purchased","waitingreceived","deliveredall","customerreceived");
        String html = "";
        if (!ignore.contains(orderEntity.getStatus())){
            List<OrderDetailtEntity> detailts = orderService.findAllDetailtByOrderId(orderEntity.getId());
            EmailSetting emailSetting = settingService.getEmail();
            WebsiteInfo websiteInfo = settingService.getWebsite();
            String content = String.valueOf(emailSetting.getContents().get(orderEntity.getStatus()));
            Context context = new Context();
            context.setVariable("order",orderEntity);
            context.setVariable("detailts",detailts);
            context.setVariable("status",orderService.getStatus().get(orderEntity.getStatus()));
            context.setVariable("fulladdress",orderService.getFullAddress(orderEntity.getBillingAddress(),orderEntity.getBillingCity(),orderEntity.getBillingDistrict(),orderEntity.getBillingWards()));
            context.setVariable("content",content);
            context.setVariable("domain",env.getProperty("spring.domain"));
            context.setVariable("website",websiteInfo);
            context.setVariable("method",orderService.getMethod().get(orderEntity.getMethod()));
            context.setVariable("title",emailSetting.getTitle()!=null?emailSetting.getTitle():"Cảm ơn đã đặt hàng");
            html = templateEngine.process("email/email-template",context);
            sendHtmlMail("Đơn hàng #"+orderEntity.getId()+" - Trạng thái: "+orderService.getStatus().get(orderEntity.getStatus())+" - "+emailSetting.getTitle(),emailSetting.getEmail(),html);
            sendHtmlMail("Đơn hàng #"+orderEntity.getId()+" - Trạng thái: "+orderService.getStatus().get(orderEntity.getStatus())+" - "+emailSetting.getTitle(),orderEntity.getBillingEmail(),html);

        }
        return html;
    }

    @Override
    public void sendEmailForgotPassword(UserEntity userEntity) {
        WebsiteInfo websiteInfo = settingService.getWebsite();
        String domain = env.getProperty("spring.domain");
        Context context = new Context();
        Date date = new Date();
        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append(userEntity.getId());
        dataBuilder.append(date.getTime());
        String hash = Hashing.hmacSha256("FORGOT_EMAIL".getBytes(StandardCharsets.UTF_8)).newHasher().putBytes(dataBuilder.toString().getBytes(StandardCharsets.UTF_8)).hash().toString();
        String url = domain+"/khoi-phuc-mat-khau"+"?id="+userEntity.getId()+"&hash="+hash+"&time="+date.getTime();
        context.setVariable("website",websiteInfo);
        context.setVariable("domain",domain);
        context.setVariable("url",url);
        String html = templateEngine.process("email/email-forgot-password",context);
        sendHtmlMail("Khôi phục tài khoản",userEntity.getEmail(),html);
    }

    @Override
    public void sendEmailDeliveryOrder(DeliveryOrderEntity orderEntity) {
        Map<String,Object> setting = settingService.delivery();
        WebsiteInfo websiteInfo = settingService.getWebsite();
        EmailSetting emailSetting = settingService.getEmail();
        Map<String,Object> website = (Map<String, Object>) setting.get("website");
        Context context = new Context();
        context.setVariable("website",website);
        context.setVariable("order",orderEntity);
        String html = templateEngine.process("email/delivery-order",context);
        sendHtmlMail("Đơn hàng "+website.get("name"),orderEntity.getUserDeliveryEntity().getUserName(),html);
        sendHtmlMail("Đơn hàng "+website.get("name"),emailSetting.getEmail(),html);
    }

    @Override
    public void sendEmailDelivery(DeliveryEntity orderEntity) {
        Map<String,Object> setting = settingService.delivery();
        WebsiteInfo websiteInfo = settingService.getWebsite();
        EmailSetting emailSetting = settingService.getEmail();
        Map<String,Object> website = (Map<String, Object>) setting.get("website");
        Context context = new Context();
        context.setVariable("website",website);
        context.setVariable("data",orderEntity);
        String html = templateEngine.process("email/delivery",context);
        sendHtmlMail("Đơn hàng "+website.get("name"),orderEntity.getUserDeliveryEntity().getUserName(),html);
        sendHtmlMail("Đơn hàng "+website.get("name"),emailSetting.getEmail(),html);
    }

    @Override
    public void sendEmailDeliveryCurrency(DeliveryCurrencyEntity orderEntity) {
        Map<String,Object> setting = settingService.delivery();
        WebsiteInfo websiteInfo = settingService.getWebsite();
        EmailSetting emailSetting = settingService.getEmail();
        Map<String,Object> website = (Map<String, Object>) setting.get("website");
        Context context = new Context();
        context.setVariable("website",website);
        context.setVariable("data",orderEntity);
        String html = templateEngine.process("email/delivery-currency",context);
        sendHtmlMail("Đơn hàng "+website.get("name"),orderEntity.getUserDeliveryEntity().getUserName(),html);
        sendHtmlMail("Đơn hàng "+website.get("name"),emailSetting.getEmail(),html);
    }

    public void sendHtmlMail(String title, String to, String html) {
        MimeMessageHelper messageHelper = getMimeMessageHelper();
        Map<String, Object> map = new HashMap<>();
        taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    if (to!=null && !to.equals("")){
                        messageHelper.setSubject(title);
                        messageHelper.setTo(to);
                        messageHelper.setText(html,true);
                        javaMailSender.send(messageHelper.getMimeMessage());
                    }

                } catch (MessagingException e) {

                }
            }
        },new Date());

    }
}
