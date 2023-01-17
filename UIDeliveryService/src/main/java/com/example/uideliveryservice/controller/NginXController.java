package com.example.uideliveryservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class NginXController {

    @GetMapping("/a")
    public String first(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return "uiA " + url;
    }

    @GetMapping("/a/b")
    public String second(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return "uiB "+url;
    }

}
