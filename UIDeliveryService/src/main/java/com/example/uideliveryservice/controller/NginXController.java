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
        log.info("int");
        String url = request.getRequestURL().toString();
        return "uiA " + url;
    }

    @GetMapping("/a/b")
    public String second(HttpServletRequest request) {
        log.info("in");
        String url = request.getRequestURL().toString();
        return "uiB "+url;
    }

}
