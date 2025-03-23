package com.logistic.client.hub.application.service;

import com.logistic.client.hub.application.dto.GetHubNameResponse;
import com.logistic.client.hub.application.dto.UserResponseDto;
import com.logistic.client.hub.application.exception.AuthExceptionCode;
import com.logistic.client.hub.application.exception.HubExceptionCode;
import com.logistic.client.hub.application.exception.UnauthorizedAccessException;
import com.logistic.client.hub.domain.exception.HubAlreadyDeletedException;
import com.logistic.client.hub.domain.exception.HubAlreadyExistsException;
import com.logistic.client.hub.domain.exception.HubNotFoundException;
import com.logistic.client.hub.domain.model.Hub;
import com.logistic.client.hub.domain.model.HubAddress;
import com.logistic.client.hub.domain.model.HubLocation;
import com.logistic.client.hub.domain.repository.HubRepository;
import com.logistic.client.hub.domain.spec.HubSpecifications;
import com.logistic.client.hub.presentation.request.CreateHubRequest;
import com.logistic.client.hub.presentation.request.UpdateHubRequest;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HubService {

  private final HubRepository hubRepository;

  public Hub createHub(CreateHubRequest hubDto, UserResponseDto user) {
    if (!"MASTER".equals(user.getRole())) {
      throw new UnauthorizedAccessException(AuthExceptionCode.UNAUTHORIZED_ACCESS);
    }

    if (hubRepository.existsByName(hubDto.getName())) {
      throw new HubAlreadyExistsException(HubExceptionCode.HUB_ALREADY_EXISTS);
    }
    Hub hub = convertToHubEntity(hubDto);
    return hubRepository.save(hub);
  }

  public void deleteHub(UUID hubId, UserResponseDto user) {
    if (!"MASTER".equals(user.getRole())) {
      throw new UnauthorizedAccessException(AuthExceptionCode.UNAUTHORIZED_ACCESS);
    }
    Hub hub = getHubOrThrow(hubId);

    if (hub.isDeleted()) {
      throw new HubAlreadyDeletedException(HubExceptionCode.HUB_ALREADY_DELETED);
    }

    hub.deleteHub(user.getUserId());
    hubRepository.delete(hub);
  }

  public Hub getHub(UUID hubId) {
    return getHubOrThrow(hubId);
  }

  public Hub updateHub(UUID hubId, UpdateHubRequest request, UserResponseDto user) {
    if (!"MASTER".equals(user.getRole())) {
      throw new UnauthorizedAccessException(AuthExceptionCode.UNAUTHORIZED_ACCESS);
    }
    Hub hub = getHubOrThrow(hubId);
    hub.updateInfo(
        request.getName(),
        new HubAddress(
            request.getPostalCode(),
            request.getStreetAddress(),
            request.getDetailAddress()),
        new HubLocation(
            request.getLatitude(),
            request.getLongitude())
    );

    return hubRepository.save(hub);
  }

  @Transactional(readOnly = true)
  public List<Hub> getAllHubs() {
    return hubRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Page<Hub> searchHubs(String keyword, int page, int size, String sort) {
    if (!(size == 10 || size == 30 || size == 50)) {
      size = 10;
    }

    Sort sortObj = "desc".equalsIgnoreCase(sort)
        ? Sort.by("createdAt").descending()
        : Sort.by("createdAt").ascending();

    Pageable pageable = PageRequest.of(page, size, sortObj);
    Specification<Hub> spec = Specification.where(
        HubSpecifications.nameOrLocationContains(keyword));

    return hubRepository.findAll(spec, pageable);
  }

  public List<GetHubNameResponse> getHubNames(List<UUID> hubIds) {

    List<Hub> hubs = hubRepository.findAllById(hubIds);

    for(UUID hubId : hubIds) {
      if(getHub(hubId).isDeleted()){
        throw new HubNotFoundException(HubExceptionCode.HUB_NOT_FOUND);
      }
    }

    return hubs.stream()
        .map(hub -> new GetHubNameResponse(hub.getId(), hub.getName()))
        .collect(Collectors.toList());
  }

  private Hub convertToHubEntity(CreateHubRequest hubDto) {
    return Hub.builder()
        .name(hubDto.getName())
        .address(new HubAddress(
            hubDto.getPostalCode(),
            hubDto.getStreetAddress(),
            hubDto.getDetailAddress()
        ))
        .location(new HubLocation(
            hubDto.getLatitude(),
            hubDto.getLongitude()
        ))
        .build();
  }

  private Hub getHubOrThrow(UUID hubId) {
    return hubRepository.findById(hubId)
        .orElseThrow(() -> new HubNotFoundException(HubExceptionCode.HUB_NOT_FOUND));
  }

}