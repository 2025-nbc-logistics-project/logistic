package com.logistic.client.hub.domain.model;

import com.logistic.client.hub.application.exception.HubExceptionCode;
import com.logistic.client.hub.domain.exception.HubAlreadyDeletedException;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "hub")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = """
    UPDATE hub
    SET is_deleted = true,
        deleted_at = now(),
        deleted_by = ?
    WHERE id = ?
""")
@Where(clause = "is_deleted = false")
public class Hub extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Embedded
  private HubAddress address;

  @Embedded
  private HubLocation location;

  public void updateInfo(String name, HubAddress address, HubLocation location){
    if (super.isDeleted()) {
      throw new HubAlreadyDeletedException(HubExceptionCode.HUB_ALREADY_DELETED);
    }
    this.name=name;
    this.address=address;
    this.location=location;
  }

  public void deleteHub(Long deleterId){
    if (super.isDeleted()){
      throw new HubAlreadyDeletedException(HubExceptionCode.HUB_ALREADY_DELETED);
    }

    super.markAsDeleted(deleterId);
  }

}