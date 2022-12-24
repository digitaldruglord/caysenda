package com.nomi.caysenda.repositories.custom.impl;

import com.nomi.caysenda.dto.CategoryCountDTO;
import com.nomi.caysenda.dto.OrderAdminDTO;
import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.entity.ProviderEntity;
import com.nomi.caysenda.repositories.custom.CategoryCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {
    @Autowired
    EntityManager entityManager;
    @Autowired
    Environment env;

    @Override
    public List<CategoryCountDTO> findAllAndCount() {
        String domain = env.getProperty("spring.domain");
        Query query = entityManager.createQuery("SELECT new com.nomi.caysenda.dto.CategoryCountDTO(c.id,c.name,c.slug,c.thumbnail,c.banner,c.parent,COUNT (p)) FROM CategoryEntity c LEFT JOIN c.products p LEFT JOIN p.providers provider WHERE provider.host=:domain GROUP BY c");
        query.setParameter("domain",domain);
        return query.getResultList();
    }

    @Override
    public Long countAllCriteria(String name, Integer host) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<CategoryEntity> routeRoot = criteriaQuery.from(CategoryEntity.class);
        Join<CategoryEntity, ProviderEntity> provider = routeRoot.join("categoryProvider",JoinType.LEFT);
        /** where */
        List<Predicate> predicates = new ArrayList<>();
        if (name!=null) predicates.add(builder.equal(routeRoot.get("name"),name));
        if (host!=null) predicates.add(builder.equal(provider.get("id"),host));
        if (predicates.size()>0){
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        criteriaQuery.select(routeRoot.get("id"));
        criteriaQuery.groupBy(routeRoot.get("id"));
        Query query = entityManager.createQuery(criteriaQuery);

        return Long.valueOf(query.getResultList().size());
    }

    @Override
    public Page<CategoryEntity> findAllCriteria(String name, Integer host, Pageable pageable) {
        if (pageable==null)  pageable =  PageRequest.of(0,20, Sort.by(Sort.Direction.DESC,"id"));
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryEntity> criteriaQuery = builder.createQuery(CategoryEntity.class);
        Root<CategoryEntity> routeRoot = criteriaQuery.from(CategoryEntity.class);
        Join<CategoryEntity, ProviderEntity> provider = routeRoot.join("categoryProvider",JoinType.LEFT);
        /** where */
        List<Predicate> predicates = new ArrayList<>();
        if (name!=null && !name.equals("")) predicates.add(builder.like(routeRoot.get("name"),"%"+name+"%"));
        if (host!=null) predicates.add(builder.equal(provider.get("id"),host));
        if (predicates.size()>0){
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        /** order by */
        List<Order> orders = new ArrayList<>();
        if (pageable.getSort()!=null){
            pageable.getSort().toList().forEach(order -> {
                if (order.isDescending()){
                    orders.add(builder.desc(routeRoot.get(order.getProperty())));
                }else {
                    orders.add(builder.asc(routeRoot.get(order.getProperty())));
                }
            });
        }
        if (orders.size()>0){
            criteriaQuery.orderBy(orders.toArray(new Order[orders.size()]));
        }
        /** group by */
        criteriaQuery.groupBy(routeRoot.get("id"));

        Query query = entityManager.createQuery(criteriaQuery);
        /** pagination */
        query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList(),pageable,countAllCriteria(name,host));
    }
}
