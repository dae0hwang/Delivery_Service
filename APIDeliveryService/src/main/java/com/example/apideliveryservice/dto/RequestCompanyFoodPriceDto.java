package com.example.apideliveryservice.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCompanyFoodPriceDto {

    @NotBlank(message = "requestCompanyFoodPrice foodId must not be blank")
    private String foodId;
    @Digits(message = "requestCompanyFoodPrice price must be digit", integer = 10, fraction = 0)
    private String price;
}
