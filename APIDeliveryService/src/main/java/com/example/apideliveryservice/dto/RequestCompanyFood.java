package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.exception.ExceptionMessage;
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
    @NotBlank(message = ExceptionMessage.RequestCompanyFoodDtoName)
    private String name;
    private BigDecimal price;
}
