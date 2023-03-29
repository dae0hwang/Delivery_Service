package com.example.apideliveryservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PrometheusTestController {

    @GetMapping("/api/info")
    public String info() {
        log.info("info test message");
        return "infoOK";
    }

    @GetMapping("/api/warn")
    public String warn() {
        log.warn("warn test message");
        return "warnOK";
    }

    @GetMapping("/api/error")
    public String error() {
        log.error("error test message");
        return "errorOK";
    }
}