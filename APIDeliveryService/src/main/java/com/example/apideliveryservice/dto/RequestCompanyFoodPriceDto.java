package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.exception.ExceptionMessage;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCompanyFoodPriceDto {

    @NotBlank(message = ExceptionMessage.RequestCompanyFoodPriceDtoFoodId)
    private String foodId;
    @Digits(message = ExceptionMessage.RequestCompanyFoodPriceDtoFoodPrice, integer = 10,
        fraction = 0)
    private String price;

}
