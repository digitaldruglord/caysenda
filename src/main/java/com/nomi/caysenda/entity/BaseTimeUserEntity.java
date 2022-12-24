package com.nomi.caysenda.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeUserEntity {
    @Column(name = "createdate",columnDefinition = "timestamp")
    @CreatedDate
    protected Date createDate;
    @Column(name = "modifydate",columnDefinition = "timestamp")
    @LastModifiedDate
    protected Date modifiedDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
