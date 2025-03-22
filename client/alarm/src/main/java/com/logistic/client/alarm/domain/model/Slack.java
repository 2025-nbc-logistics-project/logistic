package com.logistic.client.alarm.domain.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(name = "p_slack")
@Getter
@NoArgsConstructor
@Where(clause = "is_deleted = false")
public class Slack extends BaseEntity {

    @Id
    private UUID slackId;

    private Long senderId;

    private String receiverSlackId;

    @Embedded
    private Message message;

    public Slack(String receiverSlackId, Long userId, Message message) {
        this.slackId = UUID.randomUUID();
        this.senderId = userId;
        this.receiverSlackId = receiverSlackId;
        this.message = message;
    }
}
