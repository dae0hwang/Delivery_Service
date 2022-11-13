package com.example.apideliveryservice.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCompanyFoodDto {

    @NotBlank(message = "requestCompanyFood memberId must not be blank")
    private String memberId;
    @NotBlank(message = "requestCompanyFood name must not be blank")
    private String name;
    @Digits(message = "requestCompanyFood price must be digit", integer = 10, fraction = 0)
    private String price;
}
