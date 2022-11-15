package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.annotation.Password;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestGeneralMemberDto {

    @Pattern(regexp = "^[a-z0-9]{8,20}$", message = "requestGeneralMember loginName is 8 to 20 "
        + "lowercase letters and numbers")
    private String loginName;
    @Password
    private String password;
    @NotBlank(message = "requestGeneralMember name must not be blank")
    private String name;
}
