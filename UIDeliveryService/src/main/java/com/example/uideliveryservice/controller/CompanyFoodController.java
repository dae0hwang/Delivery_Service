package com.example.uideliveryservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/delivery-service/company")
public class CompanyFoodController {

    @GetMapping("food/addFood/{memberId}")
    public String addForm() {
        return "company/food/addForm";
    }

    @GetMapping("food/updatePrice/{memberId}")
    public String updatePriceForm() {
        return "company/food/updatePriceForm";
    }
}
