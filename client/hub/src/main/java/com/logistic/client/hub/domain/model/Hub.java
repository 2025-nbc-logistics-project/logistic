package com.logistic.client.hub.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.logistic.client.hub.application.exception.HubExceptionCode;
import com.logistic.client.hub.domain.exception.HubAlreadyDeletedException;
import java.util.UUID;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import jakarta.persistence.*;


@Entity
@Table(name = "p_hub")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = """
        UPDATE p_hub
        SET is_deleted = true,
            deleted_at = now()
        WHERE id = ?
    """)
@Where(clause = "is_deleted = false")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hub extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "postalCode", column = @Column(name = "hub_postal_code")),
      @AttributeOverride(name = "streetAddress", column = @Column(name = "hub_street_address")),
      @AttributeOverride(name = "detailAddress", column = @Column(name = "hub_detail_address"))
  })
  private HubAddress address;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "latitude", column = @Column(name = "hub_latitude")),
      @AttributeOverride(name = "longitude", column = @Column(name = "hub_longitude"))
  })
  private HubLocation location;

  public void updateInfo(String name, HubAddress address, HubLocation location) {
    if (super.isDeleted()) {
      throw new HubAlreadyDeletedException(HubExceptionCode.HUB_ALREADY_DELETED);
    }
    this.name = name;
    this.address = address;
    this.location = location;
  }

  public void deleteHub(UUID deleterId) {
    if (super.isDeleted()) {
      throw new HubAlreadyDeletedException(HubExceptionCode.HUB_ALREADY_DELETED);
    }

    super.markAsDeleted(deleterId);
  }

}