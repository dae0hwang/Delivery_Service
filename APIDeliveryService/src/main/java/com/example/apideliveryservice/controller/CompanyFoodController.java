package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.dto.RequestCompanyFood;
import com.example.apideliveryservice.dto.RequestCompanyFoodPrice;
import com.example.apideliveryservice.dto.ResponseCompanyFoodSuccess;
import com.example.apideliveryservice.service.CompanyFoodService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/delivery-service/company")
public class CompanyFoodController {

    private final CompanyFoodService companyFoodService;

    @PostMapping("food/addFood")
    public ResponseEntity addFood(@Validated @RequestBody RequestCompanyFood requestCompanyFood) {
        companyFoodService.addFood(requestCompanyFood.getMemberId(), requestCompanyFood.getName(), requestCompanyFood.getPrice());
        ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(201, null, null);
        return new ResponseEntity(success, HttpStatus.CREATED);
    }

    @GetMapping("/food/allFood")
    public ResponseEntity allFood(@RequestParam("memberId") Long companyMemberId) {
        List<CompanyFoodDto> allFood = companyFoodService.findAllFoodByCompanyMemberId(
            companyMemberId);
        ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(200, allFood, null);
        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    @GetMapping("/food/information")
    public ResponseEntity foodInformation(
        @RequestParam("foodId") Long companyFoodId) {
        CompanyFoodDto findFood = companyFoodService.findFoodById(companyFoodId);
        ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(200, null,
            findFood);
        return new ResponseEntity(success, HttpStatus.OK);
    }

    @PutMapping("/food/update")
    public ResponseEntity updatePrice(@Validated @RequestBody RequestCompanyFoodPrice request) {
        companyFoodService.updateFoodPrice(request.getFoodId(), request.getPrice());
        ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(200, null, null);
        return new ResponseEntity(success, HttpStatus.OK);
    }
}
