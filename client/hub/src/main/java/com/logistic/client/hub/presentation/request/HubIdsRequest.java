package com.logistic.client.hub.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubIdsRequest {
    List<UUID> hubIds;
}
