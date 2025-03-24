package com.logistic.client.ai.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_ai")
public class Ai extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "ai_id", nullable = false, updatable = false, unique = true)
    private UUID aiId;

    @Column(columnDefinition = "TEXT")
    private String request_text;

    @Column(columnDefinition = "TEXT")
    private String response_text;

    public Ai(String request_text, String response_text) {
        this.request_text = request_text;
        this.response_text = response_text;
    }

}
