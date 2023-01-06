package com.example.apideliveryservice.controllerexceptionadvice;

import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.ADD_ORDER_DUPLICATED_FOOD_NAME;
import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.ADD_ORDER_PRICE_NOT_DIGIT;
import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.ADD_ORDER_REQUEST_NAME_BLANK;
import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.ADD_ORDER_REQUEST_PRICE_BLANK;
import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.UPDATE_PRICE_REQUEST_PRICE_BLANK;
import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.UPDATE_PRICE_REQUEST_PRICE_NOT_DIGIT;
import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.findByErrorMessage;

import com.example.apideliveryservice.controller.CompanyFoodController;
import com.example.apideliveryservice.exception.CompanyFoodException;
import com.example.apideliveryservice.threadlocalstorage.ErrorInformationTls;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {CompanyFoodController.class})
public class CompanyFoodControllerExceptionAdvice {

    private final ErrorInformationTls threadLocalStorage = new ErrorInformationTls();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidExceptionAdvice(MethodArgumentNotValidException e) {
        log.warn("[exceptionAdvice] ex", e);
        return makeArgumentNotValidResponseAndSetTls(e);
    }

    private ResponseEntity makeArgumentNotValidResponseAndSetTls(
        MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        switch (findByErrorMessage(errorMessage)) {
            case ADD_ORDER_REQUEST_NAME_BLANK:
                threadLocalStorage.setErrorType(ADD_ORDER_REQUEST_NAME_BLANK.getErrorType());
                threadLocalStorage.setErrorTitle(ADD_ORDER_REQUEST_NAME_BLANK.getErrorTitle());
                threadLocalStorage.setErrorDetail(ADD_ORDER_REQUEST_NAME_BLANK.getErrormessage());
                return new ResponseEntity(ADD_ORDER_REQUEST_NAME_BLANK.getHttpStatus());
        }
        throw new RuntimeException();
    }

    @ExceptionHandler(CompanyFoodException.class)
    public ResponseEntity companyFoodExceptionAdvice(CompanyFoodException e) {
        log.warn("[exceptionAdvice] ex", e);
        return makeCompanyFoodExceptionResponseAndSetTls(e);
    }

    private ResponseEntity makeCompanyFoodExceptionResponseAndSetTls(
        CompanyFoodException e) {
        String errorMessage = e.getMessage();
        switch (findByErrorMessage(errorMessage)) {
            case ADD_ORDER_REQUEST_PRICE_BLANK:
                threadLocalStorage.setErrorType(ADD_ORDER_REQUEST_PRICE_BLANK.getErrorType());
                threadLocalStorage.setErrorTitle(ADD_ORDER_REQUEST_PRICE_BLANK.getErrorTitle());
                threadLocalStorage.setErrorDetail(ADD_ORDER_REQUEST_PRICE_BLANK.getErrormessage());
                return new ResponseEntity(ADD_ORDER_REQUEST_PRICE_BLANK.getHttpStatus());
            case ADD_ORDER_DUPLICATED_FOOD_NAME:
                threadLocalStorage.setErrorType(ADD_ORDER_DUPLICATED_FOOD_NAME.getErrorType());
                threadLocalStorage.setErrorTitle(ADD_ORDER_DUPLICATED_FOOD_NAME.getErrorTitle());
                threadLocalStorage.setErrorDetail(ADD_ORDER_DUPLICATED_FOOD_NAME.getErrormessage());
                return new ResponseEntity(ADD_ORDER_DUPLICATED_FOOD_NAME.getHttpStatus());
            case UPDATE_PRICE_REQUEST_PRICE_BLANK:
                threadLocalStorage.setErrorType(UPDATE_PRICE_REQUEST_PRICE_BLANK.getErrorType());
                threadLocalStorage.setErrorTitle(UPDATE_PRICE_REQUEST_PRICE_BLANK.getErrorTitle());
                threadLocalStorage.setErrorDetail(UPDATE_PRICE_REQUEST_PRICE_BLANK.getErrormessage());
                return new ResponseEntity(UPDATE_PRICE_REQUEST_PRICE_BLANK.getHttpStatus());
        }
        throw new RuntimeException();
    }

    //기존 exceptionHandle 다르게 처리 service에서 잡을 수 없어서
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity requestCompanyFoodNonDeserializedPrice(HttpMessageNotReadableException e,
        HttpServletRequest request){
        log.error("[exceptionHandle] ex", e);
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/api/delivery-service/company/food/update")) {
            threadLocalStorage.setErrorType(UPDATE_PRICE_REQUEST_PRICE_NOT_DIGIT.getErrorType());
            threadLocalStorage.setErrorTitle(UPDATE_PRICE_REQUEST_PRICE_NOT_DIGIT.getErrorTitle());
            threadLocalStorage.setErrorDetail(UPDATE_PRICE_REQUEST_PRICE_NOT_DIGIT.getErrormessage());
            return new ResponseEntity(UPDATE_PRICE_REQUEST_PRICE_NOT_DIGIT.getHttpStatus());
        } else if (requestURI.equals("/api/delivery-service/company/food/addFood")) {
            threadLocalStorage.setErrorType(ADD_ORDER_PRICE_NOT_DIGIT.getErrorType());
            threadLocalStorage.setErrorTitle(ADD_ORDER_PRICE_NOT_DIGIT.getErrorTitle());
            threadLocalStorage.setErrorDetail(ADD_ORDER_PRICE_NOT_DIGIT.getErrormessage());
            return new ResponseEntity(ADD_ORDER_PRICE_NOT_DIGIT.getHttpStatus());
        }
        throw new RuntimeException();

    }
}
