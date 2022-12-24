package com.nomi.caysenda.repositories.custom.impl;

import com.nomi.caysenda.dto.OrderAdminDTO;
import com.nomi.caysenda.dto.ReportOrderDTO;
import com.nomi.caysenda.entity.OrderDetailtEntity;
import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.entity.ProviderEntity;
import com.nomi.caysenda.entity.TrackingOrderEntity;
import com.nomi.caysenda.ghn.entity.GHNOrderEntity;
import com.nomi.caysenda.repositories.TrackingOrderRepository;
import com.nomi.caysenda.repositories.custom.OrderCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class OrderCustomRepositoryImpl implements OrderCustomRepository {
    @Autowired
    EntityManager entityManager;
    @Autowired
    TrackingOrderRepository trackingOrderRepository;


    @Override
    public Long countAllCriteria(String status, Boolean trash, Integer host, String keyword) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<OrderEntity> routeRoot = criteriaQuery.from(OrderEntity.class);
        Join<OrderEntity, ProviderEntity> partner = routeRoot.join("providerOrder",JoinType.LEFT);
        criteriaQuery.select(builder.count(routeRoot.get("id")));
        /** where */
        List<Predicate> predicates = new ArrayList<>();
        if (status!=null && !status.equals("incomplate")){
            if (status.equalsIgnoreCase("trash")){
                predicates.add(builder.equal(routeRoot.get("trash"),true));
            }else if (status.equalsIgnoreCase("all")){
                predicates.add(builder.equal(routeRoot.get("trash"),false));
            }else if (status.equalsIgnoreCase("tracking")){
                List<Integer> ids = searchFromTracking(keyword,new Date(),new Date(),null);
                predicates.add(builder.in(routeRoot.get("id")).value(ids));
                predicates.add(builder.equal(routeRoot.get("trash"),false));
            }else if (status.equalsIgnoreCase("ghn")){
                List<Integer> ids = searchByGHN(keyword,null);
                predicates.add(builder.in(routeRoot.get("id")).value(ids));
                predicates.add(builder.equal(routeRoot.get("trash"),false));
            }else {
                predicates.add(builder.equal(routeRoot.get("status"),status));
                predicates.add(builder.equal(routeRoot.get("trash"),false));
            }
        }else {
            predicates.add(builder.notEqual(routeRoot.get("status"),"success"));
            predicates.add(builder.equal(routeRoot.get("trash"),false));
        }

        if (host!=null) predicates.add(builder.equal(partner.get("id"),host));
        if (status!=null && status.equalsIgnoreCase("tracking")){
        }else {
            if (keyword!=null) {
                predicates.add(builder.or(
                        builder.like(routeRoot.get("billingFullName"),"%"+keyword+"%"),
                        builder.like(routeRoot.get("billingEmail"),"%"+keyword+"%"),
                        builder.like(routeRoot.get("billingPhoneNumber"),"%"+keyword+"%"),
                        builder.like(routeRoot.get("adminNote"),"%"+keyword+"%")
                ));
            }
        }
        if (predicates.size()>0){
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }

        /** group by*/
        criteriaQuery.groupBy(routeRoot.get("id"));
        return Long.valueOf(entityManager.createQuery(criteriaQuery).getResultList().size());
    }

    private List<Integer> searchFromTracking(String keyword,Date from,Date to,Pageable pageable){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = builder.createQuery(Integer.class);
        Root<TrackingOrderEntity> routeRoot = criteriaQuery.from(TrackingOrderEntity.class);
        Join<TrackingOrderEntity,OrderEntity> orderEntity = routeRoot.join("orderTracking",JoinType.LEFT);
        criteriaQuery.distinct(true);
        List<Predicate> predicates = new ArrayList<>();
        if (keyword!=null){
           predicates.add(builder.or(
                   builder.like(routeRoot.get("orderCode"),"%"+keyword+"%"),
                   builder.like(routeRoot.get("ladingCode"),"%"+keyword+"%")
           ));
        }

        if (from!=null && to!=null){
            predicates.add(builder.greaterThanOrEqualTo( routeRoot.get("dateOrder"),from));
            predicates.add(builder.lessThanOrEqualTo( routeRoot.get("dateOrder"),to));
        }
        if (predicates.size()>0){
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        criteriaQuery.select(orderEntity.get("id"));
        Query query = entityManager.createQuery(criteriaQuery);
        if (pageable!=null){
            query.setFirstResult(pageable.getPageNumber()* pageable.getPageNumber());
            query.setMaxResults(pageable.getPageSize());
        }
        return query.getResultList();
    }
    private List<Integer> searchByGHN(String keyword,Pageable pageable){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = builder.createQuery(Integer.class);
        Root<GHNOrderEntity> routeRoot = criteriaQuery.from(GHNOrderEntity.class);
        criteriaQuery.distinct(true);
        List<Predicate> predicates = new ArrayList<>();
        if (keyword!=null){
            predicates.add(builder.or(
                    builder.like(routeRoot.get("orderCodeGhn"),"%"+keyword+"%"),
                    builder.like(routeRoot.get("phoneNumber"),"%"+keyword+"%")
            ));
        }
        if (predicates.size()>0){
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        criteriaQuery.select(routeRoot.get("orderId"));
        Query query = entityManager.createQuery(criteriaQuery);
        if (pageable!=null){
            query.setFirstResult(pageable.getPageNumber()* pageable.getPageNumber());
            query.setMaxResults(pageable.getPageSize());
        }
        return query.getResultList();

    }
    @Override
    public Page<OrderAdminDTO> findAllCriteria(String status, Boolean trash, Integer host, String keyword, Date from, Date to, Pageable pageable) {
        if (pageable==null)  pageable =  PageRequest.of(0,20, Sort.by(Sort.Direction.DESC,"id"));
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderAdminDTO> criteriaQuery = builder.createQuery(OrderAdminDTO.class);
        Root<OrderEntity> routeRoot = criteriaQuery.from(OrderEntity.class);
        Join<OrderEntity, ProviderEntity> partner = routeRoot.join("providerOrder",JoinType.LEFT);
        List<Selection> selections = new ArrayList<>();
        Selection id = routeRoot.get("id").alias("id");
        Selection billingFullName = routeRoot.get("billingFullName").alias("billingFullName");
        Selection billingPhoneNumber = routeRoot.get("billingPhoneNumber").alias("billingPhoneNumber");
        Selection statusSelection = routeRoot.get("status").alias("status");
        Selection method = routeRoot.get("method").alias("method");
        Selection productAmount = routeRoot.get("productAmount").alias("productAmount");
        Selection orderAmount = routeRoot.get("orderAmount").alias("orderAmount");
        Selection ship = routeRoot.get("ship").alias("ship");
        Selection cost = routeRoot.get("cost").alias("cost");
        Selection billingEmail = routeRoot.get("billingEmail").alias("billingEmail");
        Selection billingAddress = routeRoot.get("billingAddress").alias("billingAddress");
        Selection billingCity = routeRoot.get("billingCity").alias("billingCity");
        Selection billingDistrict = routeRoot.get("billingDistrict").alias("billingDistrict");
        Selection billingWards = routeRoot.get("billingWards").alias("billingWards");
        Selection createDate = routeRoot.get("createDate").alias("createDate");
        Selection incurredCost = routeRoot.get("incurredCost").alias("incurredCost");
        Selection paid = routeRoot.get("paid").alias("paid");
        Selection providerName = partner.get("providerName").alias("providerName");
        Selection orderCode = routeRoot.get("orderCode").alias("orderCode");
        Selection note = routeRoot.get("note").alias("note");
        Selection adminNote = routeRoot.get("adminNote").alias("adminNote");
        Selection cashflowstatus = routeRoot.get("cashflowstatus").alias("cashflowstatus");
        selections.addAll(List.of(id,billingFullName,billingPhoneNumber,statusSelection,method,productAmount,orderAmount,ship,
                cost,billingEmail,billingAddress,billingCity,billingDistrict,billingWards,createDate,incurredCost,paid,providerName,orderCode,note,adminNote,cashflowstatus));
        criteriaQuery.multiselect( selections.toArray(new Selection[selections.size()]));
        /** where */
        List<Predicate> predicates = new ArrayList<>();
        if (status!=null && !status.equals("incomplate")){
            if (status.equalsIgnoreCase("trash")){
                predicates.add(builder.equal(routeRoot.get("trash"),true));
            }else if (status.equalsIgnoreCase("all")){
                predicates.add(builder.equal(routeRoot.get("trash"),false));
            }else if (status.equalsIgnoreCase("tracking")){
                List<Integer> ids = searchFromTracking(keyword,from,to,pageable);

                predicates.add(builder.in(routeRoot.get("id")).value(ids));
                predicates.add(builder.equal(routeRoot.get("trash"),false));
            }else if (status.equalsIgnoreCase("ghn")){
                List<Integer> ids = searchByGHN(keyword,pageable);
                if (ids.size()>0){
                    predicates.add(builder.in(routeRoot.get("id")).value(ids));
                    predicates.add(builder.equal(routeRoot.get("trash"),false));
                }
            }else {
                predicates.add(builder.equal(routeRoot.get("status"),status));
                predicates.add(builder.equal(routeRoot.get("trash"),false));
            }
        }else {
            predicates.add(builder.notEqual(routeRoot.get("status"),"success"));
            predicates.add(builder.equal(routeRoot.get("trash"),false));
        }
        if (host!=null) predicates.add(builder.equal(partner.get("id"),host));
        if (status!=null && status.equalsIgnoreCase("tracking")){

        }else {
            if (keyword!=null) {
                predicates.add(builder.or(
                        builder.like(routeRoot.get("billingFullName"),"%"+keyword+"%"),
                        builder.like(routeRoot.get("billingEmail"),"%"+keyword+"%"),
                        builder.like(routeRoot.get("billingPhoneNumber"),"%"+keyword+"%"),
                        builder.like(routeRoot.get("adminNote"),"%"+keyword+"%")
                ));
            }
            if (from!=null && to!=null){
                predicates.add(builder.greaterThanOrEqualTo( routeRoot.get("createDate"),from));
                predicates.add(builder.lessThanOrEqualTo( routeRoot.get("createDate"),to));
            }
        }

        if (predicates.size()>0){
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        /** order */
        if (pageable!=null && pageable.getSort()!=null){
            List<Order> orders = new ArrayList<>();
            for (Sort.Order order:pageable.getSort().toList()){
                if (order.getDirection().isAscending()){
                    orders.add(builder.asc(routeRoot.get(order.getProperty())));
                }else {
                    orders.add(builder.desc(routeRoot.get(order.getProperty())));
                }
            }
            criteriaQuery.orderBy(orders);
        }
        /** group by*/
        criteriaQuery.groupBy(routeRoot.get("id"));

        /** pagination */
        Query query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<OrderAdminDTO> list = query.getResultList();
        return new PageImpl<>(list,pageable,countAllCriteria(status, trash, host, keyword));
    }

    @Override
    public List<ReportOrderDTO> statisticByArea(Date from, Date to, Integer host, String status) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ReportOrderDTO> criteriaQuery = builder.createQuery(ReportOrderDTO.class);
        Root<OrderEntity> routeRoot = criteriaQuery.from(OrderEntity.class);
        Join<OrderEntity,ProviderEntity> providerJoin = routeRoot.join("providerOrder");

        /** selections */
        Selection netRevenue = builder.sum(routeRoot.get("productAmount")).alias("netRevenue");
        Selection grossRevenue = builder.sum(routeRoot.get("orderAmount")).alias("grossRevenue");
        Selection cost = builder.sum(routeRoot.get("cost")).alias("cost");
        Selection ship = builder.sum(routeRoot.get("ship")).alias("ship");
        Selection incurredCost = builder.sum(routeRoot.get("incurredCost")).alias("incurredCost");
        Selection province = routeRoot.get("billingCity").alias("province");
        List<Selection> selections = List.of(netRevenue,grossRevenue,cost,ship,incurredCost,province);
        criteriaQuery.multiselect(selections.toArray(new Selection[selections.size()]));
        /** where */
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.greaterThanOrEqualTo(routeRoot.get("createDate"),from));
        predicates.add(builder.lessThanOrEqualTo(routeRoot.get("createDate"),to));;
        predicates.add(builder.equal(routeRoot.get("trash"),false));
        if (host!=null && !host.equals("")){
            predicates.add(builder.equal(providerJoin.get("id"),host));
        }
        if (status!=null && !status.equals("")) predicates.add(builder.equal(routeRoot.get("status"),status));
        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        /** order */
        criteriaQuery.orderBy(builder.desc(builder.sum(routeRoot.get("productAmount"))));
        /** group */
        criteriaQuery.groupBy(routeRoot.get("billingCity"));
        /** result */
        Query query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }
}
