package com.example.uideliveryservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/delivery-service/company")
public class CompanyMemberController {

    @GetMapping("/join")
    public String joinForm() {
        return "company/member/joinForm";
    }

    @GetMapping("/members")
    public String memberList() {
        return "company/member/memberList";
    }

    @GetMapping("/members/{memberId}")
    public String memberPage() {
        return "company/member/memberPage";
    }
}
