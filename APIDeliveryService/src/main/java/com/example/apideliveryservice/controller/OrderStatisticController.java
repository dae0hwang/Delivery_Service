package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.FoodPriceSumDto;
import com.example.apideliveryservice.dto.ResponseOrderSuccess;
import com.example.apideliveryservice.service.OrderStatisticService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/delivery-service/statistic")
@RequiredArgsConstructor
public class OrderStatisticController {

    private final OrderStatisticService orderStatisticService;

    @GetMapping("/company/all/day")
    public ResponseEntity companyAllOfDayMoney() throws Exception {
        List<FoodPriceSumDto> statisticList = orderStatisticService.companyAllOfDay();
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }
}