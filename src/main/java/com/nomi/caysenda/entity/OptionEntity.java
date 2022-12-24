package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "options")
public class OptionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String code;
    @Column(columnDefinition = "text")
    String data;

    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "provider_option",
            joinColumns = @JoinColumn(name = "optionId",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "providerId",referencedColumnName = "id")
    )
    @JsonIgnore
    List<ProviderEntity> providerOption;

    public List<ProviderEntity> getProviderOption() {
        return providerOption;
    }

    public void setProviderOption(List<ProviderEntity> providerOption) {
        this.providerOption = providerOption;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
