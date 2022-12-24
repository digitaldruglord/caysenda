package com.nomi.caysenda.repositories.custom.impl;

import com.nomi.caysenda.dto.cart.CartSummary;
import com.nomi.caysenda.entity.*;
import com.nomi.caysenda.repositories.custom.CartCustomRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CartCustomRepositoryImpl implements CartCustomRepository {
    @Autowired
    EntityManager entityManager;
    @Autowired
    Environment env;
    @Override
    public CartEntity update(CartEntity cartEntity) {
        Session session =  entityManager.unwrap(Session.class);
        session.update(cartEntity);
        return cartEntity;
    }

    @Override
    public Long countStatictisCart() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<CartEntity> root = criteriaQuery.from(CartEntity.class);
        Join<CartEntity, CartProductEntity> productRoot = root.join("products");
        criteriaQuery.select(criteriaBuilder.count(root));
        /** where */

        /** group by*/
        criteriaQuery.groupBy(root);

        Query query = entityManager.createQuery(criteriaQuery);
        return query.getResultList().stream().count();
    }

    @Override
    public Page<CartSummary> statictisCart(String sort, String keyword, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(CartSummary.class);
        Root<CartEntity> root = criteriaQuery.from(CartEntity.class);
        Join<CartEntity, CartProductEntity> productRoot = root.join("products");
        Join<CartProductEntity, CartVariantEntity> variantRoot = productRoot.join("variants");
        Join<CartVariantEntity,VariantEntity> variant = variantRoot.join("cartVariantEntity");
        Join<CartEntity, UserEntity> userRoot = root.join("userCart");
        List<Selection> selects = new ArrayList<>();
        Selection id = root.get("id");
        Selection name = userRoot.get("username");
        Selection cosst = criteriaBuilder.sum(criteriaBuilder.prod(variantRoot.get("quantity"),variant.get("cost")));
        Selection note = root.get("note");
        Selection userId = userRoot.get("id");
        Selection createDate = root.get("createDate");
        Selection modifiedDate = root.get("modifiedDate");
        Selection phone = userRoot.get("phonenumber");
        selects.addAll(List.of(id,name,cosst,note,userId,createDate,modifiedDate,phone));
        criteriaQuery.multiselect(selects);
        /** where */
        if (keyword!=null && !keyword.equals("")){
            criteriaQuery.where(criteriaBuilder.or(
                    criteriaBuilder.like(userRoot.get("email"),"%"+keyword+"%"),
                    criteriaBuilder.like(userRoot.get("phonenumber"),"%"+keyword+"%"),
                    criteriaBuilder.like(userRoot.get("fullName"),"%"+keyword+"%")

            ));
        }
//        criteriaQuery.where(criteriaBuilder.equal(hostRoot.get("host"),env.getProperty("spring.domain")));
        /** group by*/
        criteriaQuery.groupBy(root);

        /** order*/
        if (sort!=null && sort.equals("cost")){
          criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.sum(criteriaBuilder.prod(variantRoot.get("quantity"),variant.get("cost")))));
        }else {
            criteriaQuery.orderBy(List.of(criteriaBuilder.desc(root.get("createDate")),criteriaBuilder.desc(root.get("modifiedDate"))));
        }
        /** pagination */
        Query query = entityManager.createQuery(criteriaQuery);
        if (pageable!=null){
            query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        return new PageImpl<CartSummary>(query.getResultList(),pageable,countStatictisCart());
    }
}
