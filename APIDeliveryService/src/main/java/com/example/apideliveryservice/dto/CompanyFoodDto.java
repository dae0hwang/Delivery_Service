package com.example.apideliveryservice.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyFoodDto {

    private Long id;
    private Long memberId;
    private String name;
    private Timestamp registrationDate;
    private BigDecimal price;
}
