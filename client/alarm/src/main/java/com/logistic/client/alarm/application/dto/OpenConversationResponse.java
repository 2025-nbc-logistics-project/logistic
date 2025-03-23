package com.logistic.client.alarm.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenConversationResponse {
    private boolean ok;
    private ChannelInfo channel;
    private String error;

    @Getter
    @Setter
    public static class ChannelInfo {
        private String id; // DM 채널 Id
        private String name;
        private boolean is_im;
    }
}
