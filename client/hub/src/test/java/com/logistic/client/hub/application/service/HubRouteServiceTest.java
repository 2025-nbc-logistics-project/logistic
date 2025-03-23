package com.logistic.client.hub.application.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.logistic.client.hub.application.dto.FindRouteResponse;
import com.logistic.client.hub.domain.model.Hub;
import com.logistic.client.hub.domain.model.HubLocation;
import com.logistic.client.hub.domain.model.HubRoute;
import com.logistic.client.hub.domain.model.HubRouteId;
import com.logistic.client.hub.domain.repository.HubRepository;
import com.logistic.client.hub.domain.repository.HubRouteRepository;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HubRouteServiceTest {

  @Mock
  private HubRepository hubRepository;

  @Mock
  private HubRouteRepository hubRouteRepository;

  private HubRouteService hubRouteService;

  @BeforeEach
  void setUp() {
    hubRouteService = new HubRouteService(hubRepository, hubRouteRepository);
  }

  private Hub createHub(UUID hubId, double lat, double lon) {
    Hub hub = Mockito.mock(Hub.class);
    Mockito.lenient().when(hub.getId()).thenReturn(hubId);
    Mockito.lenient().when(hub.getLocation()).thenReturn(new HubLocation(lat, lon));
    return hub;
  }

  private HubRoute createRoute(UUID departHubId, UUID arriveHubId) {
    HubRoute route = Mockito.mock(HubRoute.class);
    HubRouteId routeId = Mockito.mock(HubRouteId.class);
    when(route.getHubRouteId()).thenReturn(routeId);
    when(routeId.getDepartHubId()).thenReturn(departHubId);
    when(routeId.getArriveHubId()).thenReturn(arriveHubId);
    return route;
  }

  @Test
  @DisplayName("최적 경로를 올바르게 반환")
  void findOptimalRoute_returnsCorrectPath() {
    // given
    UUID hub1Id = UUID.randomUUID();
    UUID hub2Id = UUID.randomUUID();
    UUID hub3Id = UUID.randomUUID();

    Hub hub1 = createHub(hub1Id, 37.0, 127.0);
    Hub hub2 = createHub(hub2Id, 37.0, 127.0);
    Hub hub3 = createHub(hub3Id, 37.0, 127.0);

    when(hubRepository.findAll()).thenReturn(Arrays.asList(hub1, hub2, hub3));

    HubRoute route1 = createRoute(hub1Id, hub2Id);
    HubRoute route2 = createRoute(hub2Id, hub3Id);
    when(hubRouteRepository.findAll()).thenReturn(Arrays.asList(route1, route2));

    // when : hub1에서 hub3까지 최적 경로 요청
    FindRouteResponse response = hubRouteService.findOptimalRoute(hub1Id, hub3Id);

    // then : 경로 수(3개) 및 순서(허브1,허브2,허브3) 검증
    assertThat(response.getRoute()).hasSize(3);
    assertThat(response.getRoute().get(0).getHubId()).isEqualTo(hub1Id);
    assertThat(response.getRoute().get(1).getHubId()).isEqualTo(hub2Id);
    assertThat(response.getRoute().get(2).getHubId()).isEqualTo(hub3Id);
  }

  @Test
  @DisplayName("경로 미존재 : 도착 허브에 도달할 수 없는 경우 -1 반환")
  void findOptimalRoute_returnsEmptyWhenUnreachable() {
    // given: 3개의 허브가 존재하나, 허브1 -> 허브2 연결만 모킹하여 허브3으로의 경로가 없음
    UUID hub1Id = UUID.randomUUID();
    UUID hub2Id = UUID.randomUUID();
    UUID hub3Id = UUID.randomUUID();

    Hub hub1 = createHub(hub1Id, 37.0, 127.0);
    Hub hub2 = createHub(hub2Id, 37.0, 127.0);
    Hub hub3 = createHub(hub3Id, 37.0, 127.0);

    when(hubRepository.findAll()).thenReturn(Arrays.asList(hub1, hub2, hub3));

    HubRoute route1 = createRoute(hub1Id, hub2Id);
    when(hubRouteRepository.findAll()).thenReturn(Collections.singletonList(route1));

    // when : 허브1에서 허브2 최적 경로 요청
    FindRouteResponse response = hubRouteService.findOptimalRoute(hub1Id, hub3Id);

    // then : 도착 허브에 도달할 수 없으므로 빈 경로, -1 거리 반환
    assertThat(response.getRoute()).isEmpty();
    assertThat(response.getTotalDistance()).isEqualTo(-1);
  }

  @Test
  @DisplayName("시작점 및 도착점 동일 : 시작과 도착이 같은 경우 0의 총 거리를 반환")
  void findOptimalRoute_returnsZeroDistanceForSameStartAndDestination() {
    // given
    UUID hub1Id = UUID.randomUUID();
    Hub hub1 = createHub(hub1Id, 37.0, 127.0);

    when(hubRepository.findAll()).thenReturn(Collections.singletonList(hub1));
    when(hubRouteRepository.findAll()).thenReturn(Collections.emptyList());

    // when
    FindRouteResponse response = hubRouteService.findOptimalRoute(hub1Id, hub1Id);

    // then
    assertThat(response.getRoute()).hasSize(1);
    assertThat(response.getTotalDistance()).isEqualTo(0);
  }
}





























