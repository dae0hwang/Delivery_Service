package com.example.apideliveryservice.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyMemberDto {

    private Integer id;
    private String loginName;
    private String password;
    private String name;
    private int phoneVerification;
    private LocalDateTime createdAt;
}
