package com.nomi.caysenda.repositories.custom;

import com.nomi.caysenda.dto.cart.CartSummary;
import com.nomi.caysenda.entity.CartEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartCustomRepository {
    CartEntity update(CartEntity cartEntity);
    Long countStatictisCart();
    Page<CartSummary> statictisCart(String sort,String keyword,Pageable pageable);
}
