package com.example.apideliveryservice.controllerExceptionAdvice;

import com.example.apideliveryservice.controller.GeneralMemberController;
import com.example.apideliveryservice.exception.DeliveryServiceException;
import com.example.apideliveryservice.exception.ExceptionMessage;
import com.example.apideliveryservice.threadLocalStorage.ThreadLocalStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {GeneralMemberController.class})
public class GeneralMemberControllerExceptionAdvice {

    private final ThreadLocalStorage threadLocalStorage = new ThreadLocalStorage();

    @ExceptionHandler(DeliveryServiceException.class)
    public ResponseEntity methodArgumentNotValidExHandle(DeliveryServiceException e) {
        log.warn("[exceptionHandle] ex", e);

        ResponseEntity responseEntity = makeDeliveryServiceExceptionResponseAndSetTls(e);
        return responseEntity;
    }

    private ResponseEntity makeDeliveryServiceExceptionResponseAndSetTls(
        DeliveryServiceException e) {

        String errorMessage = e.getMessage();
        String errorName = e.getClass().getSimpleName();
        switch (errorMessage) {
            case ExceptionMessage.DeliveryExceptionDuplicatedName:
                threadLocalStorage.setErrorType("/errors/general/member/join/duplicate-login-name");
                threadLocalStorage.setErrorTitle(errorName);
                threadLocalStorage.setErrorDetail(errorMessage);
                return new ResponseEntity(HttpStatus.CONFLICT);
            case ExceptionMessage.DeliveryExceptionNonExistentMemberId:
                threadLocalStorage.setErrorType("/errors/general/member/find/non-exist");
                threadLocalStorage.setErrorTitle(errorName);
                threadLocalStorage.setErrorDetail(errorMessage);
                return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return null;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidExHandle(MethodArgumentNotValidException e) {
        log.warn("[exceptionHandle] ex", e);

        ResponseEntity responseEntity = makeArgumentNotValidResponseAndSetTls(e);
        return responseEntity;
    }


    private ResponseEntity makeArgumentNotValidResponseAndSetTls(
        MethodArgumentNotValidException e) {
        String errorName = e.getClass().getSimpleName();
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        switch (errorMessage) {
            case ExceptionMessage.RequestGeneralMemberDtoLoginName:
                threadLocalStorage.setErrorType("/errors/general/member/join/longinName-pattern");
                threadLocalStorage.setErrorTitle(errorName);
                threadLocalStorage.setErrorDetail(errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case ExceptionMessage.RequestGeneralMemberDtoPassword:
                threadLocalStorage.setErrorType("/errors/general/member/join/password-pattern");
                threadLocalStorage.setErrorTitle(errorName);
                threadLocalStorage.setErrorDetail(errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case ExceptionMessage.RequestGeneralMemberDtoName:
                threadLocalStorage.setErrorType("/errors/general/member/join/name-blank");
                threadLocalStorage.setErrorTitle(errorName);
                threadLocalStorage.setErrorDetail(errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case ExceptionMessage.RequestPurchaseListDtoGeneralMemberId:
                threadLocalStorage.setErrorType(
                    "/errors/general/member/purchase/generalMemberId-notDigit");
                threadLocalStorage.setErrorTitle(errorName);
                threadLocalStorage.setErrorDetail(errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case ExceptionMessage.RequestPurchaseListDtoCompanyMemberId:
                threadLocalStorage.setErrorType(
                    "/errors/general/member/purchase/companyMemberId-notDigit");
                threadLocalStorage.setErrorTitle(errorName);
                threadLocalStorage.setErrorDetail(errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case ExceptionMessage.RequestPurchaseListDtoGeneralFoodId:
                threadLocalStorage.setErrorType("/errors/general/member/purchase/foodId-notDigit");
                threadLocalStorage.setErrorTitle(errorName);
                threadLocalStorage.setErrorDetail(errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            case ExceptionMessage.RequestPurchaseListDtoGeneralFoodPrice:
                threadLocalStorage.setErrorType(
                    "/errors/general/member/purchase/foodPrice-notDigit");
                threadLocalStorage.setErrorTitle(errorName);
                threadLocalStorage.setErrorDetail(errorMessage);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
