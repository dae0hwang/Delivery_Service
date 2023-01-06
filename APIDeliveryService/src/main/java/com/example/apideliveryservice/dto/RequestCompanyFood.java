package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.Constants;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCompanyFood {

    private Long memberId;
    @NotBlank(message = Constants.addOrderRequestNameBlank)
    private String name;
    private BigDecimal price;
}
