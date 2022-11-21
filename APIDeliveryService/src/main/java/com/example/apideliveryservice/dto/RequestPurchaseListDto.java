package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.exception.ExceptionMessage;
import javax.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPurchaseListDto {

    @Digits(message = ExceptionMessage.RequestPurchaseListDtoGeneralMemberId, integer = 10,
        fraction = 0)
    private String generalMemberId;
    @Digits(message = ExceptionMessage.RequestPurchaseListDtoCompanyMemberId, integer = 10,
        fraction = 0)
    private String companyMemberId;
    @Digits(message = ExceptionMessage.RequestPurchaseListDtoGeneralFoodId, integer = 10,
        fraction = 0)
    private String foodId;
    @Digits(message = ExceptionMessage.RequestPurchaseListDtoGeneralFoodPrice, integer = 10,
        fraction = 0)
    private String foodPrice;
}
