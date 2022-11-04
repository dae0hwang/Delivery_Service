package com.example.apideliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCompanyMemberError implements ResponseCompanyMember{

    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
}
