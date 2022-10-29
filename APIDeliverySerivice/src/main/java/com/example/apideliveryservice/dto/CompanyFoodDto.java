package com.example.apideliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyFoodDto {

    private Integer id;
    private int memberId;
    private String name;
    private int price;
}
