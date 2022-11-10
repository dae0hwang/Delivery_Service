package com.example.apideliveryservice.controllerExceptionAdvice;

import com.example.apideliveryservice.controller.CompanyMemberController;
import com.example.apideliveryservice.dto.ResponseCompanyMemberError;
import com.example.apideliveryservice.exception.DuplicatedLoginNameException;
import com.example.apideliveryservice.exception.NonExistentMemberIdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {CompanyMemberController.class})
public class CompanyMemberControllerExceptionAdvice {

    @ExceptionHandler(DuplicatedLoginNameException.class)
    public ResponseEntity duplicatedLoginNameExHandle(DuplicatedLoginNameException e) {
        log.error("[exceptionHandle] ex", e);
        ResponseCompanyMemberError error = new ResponseCompanyMemberError(
            "/errors/member/join/duplicate-login-name"
            , "DuplicatedLoginName", 409
            , "Company member join fail due to DuplicatedLoginName"
            , "/api/delivery-service/company/member/join");
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidExHandle(MethodArgumentNotValidException e) {
        log.error("[exceptionHandle] ex", e);
        ResponseEntity responseEntity = makeArgumentNotValidResponseBody(e);
        return responseEntity;
    }


    private ResponseEntity makeArgumentNotValidResponseBody(MethodArgumentNotValidException e) {
        ResponseCompanyMemberError error;
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        switch (errorMessage) {
            case "requestCompanyMember loginName is 8 to 20 lowercase letters and numbers":
                error = new ResponseCompanyMemberError("/errors/member/join/longinName-pattern"
                    , "MethodArgumentNotValidException", 400, errorMessage,
                    "/api/delivery-service/company/member/join");
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
            case "requestCompanyMember password is At least 8 characters, at least 1 uppercase"
                + ", lowercase, number, and special character each":
                error = new ResponseCompanyMemberError("/errors/member/join/password-pattern"
                    , "MethodArgumentNotValidException", 400, errorMessage,
                    "/api/delivery-service/company/member/join");
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
            case "requestCompanyMember name must not be blank":
                error = new ResponseCompanyMemberError("/errors/member/join/name-blank"
                    , "MethodArgumentNotValidException", 400, errorMessage,
                    "/api/delivery-service/company/member/join");
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @ExceptionHandler(NonExistentMemberIdException.class)
    public ResponseEntity nonExistentMemberIdExHandle(NonExistentMemberIdException e) {
        log.error("[exceptionHandle] ex", e);
        ResponseCompanyMemberError error = new ResponseCompanyMemberError(
            "/errors/member/find/non-exist"
            , "NonExistentMemberIdException", 404
            , "Company member find fail due to no exist member Id"
            , "/api/delivery-service/company/member/information");
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }
}
