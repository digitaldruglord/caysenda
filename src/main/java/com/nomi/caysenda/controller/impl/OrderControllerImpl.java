package com.nomi.caysenda.controller.impl;

import com.nomi.caysenda.controller.OrderController;
import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/don-hang")
public class OrderControllerImpl implements OrderController {
    @Autowired
    OrderService orderService;
    @Override
    public ModelAndView getOrder(Integer id) {
        ModelAndView view = new ModelAndView("order/index");
        OrderEntity orderEntity = orderService.findById(id);
        if (orderEntity!=null){
            if (orderEntity.getCreateBy()==null){
                view.addObject("order",orderService.findById(id));
                view.addObject("detailts",orderService.findAllDetailtByOrderId(id));
            }else {
                view.setViewName("redirect:/");
            }
        }else {
            view.setViewName("redirect:/");
        }


        return view;
    }
}
