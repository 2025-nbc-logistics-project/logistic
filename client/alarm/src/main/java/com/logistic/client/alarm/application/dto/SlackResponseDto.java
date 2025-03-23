package com.logistic.client.alarm.application.dto;

import com.logistic.client.alarm.domain.model.Slack;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class SlackResponseDto {
    private UUID slackId;
    private String receiverSlackId;
    private String text;

    public SlackResponseDto(Slack slack) {
        this.slackId = slack.getSlackId();
        this.receiverSlackId = slack.getReceiverSlackId();
        this.text = slack.getMessage().getText();
    }
}
