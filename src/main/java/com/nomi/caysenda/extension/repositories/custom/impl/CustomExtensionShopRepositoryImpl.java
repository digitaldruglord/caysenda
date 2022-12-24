package com.nomi.caysenda.extension.repositories.custom.impl;

import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.entity.UserEntity;
import com.nomi.caysenda.extension.dto.ShopExtensionDTO;
import com.nomi.caysenda.extension.entity.ExtensionProductEntity;
import com.nomi.caysenda.extension.entity.ExtensionShopEntity;
import com.nomi.caysenda.extension.repositories.custom.CustomExtensionShopRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CustomExtensionShopRepositoryImpl implements CustomExtensionShopRepository {
    @Autowired EntityManager entityManager;
    @Override
    public Long countAllCriteria(String keyword, Integer userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<ExtensionShopEntity> root = criteriaQuery.from(ExtensionShopEntity.class);
        Join<ExtensionShopEntity,UserEntity> userRoot = root.join("userExtensionShop",JoinType.LEFT);
        /** where */
        List<Predicate> predicates = new ArrayList<>();
        if (keyword!=null) {
            if (NumberUtils.isDigits(keyword)){
                predicates.add(
                        builder.or(
                                builder.equal(root.get("id"),keyword)
                        )
                );
            }else {
                predicates.add(
                        builder.or(
                                builder.like(root.get("name"),"%"+keyword+"%"),
                                builder.like(root.get("link"),"%"+keyword+"%"),
                                builder.like(root.get("mainProduct"),"%"+keyword+"%"),
                                builder.like(root.get("status"),"%"+keyword+"%"),
                                builder.like(userRoot.get("username"),"%"+keyword+"%")
                        )
                );
            }
        }

        if (userId!=null) predicates.add(builder.equal(userRoot.get("id"),userId));
        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        /** select */
        criteriaQuery.select(builder.count(root));
        /** execute */
        Query query = entityManager.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }

    @Override
    public Page<ShopExtensionDTO> findAllCriteria(String keyword,Integer userId, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ShopExtensionDTO> criteriaQuery = builder.createQuery(ShopExtensionDTO.class);
        Root<ExtensionShopEntity> root = criteriaQuery.from(ExtensionShopEntity.class);
        Join<ExtensionShopEntity, ExtensionProductEntity> productRoot = root.join("products",JoinType.LEFT);
        Join<ExtensionShopEntity, UserEntity> userRoot = root.join("userExtensionShop",JoinType.LEFT);
        /** selections*/
        Selection id = root.get("id");
        Selection name = root.get("name");
        Selection link = root.get("link");
        Selection sku = root.get("sku");
        Selection exchangeRate = root.get("exchangeRate");
        Selection factorDefault = root.get("factorDefault");
        Selection factor1 = root.get("factor1");
        Selection factor2 = root.get("factor2");
        Selection factor3 = root.get("factor3");
        Selection factor4 = root.get("factor4");
        Selection mainProduct = root.get("mainProduct");
        Selection status = root.get("status");
        Selection enableUpdatePrice = root.get("enableUpdatePrice");
        Selection userExtensionShop = userRoot;
        Selection count = builder.count(productRoot);
        List<Selection> selections = List.of(id,name,link,sku,exchangeRate,factorDefault,factor1,factor2,factor3,factor4,mainProduct,status,userExtensionShop,count,enableUpdatePrice);
        criteriaQuery.multiselect(selections.toArray(new Selection[selections.size()]));
        /** where */
        List<Predicate> predicates = new ArrayList<>();

        if (keyword!=null){
            if (NumberUtils.isDigits(keyword)){
                predicates.add(
                        builder.or(
                                builder.equal(root.get("id"),keyword)
                        )
                );
            }else {
                predicates.add(
                        builder.or(
                                builder.like(root.get("name"),"%"+keyword+"%"),
                                builder.like(root.get("link"),"%"+keyword+"%"),
                                builder.like(root.get("mainProduct"),"%"+keyword+"%"),
                                builder.like(root.get("status"),"%"+keyword+"%"),
                                builder.like(userRoot.get("username"),"%"+keyword+"%")
                        )
                );
            }

        }
        if (userId!=null) predicates.add(builder.equal(userRoot.get("id"),userId));
        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        /** group by */
        criteriaQuery.groupBy(root.get("id"));
        /** order by*/
        List<Order> orders = new ArrayList<>();
        pageable.getSort().toList().forEach(order -> {
            if (order.isDescending()){
                orders.add(builder.desc(root.get(order.getProperty())));
            }else {
                orders.add(builder.asc(root.get(order.getProperty())));
            }
        });
        if (orders.size()>0){
            criteriaQuery.orderBy(orders.toArray(new Order[orders.size()]));
        }
        /** execute */
        Query query = entityManager.createQuery(criteriaQuery);
        /** pagination */
        if (pageable!=null){
            query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }
        return new PageImpl<>(query.getResultList(),pageable,countAllCriteria(keyword, userId));
    }
}
