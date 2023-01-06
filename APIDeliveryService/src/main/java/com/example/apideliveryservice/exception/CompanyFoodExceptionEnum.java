package com.example.apideliveryservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@Slf4j
public enum CompanyFoodExceptionEnum {
    ADD_ORDER_REQUEST_NAME_BLANK(Constants.addOrderRequestNameBlank,
        "addOrderRequestValidationException", "/errors/food/add/name-blank",
        HttpStatus.BAD_REQUEST),
    ADD_ORDER_REQUEST_PRICE_BLANK(Constants.addOrderRequestPriceBlank,
        "addOrderRequestValidationException", "/errors/food/add/price-blank",
        HttpStatus.BAD_REQUEST),
    ADD_ORDER_DUPLICATED_FOOD_NAME(Constants.addOrderDuplicatedFoodName,
        "duplicatedFoodNameException", "/errors/food/add/duplicate-name",
        HttpStatus.CONFLICT),
    ADD_ORDER_PRICE_NOT_DIGIT(Constants.addOrderPriceNotReadable,
        "addOrderRequestValidationException", "/errors/food/add/price-not-digit",
        HttpStatus.BAD_REQUEST),
    UPDATE_PRICE_REQUEST_PRICE_BLANK(Constants.updatePriceRequestPriceBlank,
        "updatePriceRequestValidationException", "/errors/food/update/price-blank",
        HttpStatus.BAD_REQUEST),
    UPDATE_PRICE_REQUEST_PRICE_NOT_DIGIT(Constants.updatePriceRequestPriceNotDigit,
        "updatePriceRequestValidationException", "/errors/food/add/price-not-digit",
        HttpStatus.BAD_REQUEST),;


    private final String errormessage;
    private final String errorTitle;
    private final String errorType;
    private final HttpStatus httpStatus;

    public static CompanyFoodExceptionEnum findByErrorMessage(String errormessage) {

        for (CompanyFoodExceptionEnum exceptionEnum : CompanyFoodExceptionEnum.values()) {
            if (exceptionEnum.getErrormessage().equals(errormessage)) {
                return exceptionEnum;
            }
        }
        throw new RuntimeException();
    }
    public static class Constants {
        public static final String addOrderRequestNameBlank =
            "companyMember request name can't be blank";
        public static final String addOrderRequestPriceBlank =
            "companyMember request price can't be blank";
        public static final String addOrderDuplicatedFoodName =
            "company food add fail due to duplicated name";
        public static final String addOrderPriceNotReadable =
            "company food add fail due to not readable price";
        public static final String updatePriceRequestPriceBlank =
            "companyMember request update price can't be blank";
        public static final String updatePriceRequestPriceNotDigit =
            "companyMember request update price must be digit";
    }

}
