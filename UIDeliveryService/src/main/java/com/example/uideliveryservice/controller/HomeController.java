package com.example.uideliveryservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/delivery-service")
    public String home() {
        return "home";
    }
}
