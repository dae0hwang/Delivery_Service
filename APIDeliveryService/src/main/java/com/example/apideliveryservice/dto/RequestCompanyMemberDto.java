package com.example.apideliveryservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCompanyMemberDto {

    @Pattern(regexp = "[a-z0-9]{8,20}]", message = "requestCompanyMember loginName is 8 to 20 "
        + "lowercase letters and numbers")
    private String loginName;
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$"
    ,message = "requestCompanyMember password is At least 8 characters, at least 1 uppercase"
        + ", lowercase, number, and special character each")
    private String password;
    @NotBlank(message = "requestCompanyMember name must not be blank")
    private String name;
}
