package com.logistic.client.alarm.presentation.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SlackRequestDto {
    private String receiverSlackId;
    private String text;
}
