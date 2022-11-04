package com.example.apideliveryservice.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCompanyMemberSuccess implements ResponseCompanyMember{

    private int status;
    private List<CompanyMemberDto> list;
    private CompanyMemberDto companyMemberDto;
}
