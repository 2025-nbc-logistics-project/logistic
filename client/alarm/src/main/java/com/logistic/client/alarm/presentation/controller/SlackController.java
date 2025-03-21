package com.logistic.client.alarm.presentation.controller;

import com.logistic.client.alarm.application.dto.SlackResponseDto;
import com.logistic.client.alarm.application.service.SlackApplicationService;
import com.logistic.client.alarm.presentation.request.SlackRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slack")
public class SlackController {

    private final SlackApplicationService slackApplicationService;

    @PostMapping
    public ResponseEntity<SlackResponseDto> createSlackAndSend(@RequestBody SlackRequestDto requestDto) {
        SlackResponseDto responseDto = slackApplicationService.createSlackAndSend(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
