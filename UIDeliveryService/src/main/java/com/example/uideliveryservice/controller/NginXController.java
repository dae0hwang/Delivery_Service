package com.example.uideliveryservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class NginXController {

    @Value("${server.port}")
    int port;
    @GetMapping("/a")
    public String first() {
        return "uiA "+port;
    }

    @GetMapping("/a/b")
    public String se() {
        return "uiB "+port;
    }

}
