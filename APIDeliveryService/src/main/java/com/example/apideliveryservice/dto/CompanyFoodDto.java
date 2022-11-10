package com.example.apideliveryservice.dto;

import java.math.BigDecimal;
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
    private BigDecimal price;
}
