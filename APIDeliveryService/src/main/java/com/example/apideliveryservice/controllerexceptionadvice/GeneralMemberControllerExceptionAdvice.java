package com.example.apideliveryservice.controllerexceptionadvice;


import static com.example.apideliveryservice.exception.GeneralMemberExceptionEnum.GENERAL_JOIN_DUPLICATED_LOGIN_NAME;
import static com.example.apideliveryservice.exception.GeneralMemberExceptionEnum.GENERAL_JOIN_LOGIN_NAME_VALIDATION;
import static com.example.apideliveryservice.exception.GeneralMemberExceptionEnum.GENERAL_JOIN_NAME_VALIDATION;
import static com.example.apideliveryservice.exception.GeneralMemberExceptionEnum.GENERAL_JOIN_PASSWORD_VALIDATION;
import static com.example.apideliveryservice.exception.GeneralMemberExceptionEnum.findByErrorMessage;

import com.example.apideliveryservice.controller.GeneralMemberController;
import com.example.apideliveryservice.exception.GeneralMemberException;
import com.example.apideliveryservice.threadlocalstorage.ErrorInformationTls;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {GeneralMemberController.class})
public class GeneralMemberControllerExceptionAdvice {

    private final ErrorInformationTls threadLocalStorage = new ErrorInformationTls();

    @ExceptionHandler(GeneralMemberException.class)
    public ResponseEntity generalMemberExceptionAdvice(GeneralMemberException e) {
        log.warn("[exceptionAdvice] ex", e);
        return makeGeneralMemberExceptionResponseAndSetTls(e);
    }

    private ResponseEntity makeGeneralMemberExceptionResponseAndSetTls(
        GeneralMemberException e) {
        String errorMessage = e.getMessage();
        switch (findByErrorMessage(errorMessage)) {
            case GENERAL_JOIN_DUPLICATED_LOGIN_NAME:
                threadLocalStorage.setErrorType(GENERAL_JOIN_DUPLICATED_LOGIN_NAME.getErrorType());
                threadLocalStorage.setErrorTitle(GENERAL_JOIN_DUPLICATED_LOGIN_NAME.getErrorTitle());
                threadLocalStorage.setErrorDetail(GENERAL_JOIN_DUPLICATED_LOGIN_NAME.getErrormessage());
                return new ResponseEntity(GENERAL_JOIN_DUPLICATED_LOGIN_NAME.getHttpStatus());

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
        switch (findByErrorMessage(errorMessage)) {
            case GENERAL_JOIN_LOGIN_NAME_VALIDATION:
                threadLocalStorage.setErrorType(GENERAL_JOIN_LOGIN_NAME_VALIDATION.getErrorType());
                threadLocalStorage.setErrorTitle(GENERAL_JOIN_LOGIN_NAME_VALIDATION.getErrorTitle());
                threadLocalStorage.setErrorDetail(GENERAL_JOIN_LOGIN_NAME_VALIDATION.getErrormessage());
                return new ResponseEntity(GENERAL_JOIN_LOGIN_NAME_VALIDATION.getHttpStatus());
            case GENERAL_JOIN_PASSWORD_VALIDATION:
                threadLocalStorage.setErrorType(GENERAL_JOIN_PASSWORD_VALIDATION.getErrorType());
                threadLocalStorage.setErrorTitle(GENERAL_JOIN_PASSWORD_VALIDATION.getErrorTitle());
                threadLocalStorage.setErrorDetail(GENERAL_JOIN_PASSWORD_VALIDATION.getErrormessage());
                return new ResponseEntity(GENERAL_JOIN_PASSWORD_VALIDATION.getHttpStatus());
            case GENERAL_JOIN_NAME_VALIDATION:
                threadLocalStorage.setErrorType(GENERAL_JOIN_NAME_VALIDATION.getErrorType());
                threadLocalStorage.setErrorTitle(GENERAL_JOIN_NAME_VALIDATION.getErrorTitle());
                threadLocalStorage.setErrorDetail(GENERAL_JOIN_NAME_VALIDATION.getErrormessage());
                return new ResponseEntity(GENERAL_JOIN_NAME_VALIDATION.getHttpStatus());
        }
        throw new RuntimeException();
    }
}
