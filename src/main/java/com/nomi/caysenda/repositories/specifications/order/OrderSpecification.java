package com.nomi.caysenda.repositories.specifications.order;

import com.nomi.caysenda.entity.OrderEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class OrderSpecification {
    public static Specification<OrderEntity> findAllForAdmin(){
        return new Specification<OrderEntity>() {
            @Override
            public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                return null;
            }
        };
    }
}
