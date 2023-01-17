package com.example.apideliveryservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NginXController {

    @Value("${server.port}")
    int port;
    @GetMapping("/a")
    public String first() {
        return "apiA "+port;
    }

    @GetMapping("/a/b")
    public String se() {
        return "apiB "+port;
    }

}
