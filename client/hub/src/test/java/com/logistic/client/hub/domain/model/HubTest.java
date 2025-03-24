package com.logistic.client.hub.domain.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.logistic.client.hub.domain.exception.HubAlreadyDeletedException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HubTest {

  private Hub hub;
  private final String deleterName = "master123";

  @BeforeEach
  void setUp() {
    hub = Hub.builder()
        .name("테스트 허브")
        .address(new HubAddress("서울시", "어딘구", "살고있동"))
        .location(new HubLocation(37.578182, 126.976702))
        .build();
  }

  @Test
  @DisplayName("허브 정보 수정")
  void updateInfo() {
    //given

    // when
    hub.updateInfo(
        ("수정된 허브"),
        new HubAddress("부산시", "머시구", "살고있동"),
        new HubLocation(35.158612, 129.159979));

    //then
    assertThat(hub.getName()).isEqualTo("수정된 허브");
    assertThat(hub.getAddress().getPostalCode()).isEqualTo("부산시");
    assertThat(hub.getLocation().getLatitude()).isEqualTo(35.158612);
  }

  @Test
  @DisplayName("허브 삭제")
  void deleteHub(){
    // given

    // when
    hub.deleteHub(deleterName);

    // then
    assertThat(hub.isDeleted()).isTrue();
  }

  @Test
  @DisplayName("이미 삭제된 허브를 다시 삭제 시 예외 발생")
  void alwaysDeleteHub(){
    // given
    hub.deleteHub(deleterName);

    // when & then
    assertThatThrownBy(() -> hub.deleteHub(deleterName))
        .isInstanceOf(HubAlreadyDeletedException.class)
        .hasMessageContaining("Hub Already Deleted");
  }



}
