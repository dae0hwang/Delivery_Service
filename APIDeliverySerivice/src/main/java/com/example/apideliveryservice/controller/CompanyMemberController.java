package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
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
    private final CompanyMemberRepository repository;

    @PostMapping("/join")
    public RequestStatus joinMember(@RequestBody HashMap<String, String> requestJson) {
        CompanyMemberDto companyMemberDto = new CompanyMemberDto(null, requestJson.get("loginName"),
            requestJson.get("password"), requestJson.get("name"), 0
            , LocalDateTime.now());
        try {
            companyMemberService.join(companyMemberDto);
            RequestStatus success = new RequestStatus("success", null
                , "201 Created", null);
            return success;
        } catch (SQLException e) {
            RequestStatus fail = new RequestStatus("fail", "SQLException"
                , "500 Internal Server Error", null);
            return fail;
        } catch (DuplicatedLoginNameException e) {
            log.info("duplicatedLonginNameError", e);
            RequestStatus fail = new RequestStatus("fail"
                , "duplicatedLoginNameException", "409 Conflict", null);
            return fail;
        } catch (DuplicatedNameException e) {
            log.info("duplicatedNameError", e);
            RequestStatus fail = new RequestStatus("fail", "duplicatedNameException"
                , "409 Conflict", null);
            return fail;
        }
    }

    @GetMapping("/allMember")
    public RequestStatus allMember() {
        try {
            List<CompanyMemberDto> allMember = companyMemberService.findAllMember();
            RequestStatus success = new RequestStatus("success", null
                , "200 OK", allMember);
            return success;
        } catch (SQLException e) {
            RequestStatus fail = new RequestStatus("fail", "SQLException"
                , "500 Internal Server Error", null);
            return fail;
        }
    }
}
