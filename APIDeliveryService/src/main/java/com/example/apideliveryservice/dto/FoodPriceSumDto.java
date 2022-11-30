package com.example.apideliveryservice.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodPriceSumDto {

    private String date;
    private BigDecimal sum;
}
