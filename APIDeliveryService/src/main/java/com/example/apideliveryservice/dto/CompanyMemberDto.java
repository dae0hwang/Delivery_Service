package com.example.apideliveryservice.dto;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyMemberDto {

    private Long id;
    private String name;
    private Timestamp createdAt;
}
