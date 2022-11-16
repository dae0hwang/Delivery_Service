package com.example.uideliveryservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/delivery-service/company")
public class CompanyMemberController {

    @GetMapping("/member/join")
    public String joinForm() {
        return "company/member/joinForm";
    }

    @GetMapping("/member/members")
    public String memberList() {
        return "company/member/memberList";
    }

    @GetMapping("/member/members/{memberId}")
    public String memberPage() {
        return "company/member/memberPage";
    }

    @GetMapping("/member/sales/{memberId}")
    public String salesPage() {
        return "company/member/salesPage";
    }
}
