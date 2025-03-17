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
import com.logistic.client.hub.presentation.request.HubDto;
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

  public Hub createHub(HubDto hubDto) {
    // 동일한 이름이면 에러 처리
    if (hubRepository.existsByName(hubDto.getName())) {
      throw new HubAlreadyExistsException(HubExceptionCode.HUB_ALREADY_EXISTS);
    }
    Hub hub = toHubEntity(hubDto);
    return hubRepository.save(hub);
  }




  private Hub toHubEntity(HubDto hubDto) {
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