package com.logistic.client.delivery.application.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HubRouteMapper {
    public static List<HubRouteResponse> toHubRouteResponses(FindRouteResponse findRouteResponse,
                                                             UUID defaultManagerId) {
        List<RouteStepResponse> steps = findRouteResponse.getRoute();
        List<HubRouteResponse> results = new ArrayList<>();

        if (steps == null || steps.size() < 2) {
            return results; // 빈 리스트 반환
        }

        for (int i = 0; i < steps.size() - 1; i++) {
            RouteStepResponse current = steps.get(i);
            RouteStepResponse next = steps.get(i + 1);

            double segmentDistanceKm = next.getCumulativeDistanceKm() - current.getCumulativeDistanceKm();
            double segmentTimeHours = next.getEstimatedTimeHours() - current.getEstimatedTimeHours();

            HubRouteResponse hubRoute = new HubRouteResponse(
                i + 1,                              // sequence
                current.getHubId(),                 // departureHubId
                next.getHubId(),                    // destinationHubId
                defaultManagerId,                   // deliveryManagerId (필요 시 설정)
                segmentDistanceKm,                  // distance (km)
                segmentTimeHours                    // time (hours)
            );

            results.add(hubRoute);
        }

        return results;
    }
}
