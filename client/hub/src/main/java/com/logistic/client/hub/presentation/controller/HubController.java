package com.logistic.client.hub.presentation.controller;

import com.logistic.client.hub.application.service.HubService;
import com.logistic.client.hub.common.ApiResponse;
import com.logistic.client.hub.common.ResponseUtil;
import com.logistic.client.hub.domain.model.Hub;
import com.logistic.client.hub.presentation.request.HubDto;
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
  public ResponseEntity<ApiResponse<Hub>> createHub(@Valid @RequestBody HubDto hubDto){
    Hub hub = hubService.createHub(hubDto);
    return ResponseUtil.success(hub);
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
  public ResponseEntity<ApiResponse<Hub>> updateHub(@PathVariable Long hubId, @Valid @RequestBody HubDto hubDto){
    Hub updatedHub = hubService.updateHub(hubId, hubDto);
    return ResponseUtil.success(updatedHub);
  }


}
