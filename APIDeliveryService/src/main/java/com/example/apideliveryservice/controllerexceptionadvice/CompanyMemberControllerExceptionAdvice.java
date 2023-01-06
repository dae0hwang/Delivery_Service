package com.example.apideliveryservice.controllerexceptionadvice;

import static com.example.apideliveryservice.exception.CompanyMemberExceptionEnum.COMPANY_JOIN_DUPLICATED_LOGIN_NAME;
import static com.example.apideliveryservice.exception.CompanyMemberExceptionEnum.COMPANY_JOIN_LOGIN_NAME_VALIDATION;
import static com.example.apideliveryservice.exception.CompanyMemberExceptionEnum.COMPANY_JOIN_NAME_VALIDATION;
import static com.example.apideliveryservice.exception.CompanyMemberExceptionEnum.COMPANY_JOIN_PASSWORD_VALIDATION;

import com.example.apideliveryservice.controller.CompanyMemberController;
import com.example.apideliveryservice.exception.CompanyMemberException;
import com.example.apideliveryservice.exception.CompanyMemberExceptionEnum;
import com.example.apideliveryservice.threadlocalstorage.ErrorInformationTls;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {CompanyMemberController.class})
public class CompanyMemberControllerExceptionAdvice {

    private final ErrorInformationTls threadLocalStorage = new ErrorInformationTls();

    @ExceptionHandler(CompanyMemberException.class)
    public ResponseEntity companyMemberExceptionAdvice(CompanyMemberException e) {
        log.warn("[exceptionAdvice] ex", e);
        return makeCompanyMemberExceptionResponseAndSetTls(e);
    }

    private ResponseEntity makeCompanyMemberExceptionResponseAndSetTls(
        CompanyMemberException e) {
        String errorMessage = e.getMessage();
        switch (CompanyMemberExceptionEnum.findByErrorMessage(errorMessage)) {
            case COMPANY_JOIN_DUPLICATED_LOGIN_NAME:
                threadLocalStorage.setErrorType(COMPANY_JOIN_DUPLICATED_LOGIN_NAME.getErrorType());
                threadLocalStorage.setErrorTitle(COMPANY_JOIN_DUPLICATED_LOGIN_NAME.getErrorTitle());
                threadLocalStorage.setErrorDetail(COMPANY_JOIN_DUPLICATED_LOGIN_NAME.getErrormessage());
                return new ResponseEntity(COMPANY_JOIN_DUPLICATED_LOGIN_NAME.getHttpStatus());

        }
        throw new RuntimeException();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidExceptionAdvice(MethodArgumentNotValidException e) {
        log.warn("[exceptionAdvice] ex", e);
        return makeArgumentNotValidResponseAndSetTls(e);
    }

    private ResponseEntity makeArgumentNotValidResponseAndSetTls(
        MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        switch (CompanyMemberExceptionEnum.findByErrorMessage(errorMessage)) {
            case COMPANY_JOIN_LOGIN_NAME_VALIDATION:
                threadLocalStorage.setErrorType(COMPANY_JOIN_LOGIN_NAME_VALIDATION.getErrorType());
                threadLocalStorage.setErrorTitle(COMPANY_JOIN_LOGIN_NAME_VALIDATION.getErrorTitle());
                threadLocalStorage.setErrorDetail(COMPANY_JOIN_LOGIN_NAME_VALIDATION.getErrormessage());
                return new ResponseEntity(COMPANY_JOIN_LOGIN_NAME_VALIDATION.getHttpStatus());
            case COMPANY_JOIN_PASSWORD_VALIDATION:
                threadLocalStorage.setErrorType(COMPANY_JOIN_PASSWORD_VALIDATION.getErrorType());
                threadLocalStorage.setErrorTitle(COMPANY_JOIN_PASSWORD_VALIDATION.getErrorTitle());
                threadLocalStorage.setErrorDetail(COMPANY_JOIN_PASSWORD_VALIDATION.getErrormessage());
                return new ResponseEntity(COMPANY_JOIN_PASSWORD_VALIDATION.getHttpStatus());
            case COMPANY_JOIN_NAME_VALIDATION:
                threadLocalStorage.setErrorType(COMPANY_JOIN_NAME_VALIDATION.getErrorType());
                threadLocalStorage.setErrorTitle(COMPANY_JOIN_NAME_VALIDATION.getErrorTitle());
                threadLocalStorage.setErrorDetail(COMPANY_JOIN_NAME_VALIDATION.getErrormessage());
                return new ResponseEntity(COMPANY_JOIN_NAME_VALIDATION.getHttpStatus());
        }
        throw new RuntimeException();
    }
}
