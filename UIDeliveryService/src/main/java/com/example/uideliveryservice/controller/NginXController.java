package com.example.uideliveryservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NginXController {

    @GetMapping("/a")
    public String first(HttpServletRequest request) {
        log.info("ui - in");

        String url = request.getRemoteAddr().toString();
        return "uiA " + url;
    }

    @GetMapping("/a/b")
    public String second(HttpServletRequest request) {
        log.info("ui - in");
        String url = request.getRemoteAddr().toString();
        return "uiB " + url;
    }

    //api
    @GetMapping("api/a")
    public String api1(HttpServletRequest request) {
        log.info("api - in ");
        String url = request.getRemoteAddr().toString();;
        return "apiA " + url;
    }

    @GetMapping("api/a/b")
    public String api2(HttpServletRequest request) {
        log.info("api - in ");
        String url = request.getRemoteAddr().toString();
        return "apiB "+url;
    }

}
