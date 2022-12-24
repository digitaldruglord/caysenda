package com.nomi.caysenda.ghn.entity;

import javax.persistence.*;

@Entity
@Table(name = "ghnSetting")
public class GHNSettingEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(unique = true)
    String keySetting;
    @Column(columnDefinition = "text")
    String valueSetting;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeySetting() {
        return keySetting;
    }

    public void setKeySetting(String keySetting) {
        this.keySetting = keySetting;
    }

    public String getValueSetting() {
        return valueSetting;
    }

    public void setValueSetting(String valueSetting) {
        this.valueSetting = valueSetting;
    }
}
