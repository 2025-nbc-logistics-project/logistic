package com.logistic.client.hub.presentation.controller;

import com.logistic.client.hub.infrastructure.config.NaverMapProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

  @Autowired
  private NaverMapProperties naverMapProperties;

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("naverClientId", naverMapProperties.getClientId());
    return "index"; // templates/index.html
  }
}