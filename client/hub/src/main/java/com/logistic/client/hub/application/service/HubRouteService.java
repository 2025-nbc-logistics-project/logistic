package com.logistic.client.hub.application.service;

import com.logistic.client.hub.application.dto.FindRouteResponse;
import com.logistic.client.hub.application.dto.RouteStepResponse;
import com.logistic.client.hub.domain.model.Hub;
import com.logistic.client.hub.domain.model.HubRoute;
import com.logistic.client.hub.domain.repository.HubRepository;
import com.logistic.client.hub.domain.repository.HubRouteRepository;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HubRouteService {

  private final HubRepository hubRepository;
  private final HubRouteRepository hubRouteRepository;

  public HubRouteService(HubRepository hubRepository, HubRouteRepository hubRouteRepository) {
    this.hubRepository = hubRepository;
    this.hubRouteRepository = hubRouteRepository;
  }

  // 하버사인(Haversine) 공식을 이용하여 두 허브 간 거리를 계산 (단위: km)
  private int calculateDistance(Hub from, Hub to) {
    final int R = 6371; // 지구 반경 (킬로미터)
    double latDistance = Math.toRadians(to.getLocation().getLatitude()) - from.getLocation()
        .getLatitude();
    double lonDistance = Math.toRadians(to.getLocation().getLongitude()) - from.getLocation()
        .getLongitude();
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(from.getLocation().getLatitude())) * Math.cos(
        Math.toRadians(to.getLocation()
            .getLatitude()))
        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c;
    return (int) Math.round(distance);
  }

  public FindRouteResponse findOptimalRoute(UUID startHubId, UUID endHubId) {
    // 1. DB에서 모든 허브 데이터를 가져와 UUID -> Hub 매핑 생성
    List<Hub> allHubs = hubRepository.findAll();
    Map<UUID, Hub> uuidToHub = new HashMap<>();
    for (Hub hub : allHubs) {
      uuidToHub.put(hub.getId(), hub);
    }
    // 2. DB에서 p_hub_route 테이블의 데이터를 가져와 그래프 구축
    //    여기서는 저장된 distance 대신, 허브의 위도/경도 정보를 이용해 동적으로 거리를 계산합니다.
    Map<UUID, Map<UUID, Integer>> graph = new HashMap<>();
    List<HubRoute> routes = hubRouteRepository.findAll();
    for (HubRoute route : routes) {
      UUID departUUID = route.getHubRouteId().getDepartHubId();
      UUID arriveUUID = route.getHubRouteId().getArriveHubId();
      Hub fromHub = uuidToHub.get(departUUID);
      Hub toHub = uuidToHub.get(arriveUUID);
      if (fromHub == null || toHub == null) {
        continue;
      }

      // 동적으로 거리를 계산 (하버사인 공식 사용)
      int dynamicDistance = calculateDistance(fromHub, toHub);

      graph.computeIfAbsent(departUUID, k -> new HashMap<>()).put(arriveUUID, dynamicDistance);
      graph.computeIfAbsent(arriveUUID, k -> new HashMap<>()).put(departUUID, dynamicDistance);
    }

    // 3. 다익스트라 알고리즘 초기화: 모든 노드의 거리를 무한대로, 시작 노드는 0으로 설정
    Map<UUID, Integer> distances = new HashMap<>();
    Map<UUID, UUID> previous = new HashMap<>();

    for (UUID node : uuidToHub.keySet()) {
      distances.put(node, Integer.MAX_VALUE);
      previous.put(node, null);
    }
    distances.put(startHubId, 0);

    // 4. 우선순위 큐를 이용해 최단 경로 탐색
    PriorityQueue<UUID> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
    queue.add(startHubId);

    while (!queue.isEmpty()) {
      UUID cur = queue.poll();
      if (cur.equals(endHubId)) {
        break;
      }
      if (!graph.containsKey(cur)) {
        continue;
      }

      for (Map.Entry<UUID, Integer> neighborEntry : graph.get(cur).entrySet()) {
        UUID neighbor = neighborEntry.getKey();
        // 현재 노드(cur)까지의 누적 거리와 현재 에지의 거리 합계를 계산
        int alt = distances.get(neighbor) + neighborEntry.getValue();
        if (alt < distances.get(neighbor)) {
          distances.put(neighbor, alt);
          previous.put(neighbor, cur);
          queue.add(neighbor);
        }
      }
    }

    // 5. 도착 허브에 도달할 수 없는 경우 처리
    Integer endDistance = distances.get(endHubId) == null ? Integer.MAX_VALUE : distances.get(endHubId);
    if (endDistance == Integer.MAX_VALUE) {
      return new FindRouteResponse(Collections.emptyList(), -1,0);
    }

    // 6. previous 맵을 이용해 최적 경로 재구성 (시작 -> ... -> 도착)
    LinkedList<UUID> path = new LinkedList<>();
    // previous를 순회하면서 모든 uuid 를 path 에 추가한다(???? 잘모르겠음)
    for (UUID at = endHubId; at != null; at = previous.get(at)) {
      path.addFirst(at);
    }

    // 7. 경로에 따른 RouteStepDto 생성 (각 단계별 누적 거리 포함)
    List<RouteStepResponse> routeSteps = new ArrayList<>();
    int cumulativeDistance = 0;
    int cumulativeTime = 0;
    int stepOrder = 1; // 순서 초기값 1
    Iterator<UUID> iter = path.iterator();
    UUID prevUUID = iter.next();
    Hub startHub = uuidToHub.get(prevUUID);
    routeSteps.add(new RouteStepResponse(
        stepOrder++,
        startHub.getId(),
        startHub.getName(),
        0.0,
        0.0
    ));

    while (iter.hasNext()) {
      UUID curUUID = iter.next();
      int edgeDistance = graph.get(prevUUID).get(curUUID);
      cumulativeDistance += edgeDistance;

      double kmEdge = edgeDistance / 100.0;
      int edgeTime = (int) Math.round(kmEdge * (60.0 / 80.0));
      cumulativeTime += edgeTime;

      Hub curHub = uuidToHub.get(curUUID);
      double stepEstimatedTimeHours = Math.round((cumulativeTime / 60.0) * 10) / 10.0;
      double stepDistanceKm = Math.round((cumulativeDistance / 100.0) * 10) / 10.0;

      routeSteps.add(new RouteStepResponse(stepOrder++, curHub.getId(), curHub.getName(), stepDistanceKm, stepEstimatedTimeHours));
      prevUUID = curUUID;
    }

    double displayDistance = Math.round((cumulativeDistance / 100.0) * 10) / 10.0;
    double displayEstimatedTime = Math.round((cumulativeTime / 60.0) * 10) / 10.0;

    return new FindRouteResponse(routeSteps, displayDistance, displayEstimatedTime);
  }
}