package com.logistic.client.alarm.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostMessageRequest {
    private String channel;
    private String text;
}
