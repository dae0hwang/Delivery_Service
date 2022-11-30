package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.entity.PurchaseListEntity;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePurchaseListSuccess {

    private int status;
    private List<PurchaseListEntity> purchaseList;
    private List<FoodPriceSumDto> sumStatisticList;
}
