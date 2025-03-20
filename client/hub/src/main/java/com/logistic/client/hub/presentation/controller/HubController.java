package com.logistic.client.hub.presentation.controller;

import com.logistic.client.hub.application.service.HubService;
import com.logistic.client.hub.common.ApiResponse;
import com.logistic.client.hub.common.ResponseUtil;
import com.logistic.client.hub.domain.model.Hub;
import com.logistic.client.hub.presentation.request.CreateHubRequest;
import com.logistic.client.hub.presentation.request.UpdateHubRequest;
import com.logistic.client.hub.presentation.response.HubResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/hubs")
@AllArgsConstructor
public class HubController {

  private final HubService hubService;

  @PostMapping()
  public ResponseEntity<ApiResponse<HubResponse>> createHub(
      @Valid @RequestBody CreateHubRequest createHubRequest) {
    Hub hub = hubService.createHub(createHubRequest);
    HubResponse hubResponse = toHubResponse(hub);
    return ResponseUtil.success(hubResponse);
  }

  @DeleteMapping("/{hubId}")
  public ResponseEntity<ApiResponse<Void>> deleteHub(@PathVariable Long hubId){
    hubService.deleteHub(hubId);
    return ResponseUtil.noContent();
  }

  @GetMapping("/{hubId}")
  public ResponseEntity<ApiResponse<Hub>> getHubById(@PathVariable Long hubId){
    Hub hub = hubService.getHub(hubId);
    return ResponseUtil.success(hub);
  }

  @GetMapping()
  public ResponseEntity<ApiResponse<List<Hub>>> getAllHubs(){
    List<Hub> hubs = hubService.getAllHubs();
    return ResponseUtil.success(hubs);

  }

  @PatchMapping("/{hubId}")
  public ResponseEntity<ApiResponse<HubResponse>> updateHub(
      @PathVariable Long hubId,
      @Valid @RequestBody UpdateHubRequest updateHubRequest
  ) {
    Hub updatedHub = hubService.updateHub(hubId, updateHubRequest);
    HubResponse hubResponse = toHubResponse(updatedHub);
    return ResponseUtil.success(hubResponse);
  }

  @GetMapping("/search")
  public ResponseEntity<ApiResponse<Page<Hub>>> searchHubs(
      @RequestParam(required = false) String key,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ){
    Page<Hub> hubs = hubService.searchHubs(key, page,size);
    return ResponseUtil.success(hubs);
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
