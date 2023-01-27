package com.example.apideliveryservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrometheusTestController {

    @GetMapping("api/end-point1")
    public String endPoint1() {
        return "Metrics for endPoint1";
    }

    @GetMapping("api/end-point2")
    public String endpoint2() {
        return "Metrics for endPoint2";
    }

    @GetMapping("api/one")
    public String one() {
        return "one";
    }

    @GetMapping("api/two")
    public String two() {
        return "two";
    }

    @GetMapping("api/error1")
    public String error1() {
        throw new IllegalStateException("error1");
    }

    @GetMapping("api/error2")
    public String error2() {
        throw new IllegalStateException("error2");
    }

}