package com.example.uideliveryservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/delivery-service/company")
public class CompanyFoodController {

    @GetMapping("/addFood/{memberId}")
    public String addForm22() {
        return "company/food/addForm";
    }
}
