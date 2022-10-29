package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.service.CompanyFoodService;
import com.example.apideliveryservice.service.DuplicatedFoodNameException;
import java.sql.SQLException;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/delivery-service/company")
public class CompanyFoodController {

    private final CompanyFoodService companyFoodService;

    @PostMapping("/addFood")
    public CompanyFoodRequestStatus addFood(@RequestBody HashMap<String, String> requestJson) {
        log.info(requestJson.toString());
        CompanyFoodDto companyFoodDto = new CompanyFoodDto(null
            , Integer.parseInt(requestJson.get("memberId")), requestJson.get("name")
            , Integer.parseInt(requestJson.get("price")));
        log.info(companyFoodDto.toString());
        try {
            companyFoodService.addFood(companyFoodDto);
            CompanyFoodRequestStatus success = new CompanyFoodRequestStatus("success", null
                , "201 Created", null, null);
            return success;
        } catch (SQLException e) {
            CompanyFoodRequestStatus fail = new CompanyFoodRequestStatus("fail", "SQLException"
                , "500 Internal Server Error", null, null);
            return fail;
        } catch (DuplicatedFoodNameException e) {
            log.error("DuplicatedFoodNameException", e);
            CompanyFoodRequestStatus fail = new CompanyFoodRequestStatus("fail"
                , "duplicatedFoodNameException", "409 Conflict", null
                , null);
            return fail;
        }
    }
}
