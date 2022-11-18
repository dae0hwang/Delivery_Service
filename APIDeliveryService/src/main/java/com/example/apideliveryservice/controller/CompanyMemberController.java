package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.dto.PurchaseListDto;
import com.example.apideliveryservice.dto.RequestCompanyMemberDto;
import com.example.apideliveryservice.dto.ResponseCompanyMemberSuccess;
import com.example.apideliveryservice.dto.ResponsePurchaseListSuccess;
import com.example.apideliveryservice.service.CompanyMemberService;
import com.example.apideliveryservice.service.PurchaseListService;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/delivery-service/company")
@RequiredArgsConstructor
public class CompanyMemberController {

    private final CompanyMemberService companyMemberService;
    private final PurchaseListService purchaseListService;

    @PostMapping("/member/join")
    public ResponseEntity joinMember(
        @Validated @RequestBody RequestCompanyMemberDto requestCompanyMember) throws SQLException {
        companyMemberService.join(requestCompanyMember.getLoginName(),
            requestCompanyMember.getPassword(), requestCompanyMember.getName());
        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(201, null, null);
        return new ResponseEntity(success, HttpStatus.CREATED);
    }

    @GetMapping("/member/allMember")
    public ResponseEntity findAllMember() throws SQLException {
        List<CompanyMemberDto> allMember = companyMemberService.findAllMember();
        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(200, allMember,
            null);
        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    @GetMapping("/member/information")
    public ResponseEntity findMember(@RequestParam("memberId") String MemberId)
        throws SQLException {
        CompanyMemberDto findMember = companyMemberService.findMember(MemberId);
        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(200, null,
            findMember);
        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    @GetMapping("/member/sales")
    public ResponseEntity findSalesLIst(@RequestParam("companyMemberId") String companyMemberId)
        throws SQLException {
        List<PurchaseListDto> salesList = purchaseListService.findByCompanyMemberIdAndThisMonth(
            companyMemberId);
        ResponsePurchaseListSuccess success = new ResponsePurchaseListSuccess(200, salesList);
        return new ResponseEntity(success, HttpStatus.OK);
    }
}
