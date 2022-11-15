package com.example.uideliveryservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/delivery-service/general")
public class GeneralMemberController {

    @GetMapping("member/join")
    public String joinForm() {
        return "general/member/joinForm";
    }

    @GetMapping("member/list")
    public String memberList() {
        return "general/member/memberList";
    }

    @GetMapping("member/list/{memberId}")
    public String memberPage() {
        return "general/member/memberPage";
    }

    @GetMapping("member/purchase")
    public String purchasePage(@RequestParam("generalMemberId") String general,
        @RequestParam("companyMemberId") String company) {
        return "general/member/purchasePage";
    }
}
