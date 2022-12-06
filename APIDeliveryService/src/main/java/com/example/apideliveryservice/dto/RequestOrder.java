package com.example.apideliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrder {

    private Long generalMemberId;
    private Long companyMemberId;
    //    @Digits(message = ExceptionMessage.RequestPurchaseListDtoGeneralFoodId, integer = 10,
//        fraction = 0)
    private Long foodId;
    private Integer foodAmount;
}
