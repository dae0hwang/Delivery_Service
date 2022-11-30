package com.example.uideliveryservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/delivery-service/statistic")
public class StatisticController {

    @GetMapping("/home")
    public String statisticHome() {
        return "statistic/statisticHome";
    }

    @GetMapping("/company/all/day")
    public String companyAllOfDay() {
        return "statistic/companyAllOfDay";
    }

    @GetMapping("/company/day")
    public String companyOfDay() {
        return "statistic/companyOfDay";
    }
}


