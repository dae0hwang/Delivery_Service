package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.FoodPriceSumDto;
import com.example.apideliveryservice.dto.ResponseOrderSuccess;
import com.example.apideliveryservice.service.OrderStatisticService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/delivery-service/statistic")
@RequiredArgsConstructor
public class OrderStatisticController {

    private final OrderStatisticService orderStatisticService;

    @GetMapping("/company/all/day")
    public ResponseEntity companyAllOfDay() {
        List<FoodPriceSumDto> statisticList = orderStatisticService.companyAllOfDay();
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }

    @GetMapping("/company/all/month")
    public ResponseEntity companyAllOfMonth() {
        List<FoodPriceSumDto> statisticList = orderStatisticService.companyAllOfMonth();
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }

    @GetMapping("/company/member/day")
    public ResponseEntity companyIdOfDay() {
        List<FoodPriceSumDto> statisticList = orderStatisticService.companyMemberOfDay();
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }

    @GetMapping("/company/member/month")
    public ResponseEntity companyIdOfMonth() {
        List<FoodPriceSumDto> statisticList = orderStatisticService.companyMemberOfMonth();
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }

    @GetMapping("/general/member/day")
    public ResponseEntity generalMemberOfDay() {
        List<FoodPriceSumDto> statisticList = orderStatisticService.generalMemberOfDay();
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }

    @GetMapping("/general/member/month")
    public ResponseEntity generalMemberOfMonth() {
        List<FoodPriceSumDto> statisticList = orderStatisticService.generalMemberOfMonth();
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }
}
