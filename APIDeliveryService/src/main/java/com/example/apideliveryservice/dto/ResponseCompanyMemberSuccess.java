package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.entity.CompanyMemberEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCompanyMemberSuccess {

    private int status;
    private List<CompanyMemberDto> list;
    private CompanyMemberDto companyMemberDto;
}
