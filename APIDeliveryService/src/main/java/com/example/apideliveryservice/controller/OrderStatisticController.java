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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/delivery-service/statistic")
@RequiredArgsConstructor
public class OrderStatisticController {

    private final OrderStatisticService orderStatisticService;

    @GetMapping("/company/all/day")
    public ResponseEntity companyAllOfDay() throws Exception {
        List<FoodPriceSumDto> statisticList = orderStatisticService.companyAllOfDay();
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }

    @GetMapping("/company/all/month")
    public ResponseEntity companyAllOfMonth() throws Exception {
        List<FoodPriceSumDto> statisticList = orderStatisticService.companyAllOfMonth();
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }

    @GetMapping("/company/id/day")
    public ResponseEntity companyIdOfDay(@RequestParam Long companyId) throws Exception {
        List<FoodPriceSumDto> statisticList = orderStatisticService.companyIdOfDay(companyId);
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }

    @GetMapping("/company/id/month")
    public ResponseEntity companyIdOfMonth(@RequestParam Long companyId) throws Exception {
        List<FoodPriceSumDto> statisticList = orderStatisticService.companyIdOfMonth(companyId);
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }

    @GetMapping("/general/id/day")
    public ResponseEntity generalIdOfDay(@RequestParam Long generalId) throws Exception {
        List<FoodPriceSumDto> statisticList = orderStatisticService.generalIdOfDay(generalId);
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }

    @GetMapping("/general/id/month")
    public ResponseEntity generalIdOfMonth(@RequestParam Long generalId) throws Exception {
        List<FoodPriceSumDto> statisticList = orderStatisticService.generalIdOfMonth(generalId);
        ResponseOrderSuccess success = new ResponseOrderSuccess(200, null,
            statisticList);
        return new ResponseEntity(success, HttpStatus.OK);
    }
}
