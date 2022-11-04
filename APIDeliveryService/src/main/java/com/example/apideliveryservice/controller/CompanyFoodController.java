package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.dto.RequestCompanyFoodDto;
import com.example.apideliveryservice.dto.RequestCompanyFoodPriceDto;
import com.example.apideliveryservice.dto.ResponseCompanyFood;
import com.example.apideliveryservice.dto.ResponseCompanyFoodError;
import com.example.apideliveryservice.dto.ResponseCompanyFoodSuccess;
import com.example.apideliveryservice.exception.BlackException;
import com.example.apideliveryservice.exception.NonExistentFoodIdException;
import com.example.apideliveryservice.exception.NotDigitException;
import com.example.apideliveryservice.service.CompanyFoodService;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/delivery-service/company")
public class CompanyFoodController {

    private final CompanyFoodService companyFoodService;

    @PostMapping("food/addFood")
    public ResponseEntity<ResponseCompanyFood> addFood(
        @RequestBody RequestCompanyFoodDto requestCompanyFood) throws SQLException {
        ResponseCompanyFoodSuccess success;
        ResponseCompanyFoodError error;
        try {
            companyFoodService.addFood(requestCompanyFood);
            success = new ResponseCompanyFoodSuccess(201, null, null);
            return ResponseEntity.status(HttpStatus.CREATED).body(success);
        } catch (DuplicatedFoodNameException e) {
            log.info("ex", e);
            error = new ResponseCompanyFoodError("/errors/food/add/duplicate-name"
                , "DuplicatedFoodNameException", 409
                , "company food add fail due to duplicated name"
                , "/api/delivery-service/company/food/addFood");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (BlackException e) {
            log.info("ex", e);
            error = new ResponseCompanyFoodError("/errors/food/add/blank-input"
                , "BlackException", 400, "company food add fail due to blank input"
                , "/api/delivery-service/company/food/addFood");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (NotDigitException e) {
            log.info("ex", e);
            error = new ResponseCompanyFoodError("/errors/food/add/not-digit"
                , "NotDigitException", 400, "company food add fail due to not digit input"
                , "/api/delivery-service/company/food/addFood");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/food/allFood")
    public ResponseEntity<ResponseCompanyFood> allFood(@RequestParam("memberId") String memberId)
        throws SQLException {
        List<CompanyFoodDto> allFood = companyFoodService.findAllFood(memberId);
        ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(200, allFood, null);
        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    @GetMapping("/food/information")
    public ResponseEntity<ResponseCompanyFood> foodInformation(
        @RequestParam("foodId") String foodId) throws SQLException {
        try {
            CompanyFoodDto findFood = companyFoodService.findFood(foodId);
            ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(200, null,
                findFood);
            return ResponseEntity.status(HttpStatus.OK).body(success);
        } catch (NonExistentFoodIdException e) {
            log.info("ex", e);
            ResponseCompanyFoodError error = new ResponseCompanyFoodError("/errors/food/find/no-id"
                , "NonExistentFoodIdException", 404, "find food fail due to no exist food id"
                , "/api/delivery-service/company/food/information");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PutMapping("/food/update")
    public ResponseEntity<ResponseCompanyFood> updatePrice(
        @RequestBody RequestCompanyFoodPriceDto request) throws SQLException {
        try {
            companyFoodService.updatePrice(request.getFoodId(), request.getPrice());
            ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(200, null, null);
            return ResponseEntity.status(HttpStatus.OK).body(success);
        } catch (BlackException e) {
            log.info("ex", e);
            ResponseCompanyFoodError error = new ResponseCompanyFoodError(
                "/errors/food/update/black-input"
                , "BlackException", 400, "update food price fail due to black request input"
                , "/api/delivery-service/company/food/update");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (NotDigitException e) {
            log.info("ex", e);
            ResponseCompanyFoodError error = new ResponseCompanyFoodError(
                "/errors/food/update/not-digit"
                , "NotDigitException", 400, "update food price fail due not digit price input"
                , "/api/delivery-service/company/food/update");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
