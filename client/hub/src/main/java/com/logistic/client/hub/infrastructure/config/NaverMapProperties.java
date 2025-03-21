package com.logistic.client.hub.infrastructure.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "naver.map")
@Component
public class NaverMapProperties {

  private String clientId;
}