package com.nomi.caysenda.repositories.custom.impl;

import com.nomi.caysenda.dto.ProductDTO;
import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.entity.ProviderEntity;
import com.nomi.caysenda.entity.VariantEntity;
import com.nomi.caysenda.repositories.custom.ProductCustomRepository;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;

;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.query.Query;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    @Autowired
    EntityManager entityManager;
    @Autowired
    private Environment env;

    @Override
    public Long countFindAllForProductDTO(Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);
        Join<ProductEntity,VariantEntity> rootVariant = root.join("variants");
        Join<ProductEntity, CategoryEntity> rootCategory = root.join("categories");
        Join<ProductEntity, ProviderEntity> rootProvider = root.join("providers");
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("categoryDefault"),rootCategory.get("id")));
        String domain = env.getProperty("spring.domain");
        predicates.add(criteriaBuilder.equal(rootProvider.get("host"),domain));
        criteriaQuery.groupBy(root.get("id"));
        criteriaQuery.select(criteriaBuilder.count(root));
        Session session = entityManager.unwrap(Session.class);
        enableFilter(session);
        Query query = session.createQuery(criteriaQuery);
        return  query.getResultList().stream().count();

    }

    @Override
    @Transactional
    public Page<ProductDTO> search(String keyword, String catSlug,String host, String sorter, Pageable pageable) {
        Session session =  entityManager.unwrap(Session.class);
        Integer loop = 0;
        while (true){
            StringBuilder sql = new StringBuilder("SELECT new com.nomi.caysenda.dto.ProductDTO(p.id,p.nameVi,p.slugVi,p.skuVi,min (v.price) as minPrice,max(v.price) as maxPrice,min (v.vip1) as minv1,min (v.vip2) as minv2,min (v.vip3) as minv3,min (v.vip4) as minv4,p.thumbnail,c.name,c.slug,p.sold,p.quickviewGallery,p.unit,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.topFlag) FROM ");
            sql.append(" ProductEntity ");
            if (catSlug!=null){
                sql.append(" p LEFT JOIN p.variants v " +
								"LEFT JOIN p.categories c " +
								"LEFT JOIN p.providers provider ");
				if (catSlug.equals("hot-product")) {
					sql.append(" WHERE (p.categoryDefault=c.id " +
							"AND provider.host=:host " +
							"AND LOWER(p.keyword) LIKE CONCAT('%',CONVERT(LOWER(:keyword),BINARY),'%') " +
							"AND p.topFlag = '1'" +
							"GROUP BY p.id");
				} else {
					if (loop.equals(0)) {
						sql.append(" WHERE (p.categoryDefault=c.id " +
								"AND provider.host=:host " +
								"AND LOWER(p.keyword) LIKE CONCAT('%',CONVERT(LOWER(:keyword),BINARY),'%') " +
								"AND c.slug=:catSlug) " +
								"OR (p.categoryDefault=c.id " +
								"AND provider.host=:host " +
								"AND LOWER(p.nameVi) LIKE CONCAT('%',CONVERT(LOWER(:keyword),BINARY),'%') " +
								"AND c.slug=:catSlug) " +
								"OR (p.categoryDefault=c.id " +
								"AND provider.host=:host " +
								"AND p.skuVi LIKE CONCAT('%',:keyword,'%') " +
								"AND c.slug=:catSlug) " +
								"GROUP BY p.id");
					} else {
						sql.append(" WHERE (p.categoryDefault=c.id " +
								"AND provider.host=:host " +
								"AND p.keyword LIKE :keyword AND c.slug=:catSlug) " +
								"OR (p.categoryDefault=c.id AND provider.host=:host " +
								"AND p.nameVi LIKE :keyword AND c.slug=:catSlug) " +
								"OR (p.categoryDefault=c.id AND provider.host=:host " +
								"AND p.skuVi LIKE CONCAT('%',:keyword,'%') " +
								"AND c.slug=:catSlug) " +
								"GROUP BY p.id");
					}
				}
            }else {
                sql.append(" p LEFT JOIN p.variants v LEFT JOIN p.categories c LEFT JOIN p.providers provider ");
                if (loop.equals(0)){
                    sql.append(" WHERE (p.categoryDefault=c.id AND provider.host=:host AND LOWER(p.keyword) LIKE CONCAT('%',CONVERT(LOWER(:keyword),BINARY),'%')) OR (p.categoryDefault=c.id AND provider.host=:host AND LOWER(p.nameVi) LIKE CONCAT('%',CONVERT(LOWER(:keyword),BINARY),'%')) OR (p.categoryDefault=c.id AND provider.host=:host AND p.skuVi LIKE CONCAT('%',:keyword,'%')) GROUP BY p.id");
                }else {
                    sql.append(" WHERE (p.categoryDefault=c.id AND provider.host=:host AND p.keyword LIKE :keyword) OR (p.categoryDefault=c.id AND provider.host=:host AND p.nameVi LIKE :keyword) OR (p.categoryDefault=c.id AND provider.host=:host AND p.skuVi LIKE CONCAT('%',:keyword,'%')) GROUP BY p.id");
                }
            }
            if (pageable.getSort()!=null){
                StringBuilder sortBuilder = new StringBuilder(" ORDER BY ");
                List<String> orders = new ArrayList<>();
                pageable.getSort().get().forEach(order -> {
                    switch (order.getProperty()){
                        case "topFlag": orders.add(" p."+order.getProperty()+" "+(order.isAscending()?"ASC":"DESC"));break;
                        case "id": orders.add(" p."+order.getProperty()+" "+(order.isAscending()?"ASC":"DESC")); break;
                        case "minv1": orders.add(" min (v.vip1) "+(order.isAscending()?"ASC":"DESC"));break;
                        case "minv2": orders.add(" max (v.vip2) "+(order.isAscending()?"ASC":"DESC"));break;
                        case "minv3": orders.add(" max (v.vip3) "+(order.isAscending()?"ASC":"DESC"));break;
                        case "minPrice": orders.add(" max (v.price) "+(order.isAscending()?"ASC":"DESC"));break;
                    }
                });
                sql.append(" ORDER BY " + orders.stream().collect(Collectors.joining(",")));
            }

            Query query = session.createQuery(sql.toString());
            if (loop==0){
                query.setParameter("keyword",keyword);
            }else {
                query.setParameter("keyword","%"+keyword+"%");
            }
            if (catSlug!=null){
                query.setParameter("catSlug",catSlug);
            }
            query.setParameter("host",host);
            Long count = query.stream().count();
            if (count>0){
                query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
                query.setMaxResults(pageable.getPageSize());
                return new PageImpl<>(query.getResultList(),pageable,count);

            }else {
                if (loop.equals(1) && count<=0){
                    return new PageImpl<>(query.getResultList(),pageable,count);
                }
            }
            loop++;
        }
    }

    @Override
    public List<String> findAllKeywordsByKeyword(String keyword,String hostID) {

        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery(
                "SELECT p.keyword FROM ProductEntity p LEFT JOIN p.providers pr" +
                        " WHERE pr.host=:host AND LOWER(p.keyword) LIKE CONCAT('%',CONVERT(LOWER(:keyword),BINARY),'%')" +
                        "GROUP BY p.keyword"

        );
        query.setParameter("host", hostID);
        query.setParameter("keyword", keyword);
        query.setFirstResult(0);
        query.setMaxResults(5);
        return query.getResultList();
    }

    private void enableFilter(Session session){
//
//        Filter filter = session.enableFilter("FILTER_DOMAIN_NOT_EMPTY");
//        filter.setParameter("condition",0);
//        Filter filterVariant = session.enableFilter("FILTER_DOMAIN");
//        filterVariant.setParameter("domain",domain);
    }
}
