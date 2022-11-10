package com.example.apideliveryservice.controllerExceptionAdvice;

import com.example.apideliveryservice.controller.CompanyFoodController;
import com.example.apideliveryservice.dto.ResponseCompanyFoodError;
import com.example.apideliveryservice.dto.ResponseCompanyMemberError;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.exception.NonExistentFoodIdException;
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
    public ResponseEntity duplicatedFoodNameExHandle(DuplicatedFoodNameException e) {
        log.error("[exceptionHandle] ex", e);
        ResponseCompanyFoodError error = new ResponseCompanyFoodError("/errors/food/add/duplicate-name"
            , "DuplicatedFoodNameException", 409
            , "company food add fail due to duplicated name"
            , "/api/delivery-service/company/food/addFood");
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NonExistentFoodIdException.class)
    public ResponseEntity nonExistentFoodIdExHandle (NonExistentFoodIdException e) {
        log.error("[exceptionHandle] ex", e);
        ResponseCompanyFoodError error = new ResponseCompanyFoodError("/errors/food/find/no-id"
            , "NonExistentFoodIdException", 404, "find food fail due to no exist food id"
            , "/api/delivery-service/company/food/information");
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
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
            case "requestCompanyFood memberId must not be blank":
                error = new ResponseCompanyMemberError("/errors/food/add/memberId-blank"
                    , "MethodArgumentNotValidException", 400, errorMessage,
                    "/api/delivery-service/company/food/addFood");
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
            case "requestCompanyFood name must not be blank":
                error = new ResponseCompanyMemberError("/errors/food/add/name-blank"
                    , "MethodArgumentNotValidException", 400, errorMessage,
                    "/api/delivery-service/company/food/addFood");
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
            case "requestCompanyFood price must be digit":
                error = new ResponseCompanyMemberError("/errors/food/add/price-notDigit"
                    , "MethodArgumentNotValidException", 400, errorMessage,
                    "/api/delivery-service/company/food/addFood");
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
            case "requestCompanyFoodPrice foodId must not be blank":
                error = new ResponseCompanyMemberError("/errors/food/update/foodId-blank"
                    , "MethodArgumentNotValidException", 400, errorMessage,
                    "/api/delivery-service/company/food/update");
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
            case "requestCompanyFoodPrice price must be digit":
                error = new ResponseCompanyMemberError("/errors/food/update/price-notDigit"
                    , "MethodArgumentNotValidException", 400, errorMessage,
                    "/api/delivery-service/company/food/update");
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
