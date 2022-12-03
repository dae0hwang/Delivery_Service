package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.exception.ExceptionMessage;
import java.math.BigDecimal;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCompanyFood {

    @NotBlank(message = ExceptionMessage.RequestCompanyFoodDtoMemberId)
    private String memberId;
    @NotBlank(message = ExceptionMessage.RequestCompanyFoodDtoName)
    private String name;
//    @Digits(message = ExceptionMessage.RequestCompanyFoodDtoPrice, integer = 10, fraction = 0)
    private BigDecimal price;
}
