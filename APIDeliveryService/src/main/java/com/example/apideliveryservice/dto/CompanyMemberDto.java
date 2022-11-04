package com.example.apideliveryservice.dto;

import java.math.BigInteger;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyMemberDto {

    //bigint
    private BigInteger id;
    //varchar
    private String loginName;
    //varchar
    private String password;
    //varchar
    private String name;
    //tinytint
    private Integer phoneVerification;
    //date
    private Date createdAt;
}
