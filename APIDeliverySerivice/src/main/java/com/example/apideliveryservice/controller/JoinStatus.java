package com.example.apideliveryservice.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinStatus {

    private String status;
    private String errorMessage;
}
