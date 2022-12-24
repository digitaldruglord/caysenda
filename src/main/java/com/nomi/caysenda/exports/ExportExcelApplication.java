package com.nomi.caysenda.exports;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityManager;

@Getter
@Setter
public class ExportExcelApplication {
    EntityManager entityManager;

    public ExportExcelApplication(EntityManager entityManager) {
        this.entityManager = entityManager;

    }
}
