package com.example.apideliveryservice.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PrometheusTestController {

    private static int count = 0;
//    @GetMapping("api/ip")
//    public String ipTest(HttpServletRequest request) {
//        String ip = request.getHeader("X_FORWARDED_FOR");
//        return "ip = " + ip;
//    }
//    @GetMapping("api/end-point1")
//    public String endPoint1() {
//
//        return "Metrics for endPoint1";
//    }
//
//    @GetMapping("api/end-point2")
//    public String endpoint2() {
//        return "Metrics for endPoint2";
//    }
//
//    @GetMapping("api/one")
//    public String one() {
//        return "one";
//    }
//
//    @GetMapping("api/two")
//    public String two() {
//        return "two";
//    }
//
//    @GetMapping("api/error1")
//    public String error1() {
//        throw new IllegalStateException("error1");
//    }
//
//    @GetMapping("api/error2")
//    public String error2() {
//        throw new IllegalStateException("error2");
//    }
//
//    @GetMapping("api/error")
//    public int errorRandomTest() {
//        count++;
//        if (count == 3) {
//            count = 0;
//            throw new IllegalStateException("error2");
//        } else {
//            return count;
//        }
//    }

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