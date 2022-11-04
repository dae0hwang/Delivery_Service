package com.example.apideliveryservice.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCompanyFoodSuccess implements ResponseCompanyFood{

    private int status;
    private List<CompanyFoodDto> list;
    private CompanyFoodDto companyFoodDto;
}
