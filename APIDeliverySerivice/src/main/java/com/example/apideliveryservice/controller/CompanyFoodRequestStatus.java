package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.dto.CompanyMemberDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyFoodRequestStatus {

    private String status;
    private String errorMessage;
    private String HttpStatusCode;
    private List<CompanyFoodDto> list;
    private CompanyFoodDto companyFoodDto;
}
