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
    private Long foodId;
    private Integer foodAmount;
}
