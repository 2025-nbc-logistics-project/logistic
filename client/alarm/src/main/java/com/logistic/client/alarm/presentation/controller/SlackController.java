package com.logistic.client.alarm.presentation.controller;

import com.logistic.client.alarm.application.dto.PageResponseDto;
import com.logistic.client.alarm.application.dto.SlackResponseDto;
import com.logistic.client.alarm.application.service.SlackApplicationService;
import com.logistic.client.alarm.presentation.request.SlackRequestDto;
import com.logistic.client.alarm.presentation.request.SlackSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping("/{slackId}")
    public ResponseEntity<SlackResponseDto> readSlack(@PathVariable("slackId") UUID slackId) {
        SlackResponseDto responseDto = slackApplicationService.readSlack(slackId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<SlackResponseDto>> searchSlack(@ModelAttribute SlackSearchDto requestDto) {
        PageResponseDto<SlackResponseDto> responseDtoPage = slackApplicationService.searchSlack(requestDto);
        return ResponseEntity.ok(responseDtoPage);
    }
}
