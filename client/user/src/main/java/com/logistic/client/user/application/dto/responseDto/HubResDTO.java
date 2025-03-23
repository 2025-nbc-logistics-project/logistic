package com.logistic.client.user.application.dto.responseDto;


import lombok.Getter;

import java.util.UUID;

@Getter
public class HubResDTO {
    private UUID hubId;
    private String name;
    private HubAddress address;
    private HubLocation location;
}

class HubAddress {
    private String postalCode;
    private String streetAddress;
    private String detailAddress;
}

class HubLocation {
    private double latitude;
    private double longitude;
}
