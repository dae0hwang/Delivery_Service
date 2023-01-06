package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.exception.CompanyMemberExceptionEnum.Constants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCompanyMember {

    @Pattern(regexp = "^[a-z0-9]{8,20}$", message =
        Constants.joinLoginNameValidation)
    private String loginName;
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$", message
        = Constants.joinPasswordValidation)
    private String password;
    @NotBlank(message = Constants.joinNameValidation)
    private String name;
}
