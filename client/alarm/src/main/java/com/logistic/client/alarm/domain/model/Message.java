package com.logistic.client.alarm.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Message {
    private final String text;

    protected Message() {
        this.text = null;
    }

    public Message(String text) {
        this.text = text;
    }
}
