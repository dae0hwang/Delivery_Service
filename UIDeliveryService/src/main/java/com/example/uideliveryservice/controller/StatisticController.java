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

    @GetMapping("/company/id/list")
    public String companyIdList() {
        return "statistic/companyByIdList";
    }

    @GetMapping("/company/id/day/{companyId}")
    public String companyIdOfDay() {
        return "statistic/companyIdOfDay";
    }

    @GetMapping("/company/id/month/{companyId}")
    public String companyIdOfMonth() {
        return "statistic/companyIdOfMonth";
    }

    @GetMapping("/general/id/list")
    public String generalIdList() {
        return "statistic/generalByIdList";
    }

    @GetMapping("/general/id/day/{companyId}")
    public String generalIdOfDay() {
        return "statistic/generalIdOfDay";
    }

    @GetMapping("/general/id/month/{companyId}")
    public String generalIdOfMonth() {
        return "statistic/generalIdOfMonth";
    }
}


