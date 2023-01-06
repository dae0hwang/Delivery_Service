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


    @GetMapping("/company/all/month")
    public String companyAllOfMonth() {
        return "statistic/companyAllOfMonth";
    }

    @GetMapping("/company/member/day")
    public String companyMemberOfDay() {
        return "statistic/companyMemberOfDay";
    }

    @GetMapping("/company/member/month")
    public String companyMemberOfMonth() {
        return "statistic/companyMemberOfMonth";
    }

    @GetMapping("/general/member/day")
    public String generalMemberOfDay() {
        return "statistic/generalMemberOfDay";
    }

    @GetMapping("/general/member/month")
    public String generalMemberOfMonth() {
        return "statistic/generalMemberOfMonth";
    }
}


