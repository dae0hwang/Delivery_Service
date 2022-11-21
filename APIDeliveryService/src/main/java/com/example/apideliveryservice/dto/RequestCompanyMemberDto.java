package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.exception.ExceptionMessage;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCompanyMemberDto {

    @Pattern(regexp = "^[a-z0-9]{8,20}$", message =
        ExceptionMessage.RequestCompanyMemberDtoLoginName)
    private String loginName;
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$", message
        = ExceptionMessage.RequestCompanyMemberDtoPassword)
    private String password;
    @NotBlank(message = ExceptionMessage.RequestCompanyMemberDtoName)
    private String name;
}
