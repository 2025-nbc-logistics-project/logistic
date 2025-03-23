package com.logistic.client.alarm.presentation.controller;

import com.logistic.client.alarm.application.dto.OrderInfoDto;
import com.logistic.client.alarm.application.dto.PageResponseDto;
import com.logistic.client.alarm.application.dto.SlackResponseDto;
import com.logistic.client.alarm.application.service.SlackApplicationService;
import com.logistic.client.alarm.presentation.request.SlackRequestDto;
import com.logistic.client.alarm.presentation.request.SlackSearchDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slack")
public class SlackController {

    private final SlackApplicationService slackApplicationService;

    @PostMapping("/order")
    public ResponseEntity<Void> createOrderSlack(@RequestBody OrderInfoDto requestDto) {
        slackApplicationService.createOrderSlack(requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<SlackResponseDto> createSlackAndSend(@RequestBody SlackRequestDto requestDto,
                                                               HttpServletRequest request) {
        String userIdStr = request.getHeader("userId");
        UUID userId = UUID.fromString(userIdStr);
        SlackResponseDto responseDto = slackApplicationService.createSlackAndSend(requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{slackId}")
    public ResponseEntity<SlackResponseDto> readSlack(@PathVariable("slackId") UUID slackId,
                                                      HttpServletRequest request) {
        String role = request.getHeader("role");
        if (!role.equals("MASTER")) {
            throw new ForbiddenException("마스터 권한의 유저만 접근 가능합니다.");
        }
        SlackResponseDto responseDto = slackApplicationService.readSlack(slackId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<SlackResponseDto>> searchSlack(@ModelAttribute SlackSearchDto requestDto,
                                                                         HttpServletRequest request) {
        String role = request.getHeader("role");
        if (!role.equals("MASTER")) {
            throw new ForbiddenException("마스터 권한의 유저만 접근 가능합니다.");
        }
        PageResponseDto<SlackResponseDto> responseDtoPage = slackApplicationService.searchSlack(requestDto);
        return ResponseEntity.ok(responseDtoPage);
    }

    @PatchMapping("/{slackId}")
    public ResponseEntity<String> deleteSlack(@PathVariable("slackId") UUID slackId,
                                              HttpServletRequest request) {
        String role = request.getHeader("role");
        if (!role.equals("MASTER")) {
            throw new ForbiddenException("마스터 권한의 유저만 접근 가능합니다.");
        }
        slackApplicationService.deleteSlack(slackId);
        return ResponseEntity.ok("삭제 성공 : " + slackId);
    }
}
