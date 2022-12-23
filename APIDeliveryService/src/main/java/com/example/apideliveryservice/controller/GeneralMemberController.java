//package com.example.apideliveryservice.controller;
//
//import com.example.apideliveryservice.dto.GeneralMemberDto;
//import com.example.apideliveryservice.dto.RequestGeneralMember;
//import com.example.apideliveryservice.dto.ResponseGeneralMemberSuccess;
//import com.example.apideliveryservice.service.GeneralMemberService;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/delivery-service/general")
//@RequiredArgsConstructor
//public class GeneralMemberController {
//
//    private final GeneralMemberService generalMemberService;
//
//    @PostMapping("/member/join")
//    public ResponseEntity joinMember(
//        @Validated @RequestBody RequestGeneralMember requestGeneralMember) throws Exception {
//        generalMemberService.join(requestGeneralMember.getLoginName(),
//            requestGeneralMember.getPassword(), requestGeneralMember.getName());
//        ResponseGeneralMemberSuccess success = new ResponseGeneralMemberSuccess(201, null, null);
//        return new ResponseEntity(success, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/member/all")
//    public ResponseEntity findAllMember() throws Exception {
//        List<GeneralMemberDto> allMember = generalMemberService.findAllMember();
//        ResponseGeneralMemberSuccess success = new ResponseGeneralMemberSuccess(200, allMember,
//            null);
//        return ResponseEntity.status(HttpStatus.OK).body(success);
//    }
//
//    @GetMapping("/member/information")
//    public ResponseEntity findMember(@RequestParam("memberId") String memberId) throws Exception {
//        GeneralMemberDto findMember = generalMemberService.findById(memberId);
//        ResponseGeneralMemberSuccess success = new ResponseGeneralMemberSuccess(200, null,
//            findMember);
//        return ResponseEntity.status(HttpStatus.OK).body(success);
//    }
//}
