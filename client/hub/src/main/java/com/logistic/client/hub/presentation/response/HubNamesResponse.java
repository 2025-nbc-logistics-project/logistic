package com.logistic.client.hub.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubNamesResponse {
    List<String> hubNames;
}
