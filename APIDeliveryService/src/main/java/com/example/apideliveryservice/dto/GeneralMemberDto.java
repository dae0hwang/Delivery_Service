package com.example.apideliveryservice.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralMemberDto {

    private Long id;
    private String loginName;
    private String password;
    private String name;
    private Boolean phoneVerification;
    private Timestamp createdAt;
}