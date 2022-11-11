package com.example.apideliveryservice.controllerExceptionAdvice;

import com.example.apideliveryservice.controller.CompanyFoodController;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.exception.NonExistentFoodIdException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {CompanyFoodController.class})
public class CompanyFoodControllerExceptionAdvice {

    @ExceptionHandler(DuplicatedFoodNameException.class)
    public ResponseEntity duplicatedFoodNameExHandle(DuplicatedFoodNameException e,
        HttpServletRequest request) {
        log.error("[exceptionHandle] ex", e);

        String errorName = e.getClass().getSimpleName();
        request.setAttribute("errorType", "/errors/food/add/duplicate-name");
        request.setAttribute("errorTitle", errorName);
        request.setAttribute("errorDetail", "company food add fail due to duplicated name");
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NonExistentFoodIdException.class)
    public ResponseEntity nonExistentFoodIdExHandle(NonExistentFoodIdException e,
        HttpServletRequest request) {
        log.error("[exceptionHandle] ex", e);

        String errorName = e.getClass().getSimpleName();
        request.setAttribute("errorType", "/errors/food/find/no-id");
        request.setAttribute("errorTitle", errorName);
        request.setAttribute("errorDetail", "find food fail due to no exist food id");
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidExHandle(MethodArgumentNotValidException e,
        HttpServletRequest request) {
        log.error("[exceptionHandle] ex", e);

        ResponseEntity responseEntity = makeArgumentNotValidResponse(e, request);
        return responseEntity;
    }

    private ResponseEntity makeArgumentNotValidResponse(MethodArgumentNotValidException e,
        HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String errorName = e.getClass().getSimpleName();
        switch (errorMessage) {
            case "requestCompanyFood memberId must not be blank":
                request.setAttribute("errorType", "/errors/food/add/memberId-blank");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case "requestCompanyFood name must not be blank":
                request.setAttribute("errorType", "/errors/food/add/name-blank");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case "requestCompanyFood price must be digit":
                request.setAttribute("errorType", "/errors/food/add/price-notDigit");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case "requestCompanyFoodPrice foodId must not be blank":
                request.setAttribute("errorType", "/errors/food/update/foodId-blank");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case "requestCompanyFoodPrice price must be digit":
                request.setAttribute("errorType", "/errors/food/update/price-notDigit");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
