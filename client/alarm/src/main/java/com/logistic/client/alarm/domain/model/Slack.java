package com.logistic.client.alarm.domain.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_slack")
@Getter
@NoArgsConstructor
public class Slack extends BaseEntity {

    @Id
    private UUID slackId;

    private String receiverSlackId;

    @Embedded
    private Message message;

    private Slack(String receiverSlackId, Message message) {
        this.slackId = UUID.randomUUID();
        this.receiverSlackId = receiverSlackId;
        this.message = message;
    }
}
