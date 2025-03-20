package com.logistic.client.hub.application.service;

import com.logistic.client.hub.application.exception.HubExceptionCode;
import com.logistic.client.hub.domain.exception.HubAlreadyDeletedException;
import com.logistic.client.hub.domain.exception.HubAlreadyExistsException;
import com.logistic.client.hub.domain.exception.HubNotFoundException;
import com.logistic.client.hub.domain.model.Hub;
import com.logistic.client.hub.domain.model.HubAddress;
import com.logistic.client.hub.domain.model.HubLocation;
import com.logistic.client.hub.domain.repository.HubRepository;
import com.logistic.client.hub.domain.spec.HubSpecifications;
import com.logistic.client.hub.presentation.request.CreateHubRequest;
import com.logistic.client.hub.presentation.request.HubDto;
import com.logistic.client.hub.presentation.request.UpdateHubRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HubService {

  private final HubRepository hubRepository;

  public Hub createHub(CreateHubRequest hubDto) {
    if (hubRepository.existsByName(hubDto.getName())) {
      throw new HubAlreadyExistsException(HubExceptionCode.HUB_ALREADY_EXISTS);
    }
    Hub hub = toHubEntity(hubDto);
    return hubRepository.save(hub);
  }

  public void deleteHub(Long hubId) {
    Hub hub = getHubOrThrow(hubId);

    if(hub.isDeleted()){
      throw new HubAlreadyDeletedException(HubExceptionCode.HUB_ALREADY_DELETED);
    }
    // TODO : SecurityContext에서 userId 가져오기
    Long currentUserId = 0L;
    hub.deleteHub(currentUserId);
    hubRepository.delete(hub);
  }

  public Hub getHub(Long hubId) {
    return getHubOrThrow(hubId);
  }

  public Hub updateHub(Long hubId, UpdateHubRequest request) {
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
  public Page<Hub> searchHubs(String keyword, int page, int size){
    Pageable pageable = PageRequest.of(page, size);
    Specification<Hub> spec = Specification.where(HubSpecifications.nameOrLocationContains(keyword));

    return hubRepository.findAll(spec, pageable);
  }


  private Hub toHubEntity(CreateHubRequest hubDto) {
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

  private Hub getHubOrThrow(Long hubId) {
    return hubRepository.findById(hubId)
        .orElseThrow(() -> new HubNotFoundException(HubExceptionCode.HUB_NOT_FOUND));
  }

}