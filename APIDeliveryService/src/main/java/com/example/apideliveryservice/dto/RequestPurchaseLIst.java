package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.exception.ExceptionMessage;
import javax.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPurchaseLIst {

    private Long generalMemberId;
    private Long companyMemberId;
    private String foodName;
    @Digits(message = ExceptionMessage.RequestPurchaseListDtoGeneralFoodId, integer = 10,
        fraction = 0)
    private Long foodId;
}
