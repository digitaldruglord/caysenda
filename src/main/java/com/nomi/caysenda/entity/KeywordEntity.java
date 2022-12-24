package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "keywords")
@EntityListeners(AuditingEntityListener.class)
public class KeywordEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String keyword;
    @Column(name = "createdate",columnDefinition = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    protected Date createDate;

    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "provider_keyword",
            joinColumns = @JoinColumn(name = "keywordId",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "providerId",referencedColumnName = "id")
    )
    @JsonIgnore
    List<ProviderEntity> providerKeyword;

    public List<ProviderEntity> getProviderKeyword() {
        return providerKeyword;
    }

    public void setProviderKeyword(List<ProviderEntity> providerKeyword) {
        this.providerKeyword = providerKeyword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
