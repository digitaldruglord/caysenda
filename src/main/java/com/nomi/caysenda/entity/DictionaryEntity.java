package com.nomi.caysenda.entity;

import javax.persistence.*;

@Entity
@Table(name = "dictionaries")
public class DictionaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(unique = true)
    String zhWord;
    String viWord;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZhWord() {
        return zhWord;
    }

    public void setZhWord(String zhWord) {
        this.zhWord = zhWord;
    }

    public String getViWord() {
        return viWord;
    }

    public void setViWord(String viWord) {
        this.viWord = viWord;
    }
}
