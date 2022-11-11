package com.example.apideliveryservice.controllerExceptionAdvice;

import com.example.apideliveryservice.controller.CompanyMemberController;
import com.example.apideliveryservice.exception.DuplicatedLoginNameException;
import com.example.apideliveryservice.exception.NonExistentMemberIdException;
import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity duplicatedLoginNameExHandle(DuplicatedLoginNameException e,
        HttpServletRequest request) {
        log.error("[exceptionHandle] ex", e);

        String errorName = e.getClass().getSimpleName();
        request.setAttribute("errorType", "/errors/member/join/duplicate-login-name");
        request.setAttribute("errorTitle", errorName);
        request.setAttribute("errorDetail", "Company member join fail due to DuplicatedLoginName");
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidExHandle(MethodArgumentNotValidException e,
        HttpServletRequest request) {
        log.error("[exceptionHandle] ex", e);

        ResponseEntity responseEntity = makeArgumentNotValidResponseBody(e, request);
        return responseEntity;
    }


    private ResponseEntity makeArgumentNotValidResponseBody(MethodArgumentNotValidException e,
        HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String errorName = e.getClass().getSimpleName();
        switch (errorMessage) {
            case "requestCompanyMember loginName is 8 to 20 lowercase letters and numbers":
                request.setAttribute("errorType", "/errors/member/join/longinName-pattern");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case "requestCompanyMember password is At least 8 characters, at least 1 uppercase"
                + ", lowercase, number, and special character each":
                request.setAttribute("errorType", "/errors/member/join/password-pattern");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case "requestCompanyMember name must not be blank":
                request.setAttribute("errorType", "/errors/member/join/name-blank");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @ExceptionHandler(NonExistentMemberIdException.class)
    public ResponseEntity nonExistentMemberIdExHandle(NonExistentMemberIdException e,
        HttpServletRequest request) {
        log.error("[exceptionHandle] ex", e);

        String errorName = e.getClass().getSimpleName();
        request.setAttribute("errorType", "/errors/member/find/non-exist");
        request.setAttribute("errorTitle", errorName);
        request.setAttribute("errorDetail", "Company member find fail due to no exist member Id");
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
