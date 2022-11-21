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
public class RequestCompanyFoodDto {

    @NotBlank(message = ExceptionMessage.RequestCompanyFoodDtoMemberId)
    private String memberId;
    @NotBlank(message = ExceptionMessage.RequestCompanyFoodDtoName)
    private String name;
    @Digits(message = ExceptionMessage.RequestCompanyFoodDtoPrice, integer = 10, fraction = 0)
    private String price;
}
