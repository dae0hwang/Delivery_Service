package com.example.apideliveryservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralMemberExceptionEnum {

    GENERAL_JOIN_DUPLICATED_LOGIN_NAME(Constants.joinDuplicatedLoginName,
        "joinDuplicatedLoginNameException", "/errors/general/member/join/duplicate-login-name",
        HttpStatus.CONFLICT),
    GENERAL_JOIN_LOGIN_NAME_VALIDATION(Constants.joinLoginNameValidation,
        "joinLoginNameValidationException", "/errors/general/join/longinName-pattern",
        HttpStatus.BAD_REQUEST),
    GENERAL_JOIN_PASSWORD_VALIDATION(Constants.joinPasswordValidation,
        "joinPasswordValidationException", "/errors/general/join/password-pattern",
        HttpStatus.BAD_REQUEST),
    GENERAL_JOIN_NAME_VALIDATION(Constants.joinNameValidation,
        "joinNameValidationException", "/errors/general/join/name-blank",
        HttpStatus.BAD_REQUEST),;


    private final String errormessage;
    private final String errorTitle;
    private final String errorType;
    private final HttpStatus httpStatus;

    public static GeneralMemberExceptionEnum findByErrorMessage(String errormessage) {

        for (GeneralMemberExceptionEnum exceptionEnum : GeneralMemberExceptionEnum.values()) {
            if (exceptionEnum.getErrormessage().equals(errormessage)) {
                return exceptionEnum;
            }
        }
        throw new RuntimeException();
    }
    public static class Constants {
        public static final String joinDuplicatedLoginName =
            "generalMember join fail due to duplicated loginName";
        public static final String joinLoginNameValidation = "requestGeneraMember loginName"
            + " is 8 to 20 lowercase letters and numbers";
        public static final String joinPasswordValidation = "requestGeneralMember password "
            + "is At least 8 characters, at least 1 uppercase, lowercase, number, and special "
            + "character each";
        public static final String joinNameValidation = "requestGeneralMember name must "
            + "not be blank";

    }

}
