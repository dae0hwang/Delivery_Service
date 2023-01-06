package com.example.apideliveryservice.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FoodPriceSumDto {

    private Long memberId;
    private String memberName;
    private String date;
    private BigDecimal sum;

    @QueryProjection
    public FoodPriceSumDto(String date, BigDecimal sum) {
        this.date = date;
        this.sum = sum;
    }

    @QueryProjection
    public FoodPriceSumDto(Long memberId, String memberName, String date, BigDecimal sum) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.date = date;
        this.sum = sum;
    }


}
