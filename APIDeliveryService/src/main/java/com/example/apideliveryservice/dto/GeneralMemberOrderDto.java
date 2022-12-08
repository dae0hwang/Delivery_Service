package com.example.apideliveryservice.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralMemberOrderDto {

    private Date registrationDate;
    private Long orderId;
    private Long generalId;
    private Long foodId;
    private String foodName;
    private BigDecimal foodPrice;
    private Integer foodAmount;
    private Long companyId;
}
