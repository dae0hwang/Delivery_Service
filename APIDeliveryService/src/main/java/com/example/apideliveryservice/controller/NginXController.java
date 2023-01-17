package com.example.apideliveryservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class NginXController {


    @GetMapping("/a")
    public String first(HttpServletRequest request) {
        log.info("in");
        String url = request.getRequestURL().toString();
        return "apiA " + url;
    }

    @GetMapping("/a/b")
    public String second(HttpServletRequest request) {
        log.info("in");
        String url = request.getRequestURL().toString();
        return "apiB "+url;
    }

}
