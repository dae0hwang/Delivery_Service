package com.example.apideliveryservice.controllerexceptionadvice;

import com.example.apideliveryservice.controller.CompanyFoodController;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.exception.ExceptionMessage;
import com.example.apideliveryservice.exception.NonExistentFoodIdException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
        request.setAttribute("errorDetail", ExceptionMessage.DuplicatedFoodNameException);
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NonExistentFoodIdException.class)
    public ResponseEntity nonExistentFoodIdExHandle(NonExistentFoodIdException e,
        HttpServletRequest request) {
        log.error("[exceptionHandle] ex", e);

        String errorName = e.getClass().getSimpleName();
        request.setAttribute("errorType", "/errors/food/find/no-id");
        request.setAttribute("errorTitle", errorName);
        request.setAttribute("errorDetail",ExceptionMessage.NonExistentFoodIdException);
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
            case ExceptionMessage.DuplicatedFoodNameException:
                request.setAttribute("errorType", "/errors/food/add/memberId-blank");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case ExceptionMessage.RequestCompanyFoodDtoName:
                request.setAttribute("errorType", "/errors/food/add/name-blank");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case ExceptionMessage.RequestCompanyFoodDtoPrice:
                request.setAttribute("errorType", "/errors/food/add/price-notDigit");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case ExceptionMessage.RequestCompanyFoodPriceDtoFoodId:
                request.setAttribute("errorType", "/errors/food/update/foodId-blank");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case ExceptionMessage.RequestCompanyFoodPriceDtoFoodPrice:
                request.setAttribute("errorType", "/errors/food/update/price-notDigit");
                request.setAttribute("errorTitle", errorName);
                request.setAttribute("errorDetail", errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return null;
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public void SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e,
        HttpServletRequest request) {
        log.error("[exceptionHandle] ex 여기 들어옴", e);
        log.error(e.getMessage());

    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void HttpMessageNotReadableException(HttpMessageNotReadableException e,
        HttpServletRequest request) {
        log.error("[exceptionHandle] ex 여기 들어옴", e);
        log.error(e.getMessage());

    }

    @ExceptionHandler(InvalidFormatException.class)
    public void HttpMessageNotReadableException(InvalidFormatException e,
        HttpServletRequest request) {
        log.error("[exceptionHandle] ex 여기 들어옴", e);
        log.error(e.getMessage());

    }

}
