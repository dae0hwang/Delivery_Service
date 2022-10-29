package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.service.CompanyMemberService;
import com.example.apideliveryservice.service.DuplicatedLoginNameException;
import com.example.apideliveryservice.service.DuplicatedNameException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/delivery-service/company")
@RequiredArgsConstructor
public class CompanyMemberController {

    private final CompanyMemberService companyMemberService;

    @PostMapping("/join")
    public CompanyMemberRequestStatus joinMember(@RequestBody HashMap<String, String> requestJson) {
        CompanyMemberDto companyMemberDto = new CompanyMemberDto(null, requestJson.get("loginName"),
            requestJson.get("password"), requestJson.get("name"), 0
            , LocalDateTime.now());
        try {
            companyMemberService.join(companyMemberDto);
            CompanyMemberRequestStatus success = new CompanyMemberRequestStatus("success", null
                , "201 Created", null,null);
            return success;
        } catch (SQLException e) {
            CompanyMemberRequestStatus fail = new CompanyMemberRequestStatus("fail", "SQLException"
                , "500 Internal Server Error", null,null);
            return fail;
        } catch (DuplicatedLoginNameException e) {
            log.error("duplicatedLonginNameError", e);
            CompanyMemberRequestStatus fail = new CompanyMemberRequestStatus("fail"
                , "duplicatedLoginNameException", "409 Conflict", null
            ,null);
            return fail;
        } catch (DuplicatedNameException e) {
            log.error("duplicatedNameError", e);
            CompanyMemberRequestStatus fail = new CompanyMemberRequestStatus("fail"
                , "duplicatedNameException", "409 Conflict"
                , null,null);
            return fail;
        }
    }

    @GetMapping("/allMember")
    public CompanyMemberRequestStatus allMember() {
        try {
            List<CompanyMemberDto> allMember = companyMemberService.findAllMember();
            CompanyMemberRequestStatus success = new CompanyMemberRequestStatus("success"
                , null, "200 OK", allMember,null);
            return success;
        } catch (SQLException e) {
            CompanyMemberRequestStatus fail = new CompanyMemberRequestStatus("fail"
                , "SQLException", "500 Internal Server Error", null
            ,null);
            return fail;
        }
    }
}
