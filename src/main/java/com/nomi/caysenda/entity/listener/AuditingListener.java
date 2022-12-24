package com.nomi.caysenda.entity.listener;
import com.nomi.caysenda.entity.BaseTimeEntity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class AuditingListener {
    @PrePersist
    public void prePersist(BaseTimeEntity baseEntity){
        Date date = new Date();
        baseEntity.setCreateDate(date);

    }
    @PreUpdate
    public void preUpdate(BaseTimeEntity baseEntity){
        Date date = new Date();
        baseEntity.setModifiedDate(date);


    }


}
