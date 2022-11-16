package com.example.apideliveryservice.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseListDto {

    private Long id;
    private Long generalId;
    private Long companyId;
    private Long foodId;
    private BigDecimal foodPrice;
    private Timestamp createdAt;
}
