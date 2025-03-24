package com.logistic.client.hub.presentation.controller;

import com.logistic.client.hub.application.dto.FindRouteResponse;
import com.logistic.client.hub.application.dto.GetHubNameResponse;
import com.logistic.client.hub.application.dto.UserResponseDto;
import com.logistic.client.hub.application.service.HubRouteService;
import com.logistic.client.hub.application.service.HubService;
import com.logistic.client.hub.infrastructure.client.UserClient;
import com.logistic.client.hub.presentation.common.ApiResponse;
import com.logistic.client.hub.presentation.common.ResponseUtil;
import com.logistic.client.hub.domain.model.Hub;
import com.logistic.client.hub.presentation.request.CreateHubRequest;
import com.logistic.client.hub.presentation.request.UpdateHubRequest;
import com.logistic.client.hub.presentation.response.HubResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import java.util.stream.Collectors;
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
  private final UserClient userClient;


  @PostMapping()
  public ResponseEntity<ApiResponse<HubResponse>> createHub(
      @Valid @RequestBody CreateHubRequest createHubRequest,
      HttpServletRequest request) {

    String authorization = request.getHeader("Authorization");
    String role = request.getHeader("role");
    String username = request.getHeader("username");

    UserResponseDto user = userClient.getUser(authorization, role, username);
    Hub hub = hubService.createHub(createHubRequest, user);
    HubResponse hubResponse = toHubResponse(hub);
    return ResponseUtil.success(hubResponse);
  }

  @PatchMapping("/{hubId}/delete")
  public ResponseEntity<ApiResponse<Void>> deleteHub(@PathVariable UUID hubId,
      HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");
    String role = request.getHeader("role");
    String username = request.getHeader("username");
    UserResponseDto user = userClient.getUser(authorization, role, username);
    hubService.deleteHub(hubId, user);
    return ResponseUtil.noContent();
  }

  @GetMapping("/{hubId}")
  public ResponseEntity<ApiResponse<HubResponse>> getHubById(@PathVariable UUID hubId) {
    Hub hub = hubService.getHub(hubId);
    HubResponse hubResponse = toHubResponse(hub);
    return ResponseUtil.success(hubResponse);
  }

  @GetMapping("/feign/{hubId}")
  public ResponseEntity<ApiResponse<HubResponse>> getHubByIdFeign(@PathVariable UUID hubId) {
    Hub hub = hubService.getHub(hubId);
    HubResponse hubResponse = toHubResponse(hub);
    return ResponseUtil.success(hubResponse);
  }

  @GetMapping()
  public ResponseEntity<ApiResponse<List<HubResponse>>> getAllHubs() {
    List<Hub> hubs = hubService.getAllHubs();
    List<HubResponse> responseList = hubs.stream().map(this::toHubResponse)
        .collect(Collectors.toList());
    return ResponseUtil.success(responseList);

  }

  @PatchMapping("/{hubId}")
  public ResponseEntity<ApiResponse<HubResponse>> updateHub(
      @PathVariable UUID hubId,
      @Valid @RequestBody UpdateHubRequest updateHubRequest,
      HttpServletRequest request
  ) {
    String authorization = request.getHeader("Authorization");
    String role = request.getHeader("role");
    String username = request.getHeader("username");
    UserResponseDto user = userClient.getUser(authorization, role, username);
    Hub updatedHub = hubService.updateHub(hubId, updateHubRequest, user);
    HubResponse hubResponse = toHubResponse(updatedHub);
    return ResponseUtil.success(hubResponse);
  }

  @GetMapping("/search")
  public ResponseEntity<ApiResponse<List<HubResponse>>> searchHubs(
      @RequestParam(required = false) String key,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "asc") String sort
  ) {
    Page<Hub> hubs = hubService.searchHubs(key, page, size, sort);
    List<HubResponse> responseList = hubs.getContent()
        .stream()
        .map(this::toHubResponse)
        .collect(Collectors.toList());
    return ResponseUtil.success(responseList);
  }

  @GetMapping("/feign/routes")
  public ResponseEntity<ApiResponse<FindRouteResponse>> getHubRoutes(
      @RequestParam UUID departHubId,
      @RequestParam UUID arriveHubId) {
    FindRouteResponse route = hubRouteService.findOptimalRoute(departHubId, arriveHubId);
    return ResponseUtil.success(route);
  }

  @PostMapping("/feign/names")
  public ResponseEntity<ApiResponse<List<GetHubNameResponse>>> getHubNames(
      @RequestBody List<UUID> hubIds) {
    List<GetHubNameResponse> hubNames = hubService.getHubNames(hubIds);
    return ResponseUtil.success(hubNames);
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
