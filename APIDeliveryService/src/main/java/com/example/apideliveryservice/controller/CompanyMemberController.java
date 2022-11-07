package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.dto.RequestCompanyMemberDto;
import com.example.apideliveryservice.dto.ResponseCompanyMember;
import com.example.apideliveryservice.dto.ResponseCompanyMemberError;
import com.example.apideliveryservice.dto.ResponseCompanyMemberSuccess;
import com.example.apideliveryservice.exception.BlackException;
import com.example.apideliveryservice.exception.NonExistentMemberIdException;
import com.example.apideliveryservice.service.CompanyMemberService;
import com.example.apideliveryservice.exception.DuplicatedLoginNameException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/member/join")
    public ResponseEntity<ResponseCompanyMember> joinMember(
        @RequestBody RequestCompanyMemberDto requestCompanyMember) throws SQLException {

        CompanyMemberDto companyMemberDto = new CompanyMemberDto(null
            , requestCompanyMember.getLoginName(), requestCompanyMember.getPassword()
            , requestCompanyMember.getName(), 0
            , new Date(System.currentTimeMillis()));
        ResponseCompanyMemberSuccess success;
        ResponseCompanyMemberError error;
        try {
            companyMemberService.join(companyMemberDto);
            success = new ResponseCompanyMemberSuccess(201, null, null);
            return ResponseEntity.status(HttpStatus.CREATED).body(success);
        } catch (DuplicatedLoginNameException e) {
            log.info("ex", e);
            error = new ResponseCompanyMemberError("/errors/member/join/duplicate-login-name"
                , "DuplicatedLoginName", 409
                , "Company member join fail due to DuplicatedLoginName"
                , "/api/delivery-service/company/member/join");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (BlackException e) {
            log.info("ex", e);
            error = new ResponseCompanyMemberError("/errors/member/join/blank-input"
                , "BlackException", 400
                , "Company member join fail due to blank input request"
                , "/api/delivery-service/company/member/join");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/member/allMember")
    public ResponseEntity<ResponseCompanyMember> findAllMember() throws SQLException{
        List<CompanyMemberDto> allMember = companyMemberService.findAllMember();
        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(200, allMember,
            null);
        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    @GetMapping("/member/information")
    public ResponseEntity<ResponseCompanyMember> findMember
        (@RequestParam("memberId") String MemberId) throws SQLException {
        try {
            CompanyMemberDto findMember = companyMemberService.findMember(MemberId);
            ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(200
                , null, findMember);
            return ResponseEntity.status(HttpStatus.OK).body(success);
        } catch (NonExistentMemberIdException e) {
            log.info("ex", e);
            ResponseCompanyMemberError error
                = new ResponseCompanyMemberError("/errors/member/find/non-exist"
                , "NonExistentMemberIdException", 404
                , "Company member find fail due to no exist member Id"
                , "/api/delivery-service/company/member/information");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
