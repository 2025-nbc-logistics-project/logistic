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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Hub API")
@RestController
@RequestMapping("api/v1/hubs")
@AllArgsConstructor
public class HubController {

  private final HubService hubService;
  private final HubRouteService hubRouteService;
  private final UserClient userClient;

  @Operation(summary = "허브 생성", description = "새로운 허브를 생성합니다.")
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

  @Operation(summary = "허브 삭제", description = "기존 허브를 삭제합니다.")
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

  @Operation(
      summary = "허브 단일 조회 (내부)",
      description = "허브 ID로 특정 허브의 상세 정보를 조회합니다."
  )
  @GetMapping("/{hubId}")
  public ResponseEntity<ApiResponse<HubResponse>> getHubById(@PathVariable UUID hubId) {
    Hub hub = hubService.getHub(hubId);
    HubResponse hubResponse = toHubResponse(hub);
    return ResponseUtil.success(hubResponse);
  }

  @Operation(
      summary = "허브 단일 조회 (Feign)",
      description = "Feign 클라이언트에서 사용하기 위한 허브 단일 조회 API입니다."
  )
  @GetMapping("/feign/{hubId}")
  public ResponseEntity<ApiResponse<HubResponse>> getHubByIdFeign(@PathVariable UUID hubId) {
    Hub hub = hubService.getHub(hubId);
    HubResponse hubResponse = toHubResponse(hub);
    return ResponseUtil.success(hubResponse);
  }

  @Operation(
      summary = "허브 목록 조회",
      description = "전체 허브 목록을 조회합니다."
  )
  @GetMapping()
  public ResponseEntity<ApiResponse<List<HubResponse>>> getAllHubs() {
    List<Hub> hubs = hubService.getAllHubs();
    List<HubResponse> responseList = hubs.stream().map(this::toHubResponse)
        .collect(Collectors.toList());
    return ResponseUtil.success(responseList);

  }

  @Operation(
      summary = "허브 수정",
      description = "허브 ID와 수정 요청 정보를 받아 허브 정보를 업데이트합니다."
  )
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

  @Operation(
      summary = "허브 검색",
      description = "검색 키워드, 페이지, 정렬 옵션 등을 통해 허브 목록을 검색합니다."
  )
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

  @Operation(
      summary = "허브 경로 조회 (Feign)",
      description = "출발 허브와 도착 허브 ID를 이용해 최적 경로를 조회합니다."
  )
  @GetMapping("/feign/routes")
  public ResponseEntity<ApiResponse<FindRouteResponse>> getHubRoutes(
      @RequestParam UUID departHubId,
      @RequestParam UUID arriveHubId) {
    FindRouteResponse route = hubRouteService.findOptimalRoute(departHubId, arriveHubId);
    return ResponseUtil.success(route);
  }

  @Operation(
      summary = "허브 이름 조회 (Feign)",
      description = "허브 ID 목록에 해당하는 허브들의 이름을 반환합니다."
  )
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
