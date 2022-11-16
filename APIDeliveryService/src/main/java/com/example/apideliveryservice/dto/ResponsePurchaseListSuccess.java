package com.example.apideliveryservice.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePurchaseListSuccess {

    private int status;
    private List<PurchaseListDto> list;
}
