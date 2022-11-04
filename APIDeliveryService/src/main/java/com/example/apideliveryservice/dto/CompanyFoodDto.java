package com.example.apideliveryservice.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyFoodDto {

    //bigint
    private BigInteger id;
    //bigint
    private BigInteger memberId;
    //varchar
    private String name;
    //decimal
    private BigDecimal price;
}
