package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.service.CompanyJoinService;
import com.example.apideliveryservice.service.DuplicatedLoginNameException;
import com.example.apideliveryservice.service.DuplicatedNameException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    private final CompanyJoinService companyJoinService;

    @PostMapping("/join")
    public JoinStatus joinMember(@RequestBody HashMap<String, String> requestJson) {
        CompanyMemberDto companyMemberDto = new CompanyMemberDto(null, requestJson.get("loginName"),
            requestJson.get("password"), requestJson.get("name"), 0
            , LocalDateTime.now());
        try {
            companyJoinService.join(companyMemberDto);
            JoinStatus success = new JoinStatus("success", null
                , "201 Created");
            return success;
        } catch (SQLException e) {
            JoinStatus fail = new JoinStatus("fail", "SQLException"
                , "500 Internal Server Error");
            return fail;
        } catch (DuplicatedLoginNameException e) {
            log.info("duplicatedLonginNameError", e);
            JoinStatus fail = new JoinStatus("fail"
                , "duplicatedLoginNameException", "409 Conflict");
            return fail;
        } catch (DuplicatedNameException e) {
            log.info("duplicatedNameError", e);
            JoinStatus fail = new JoinStatus("fail", "duplicatedNameException"
                , "409 Conflict");
            return fail;
        }
    }
}
