package com.nomi.caysenda.controller.impl;

import com.nomi.caysenda.controller.PrintController;
import com.nomi.caysenda.dto.PrintOrderDTO;
import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/print")
public class PrintControllerImpl implements PrintController {
    @Autowired
    OrderService orderService;

    @Override
    public ModelAndView print(List<Integer> ids) {
        ModelAndView view = new ModelAndView("print/print");
        List<PrintOrderDTO> orders = orderService.getDataPrint(ids);
        orders.get(0).getDetailts().get(0).getTopFlag().equals("1");
        view.addObject("orders",orders);
        return view;
    }
}
