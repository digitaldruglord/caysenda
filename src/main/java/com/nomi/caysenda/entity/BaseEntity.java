package com.nomi.caysenda.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Column(name = "createdate",columnDefinition = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    protected Date createDate;
    @Column(name = "modifydate",columnDefinition = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    protected Date modifyDate;
    @Column(name = "createby")
    @CreatedBy
    protected String createBy;
    @Column(name = "modifyby")
    @LastModifiedBy
    protected String modifyBy;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }
}
