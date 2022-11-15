package com.example.apideliveryservice.controller;

import com.example.apideliveryservice.dto.RequestGeneralMemberDto;
import com.example.apideliveryservice.dto.ResponseGeneralMemberSuccess;
import com.example.apideliveryservice.service.GeneralMemberService;
import com.example.apideliveryservice.threadLocalStorage.ThreadLocalStorage;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/delivery-service/general")
@RequiredArgsConstructor
public class GeneralMemberController {

    private final GeneralMemberService generalMemberService;
    private ThreadLocalStorage threadLocalStorage = new ThreadLocalStorage();

    @PostMapping("/member/join")
    public ResponseEntity joinMember(
        @Validated @RequestBody RequestGeneralMemberDto requestGeneralMember) throws SQLException {
        generalMemberService.join(requestGeneralMember.getLoginName(),
            requestGeneralMember.getPassword(), requestGeneralMember.getName());
        ResponseGeneralMemberSuccess success = new ResponseGeneralMemberSuccess(201, null, null);
        return new ResponseEntity(success, HttpStatus.CREATED);
    }
}
