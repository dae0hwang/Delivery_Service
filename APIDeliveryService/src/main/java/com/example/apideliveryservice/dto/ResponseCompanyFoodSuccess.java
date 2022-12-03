package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.entity.CompanyFoodEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCompanyFoodSuccess  {

    private int status;
    private List<CompanyFoodDto> list;
    private CompanyFoodDto companyFoodDto;
}
