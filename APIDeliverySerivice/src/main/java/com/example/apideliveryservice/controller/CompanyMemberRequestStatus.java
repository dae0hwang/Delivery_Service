package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyMemberRequestStatus {

    private String status;
    private String errorMessage;
    private String HttpStatusCode;
    private List<CompanyMemberDto> list;
    private CompanyMemberDto companyMemberDto;
}
