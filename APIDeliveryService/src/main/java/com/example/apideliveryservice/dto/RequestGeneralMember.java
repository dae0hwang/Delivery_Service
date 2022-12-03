package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.annotation.Password;
import com.example.apideliveryservice.exception.ExceptionMessage;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestGeneralMember {

    @Pattern(regexp = "^[a-z0-9]{8,20}$", message = ExceptionMessage.RequestGeneralMemberDtoLoginName)
    private String loginName;
    @Password
    private String password;
    @NotBlank(message = ExceptionMessage.RequestGeneralMemberDtoName)
    private String name;
}
