package com.logistic.client.alarm.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Getter;

@Embeddable
@Getter
public class Message {
    @Lob
    private final String text;

    protected Message() {
        this.text = null;
    }

    public Message(String text) {
        this.text = text;
    }
}
