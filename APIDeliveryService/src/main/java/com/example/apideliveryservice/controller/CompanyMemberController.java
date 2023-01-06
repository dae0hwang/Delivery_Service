package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.dto.RequestCompanyMember;
import com.example.apideliveryservice.dto.ResponseCompanyMemberSuccess;
import com.example.apideliveryservice.service.CompanyMemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/delivery-service/company")
@RequiredArgsConstructor
public class CompanyMemberController {

    private final CompanyMemberService companyMemberService;

    @PostMapping("/member/join")
    public ResponseEntity<ResponseCompanyMemberSuccess> joinMember(
        @Validated @RequestBody RequestCompanyMember requestCompanyMember) {
        companyMemberService.join(requestCompanyMember.getLoginName(),
            requestCompanyMember.getPassword(), requestCompanyMember.getName());
        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(201, null, null);
        return new ResponseEntity<>(success, HttpStatus.CREATED);
    }

    @GetMapping("/member/allMember")
    public ResponseEntity<ResponseCompanyMemberSuccess> findAllMember() {
        List<CompanyMemberDto> allMember = companyMemberService.findAllMember();
        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(200, allMember,
            null);
        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    @GetMapping("/member/information")
    public ResponseEntity<ResponseCompanyMemberSuccess> findMember(@RequestParam("memberId") Long MemberId) {
        CompanyMemberDto findMember = companyMemberService.findMember(MemberId);
        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(200, null,
            findMember);
        return ResponseEntity.status(HttpStatus.OK).body(success);
    }
}
