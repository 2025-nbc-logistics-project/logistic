package com.logistic.client.company.domain.model.common;

import com.logistic.client.company.domain.exception.common.AlreadyDeletedException;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); //temp

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy = 1L; //temp

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    //삭제
    public void delete(Long deletedBy) {
        if(this.deletedAt != null) {
            throw new AlreadyDeletedException();
        }
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}
