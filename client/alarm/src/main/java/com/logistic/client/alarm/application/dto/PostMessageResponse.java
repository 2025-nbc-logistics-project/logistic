package com.logistic.client.alarm.application.dto;

import lombok.Data;

@Data
public class PostMessageResponse {
    private boolean ok;
    private String channel;
    private String timeStamp;
    private SlackMessage message;
    private String error;

    @Data
    public static class SlackMessage {
        private String text;
        private String user;
        private String type;
    }
}
