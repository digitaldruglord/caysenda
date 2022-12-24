package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pages")
public class PageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String slug;
    String name;
    @Column(columnDefinition = "text")
    String content;
    @Column(columnDefinition = "text")
    String css;
    String description;
    String image;
    String type;
    Boolean trash;
    @ManyToMany()
    @LazyCollection(LazyCollectionOption.TRUE)
    @JoinTable(name = "provider_post",
            joinColumns = @JoinColumn(name = "postId",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "providerId",referencedColumnName = "id")
    )
    @JsonIgnore
    List<ProviderEntity> providerPage;

    public List<ProviderEntity> getProviderPage() {
        return providerPage;
    }

    public void setProviderPage(List<ProviderEntity> providerPage) {
        this.providerPage = providerPage;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public Boolean getTrash() {
        return trash;
    }

    public void setTrash(Boolean trash) {
        this.trash = trash;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
