package com.nomi.caysenda.repositories.custom;

import com.nomi.caysenda.dto.OrderAdminDTO;
import com.nomi.caysenda.dto.ReportOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface OrderCustomRepository {
    Long countAllCriteria(String status, Boolean trash, Integer host, String keyword);
    Page<OrderAdminDTO> findAllCriteria(String status, Boolean trash, Integer host, String keyword,Date from,Date to, Pageable pageable);
    List<ReportOrderDTO> statisticByArea(Date from,Date to,Integer host,String status);
}
