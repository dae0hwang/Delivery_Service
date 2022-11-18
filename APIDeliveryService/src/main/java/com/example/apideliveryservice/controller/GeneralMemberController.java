package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.GeneralMemberDto;
import com.example.apideliveryservice.dto.PurchaseListDto;
import com.example.apideliveryservice.dto.RequestGeneralMemberDto;
import com.example.apideliveryservice.dto.RequestPurchaseListDto;
import com.example.apideliveryservice.dto.ResponseGeneralMemberSuccess;
import com.example.apideliveryservice.dto.ResponsePurchaseListSuccess;
import com.example.apideliveryservice.service.GeneralMemberService;
import com.example.apideliveryservice.service.PurchaseListService;
import com.example.apideliveryservice.threadLocalStorage.ThreadLocalStorage;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/delivery-service/general")
@RequiredArgsConstructor
public class GeneralMemberController {

    private final GeneralMemberService generalMemberService;
    private final PurchaseListService purchaseListService;
    private ThreadLocalStorage threadLocalStorage = new ThreadLocalStorage();

    @PostMapping("/member/join")
    public ResponseEntity joinMember(
        @Validated @RequestBody RequestGeneralMemberDto requestGeneralMember) throws SQLException {
        generalMemberService.join(requestGeneralMember.getLoginName(),
            requestGeneralMember.getPassword(), requestGeneralMember.getName());
        ResponseGeneralMemberSuccess success = new ResponseGeneralMemberSuccess(201, null, null);
        return new ResponseEntity(success, HttpStatus.CREATED);
    }

    @GetMapping("/member/all")
    public ResponseEntity findAllMember() throws SQLException {
        List<GeneralMemberDto> allMember = generalMemberService.findAllMember();
        ResponseGeneralMemberSuccess success = new ResponseGeneralMemberSuccess(200, allMember,
            null);
        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    @GetMapping("/member/information")
    public ResponseEntity findMember(@RequestParam("memberId") String memberId)
        throws SQLException {
        GeneralMemberDto findMember = generalMemberService.findById(memberId);
        ResponseGeneralMemberSuccess success = new ResponseGeneralMemberSuccess(200, null,
            findMember);
        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    @PostMapping("/member/purchase")
    public ResponseEntity foodPurchase(
        @Validated @RequestBody RequestPurchaseListDto requestPurchaseList) throws SQLException {
        purchaseListService.addList(requestPurchaseList.getGeneralMemberId(),
            requestPurchaseList.getCompanyMemberId(), requestPurchaseList.getFoodId(),
            requestPurchaseList.getFoodPrice());
        ResponsePurchaseListSuccess success = new ResponsePurchaseListSuccess(201, null);
        return new ResponseEntity(success, HttpStatus.CREATED);
    }

    @GetMapping("/member/sales")
    public ResponseEntity findSalesLIst(@RequestParam("generalMemberId") String generalMemberId)
        throws SQLException {
        List<PurchaseListDto> salesList = purchaseListService.findByGeneralMemberIdAndThisMonth(
            generalMemberId);
        ResponsePurchaseListSuccess success = new ResponsePurchaseListSuccess(200, salesList);
        return new ResponseEntity(success, HttpStatus.OK);
    }
}
