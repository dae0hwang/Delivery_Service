package com.example.apideliveryservice.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPurchaseListDto {

    @Digits(message = "requestPurchaseListDto generalMemberId must be digit", integer = 10,
        fraction = 0)
    private String generalMemberId;
    @Digits(message = "requestPurchaseListDto companyMemberId must be digit", integer = 10,
        fraction = 0)
    private String companyMemberId;
    @Digits(message = "requestPurchaseListDto foodId must be digit", integer = 10, fraction = 0)
    private String foodId;
    @Digits(message = "requestPurchaseListDto foodPrice must be digit", integer = 10, fraction = 0)
    private String foodPrice;
}
