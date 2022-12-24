package com.nomi.caysenda.repositories.custom.impl;

import com.nomi.caysenda.repositories.custom.KeywordCustomRepository;
import com.nomi.caysenda.services.ProductService;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class KeywordCustomRepositoryImpl implements KeywordCustomRepository {
    @Autowired
    EntityManager entityManager;
    @Autowired
    ProductService productService;
    @Override
    public List<String> searchAllByKey(String key, String hostID) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery(
                "SELECT k.keyword FROM KeywordEntity k LEFT JOIN k.providerKeyword p" +
                        " WHERE p.host=:host AND LOWER(k.keyword) LIKE CONCAT('%',CONVERT(LOWER(:keyword),BINARY),'%')" +
                        "GROUP BY k.keyword ORDER BY COUNT (k.keyword) desc"

        );
        query.setParameter("host", hostID);
        query.setParameter("keyword", key);
        query.setFirstResult(0);
        query.setMaxResults(5);
        List<String> keyProduct = productService.findAllKeywordByKeyword(key,hostID);
        Set<String> keywords =  new HashSet<>(query.getResultList());
        keyProduct.forEach(s -> {
            String[] splits =s.split(",");
            Arrays.stream(splits).forEach(s1 -> {
                keywords.add(s1.trim());
            });
        });
        List<String> strings = new ArrayList<>();
        keywords.forEach(s -> {
            if (s.toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT))){
                if (strings.size()<10){
                    strings.add(s);
                }

            }
        });
        Collections.sort(strings,(o1, o2) -> {
            return Integer.compare(o1.length(), o2.length());
        });
        mergesData(strings,keywords);

        return strings;
    }
    private void mergesData(List<String> merge,Set<String> data){
        if (merge.size()<10){
            data.forEach(s -> {
                if (!checkExitsText(s,merge)){
                    if (merge.size()<10){
                        merge.add(s);
                    }

                }
            });
        }

    }
    private Boolean checkExitsText(String text,List<String> data){
        AtomicReference<Boolean> check = new AtomicReference<>(false);
        data.forEach(s -> {
            if (s.equals(text)){
                check.set(true);
            }
        });
        return  check.get();
    }
}
