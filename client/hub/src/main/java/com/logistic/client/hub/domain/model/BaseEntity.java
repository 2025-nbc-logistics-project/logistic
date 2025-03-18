package com.logistic.client.hub.domain.model;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreRemove;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEntity {
  private LocalDateTime createdAt;
  private Long createdBy;
  private LocalDateTime updatedAt;
  private Long updatedBy;
  private LocalDateTime deletedAt;
  private Long deletedBy;
  private Boolean isDeleted = false;

  public void markAsDeleted(Long userId){
    if(Boolean.TRUE.equals(isDeleted)){
      return;
    }
    this.isDeleted = true;
    this.deletedAt = LocalDateTime.now();
    this.deletedBy = userId;
  }

  public boolean isDeleted(){
    return Boolean.TRUE.equals(isDeleted);
  }

}