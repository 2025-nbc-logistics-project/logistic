package com.logistic.client.hub.presentation.controller;

import com.logistic.client.hub.application.dto.FindRouteResponse;
import com.logistic.client.hub.application.service.HubRouteService;
import com.logistic.client.hub.application.service.HubService;
import com.logistic.client.hub.presentation.common.ApiResponse;
import com.logistic.client.hub.presentation.common.ResponseUtil;
import com.logistic.client.hub.domain.model.Hub;
import com.logistic.client.hub.presentation.request.CreateHubRequest;
import com.logistic.client.hub.presentation.request.UpdateHubRequest;
import com.logistic.client.hub.presentation.response.HubResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/hubs")
@AllArgsConstructor
public class HubController {

  private final HubService hubService;
  private final HubRouteService hubRouteService;

  @PostMapping()
  public ResponseEntity<ApiResponse<HubResponse>> createHub(
      @Valid @RequestBody CreateHubRequest createHubRequest) {
    Hub hub = hubService.createHub(createHubRequest);
    HubResponse hubResponse = toHubResponse(hub);
    return ResponseUtil.success(hubResponse);
  }

  @PatchMapping("/{hubId}/delete")
  public ResponseEntity<ApiResponse<Void>> deleteHub(@PathVariable UUID hubId) {
    // TODO : SecurityContext에서 userId 가져오기
    Long userId = 0L;
    hubService.deleteHub(hubId, userId);
    return ResponseUtil.noContent();
  }

  @GetMapping("/{hubId}")
  public ResponseEntity<ApiResponse<Hub>> getHubById(@PathVariable UUID hubId) {
    Hub hub = hubService.getHub(hubId);
    return ResponseUtil.success(hub);
  }

  @GetMapping()
  public ResponseEntity<ApiResponse<List<Hub>>> getAllHubs() {
    List<Hub> hubs = hubService.getAllHubs();
    return ResponseUtil.success(hubs);

  }

  @PatchMapping("/{hubId}")
  public ResponseEntity<ApiResponse<HubResponse>> updateHub(
      @PathVariable UUID hubId,
      @Valid @RequestBody UpdateHubRequest updateHubRequest
  ) {
    Hub updatedHub = hubService.updateHub(hubId, updateHubRequest);
    HubResponse hubResponse = toHubResponse(updatedHub);
    return ResponseUtil.success(hubResponse);
  }

  @GetMapping("/search")
  public ResponseEntity<ApiResponse<List<Hub>>> searchHubs(
      @RequestParam(required = false) String key,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "asc") String sort
  ) {
    Page<Hub> hubs = hubService.searchHubs(key, page, size, sort);
    return ResponseUtil.success(hubs.getContent());
  }

  @GetMapping("/routes")
  public ResponseEntity<ApiResponse<FindRouteResponse>> getHubRoutes(
      @RequestParam UUID startHubId,
      @RequestParam UUID endHubId) {
    FindRouteResponse route = hubRouteService.findOptimalRoute(startHubId, endHubId);
    return ResponseUtil.success(route);
  }

  private HubResponse toHubResponse(Hub hub) {
    return HubResponse.builder()
        .id(hub.getId())
        .name(hub.getName())
        .postalCode(hub.getAddress().getPostalCode())
        .streetAddress(hub.getAddress().getStreetAddress())
        .detailAddress(hub.getAddress().getDetailAddress())
        .latitude(hub.getLocation().getLatitude())
        .longitude(hub.getLocation().getLongitude())
        .build();
  }
}
