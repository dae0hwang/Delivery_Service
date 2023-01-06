package com.example.apideliveryservice.controller;

import static com.example.apideliveryservice.exception.CompanyMemberExceptionEnum.COMPANY_JOIN_DUPLICATED_LOGIN_NAME;
import static com.example.apideliveryservice.exception.CompanyMemberExceptionEnum.COMPANY_JOIN_LOGIN_NAME_VALIDATION;
import static com.example.apideliveryservice.exception.CompanyMemberExceptionEnum.COMPANY_JOIN_NAME_VALIDATION;
import static com.example.apideliveryservice.exception.CompanyMemberExceptionEnum.COMPANY_JOIN_PASSWORD_VALIDATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.apideliveryservice.controllerexceptionadvice.CompanyMemberControllerExceptionAdvice;
import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.dto.ResponseCompanyMemberSuccess;
import com.example.apideliveryservice.dto.ResponseError;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import com.example.apideliveryservice.interceptor.ExceptionResponseInterceptor;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import com.example.apideliveryservice.service.CompanyMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
@ActiveProfiles("test")
class CompanyMemberControllerTest {

    @Autowired
    CompanyMemberController controller;
    @Autowired
    CompanyMemberRepository companyMemberRepository;
    @Autowired
    CompanyMemberService companyMemberService;
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    String baseUrl;

    @BeforeEach
    void beforeEach() {
        baseUrl = "/api/delivery-service/company";
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new CompanyMemberControllerExceptionAdvice())
            .addInterceptors(new ExceptionResponseInterceptor()).build();
    }

    @Test
    @DisplayName("회원 가입 성공 Test")
    void joinMember1() throws Exception {
        //given
        String url = baseUrl + "/member/join";

        String requestJson = "{\"loginName\":\"asdfgge2233\", \"password\":\"AsS@!#12344\""
        + ", \"name\":\"name\"}";
        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(201, null, null);
        String responseContent = objectMapper.writeValueAsString(success);
        //when
        //then
        mockMvc.perform(post(url).contentType("application/json").content(requestJson))
            .andExpect(status().isCreated()).andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("중복된 loginName 회원 가입 실패 Test")
    void joinMember2() throws Exception {
        //given
        String url = baseUrl + "/member/join";

        companyMemberRepository.save(
            new CompanyMemberEntity("asddf21213", "password", "name", false));

        String requestJson = "{\"loginName\":\"asddf21213\", \"password\":\"AsS@!#12344\""
            + ", \"name\":\"name\"}";

        ResponseError error
            = new ResponseError(COMPANY_JOIN_DUPLICATED_LOGIN_NAME.getErrorType()
            , COMPANY_JOIN_DUPLICATED_LOGIN_NAME.getErrorTitle(), 409,
            COMPANY_JOIN_DUPLICATED_LOGIN_NAME.getErrormessage()
            , "/api/delivery-service/company/member/join");
        String responseContent = objectMapper.writeValueAsString(error);
        //when
        //then
        mockMvc.perform(post(url).contentType("application/json").content(requestJson))
            .andExpect(status().isConflict()).andExpect(content().json(responseContent))
            .andDo(log());
    }


    @Test
    @DisplayName("validation loginName 5자이상 20자 이하 회원가입 실패 Test")
    void JoinMember3() throws Exception {
        //given
        String url = baseUrl + "/member/join";

        String requestJson1 = "{\"loginName\":\"      \", \"password\":\"AsS@!#12344\""
            + ", \"name\":\"name\"}";
        String requestJson2 = "{\"loginName\":\"asdfasdfasdfasdfasdfadsf\", \"password\":\"AsS@!#12344\""
            + ", \"name\":\"name\"}";

        ResponseError error = new ResponseError(COMPANY_JOIN_LOGIN_NAME_VALIDATION.getErrorType(),
            COMPANY_JOIN_LOGIN_NAME_VALIDATION.getErrorTitle(), 400,
            COMPANY_JOIN_LOGIN_NAME_VALIDATION.getErrormessage(),
            "/api/delivery-service/company/member/join");
        String responseContent = objectMapper.writeValueAsString(error);
        //when
        //then
        mockMvc.perform(post(url).contentType("application/json").content(requestJson1))
            .andExpect(status().isBadRequest()).andExpect(content().json(responseContent))
            .andDo(log());
        mockMvc.perform(post(url).contentType("application/json").content(requestJson2))
            .andExpect(status().isBadRequest()).andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("validation password 소문자 대문자 특수문자 숫자 하나이상 포함 8자 이상 회원가입 실패 Test")
    void JoinMember4() throws Exception {
        //given
        String url = baseUrl + "/member/join";

        String requestJson1 = "{\"loginName\":\"asddf21213\", \"password\":\"ad12344123123\""
            + ", \"name\":\"name\"}";
        String requestJson2 = "{\"loginName\":\"asddf21213\", \"password\":\"aA!2\""
            + ", \"name\":\"name\"}";

        ResponseError error = new ResponseError(COMPANY_JOIN_PASSWORD_VALIDATION.getErrorType(),
            COMPANY_JOIN_PASSWORD_VALIDATION.getErrorTitle(), 400,
            COMPANY_JOIN_PASSWORD_VALIDATION.getErrormessage(),
            "/api/delivery-service/company/member/join");
        String responseContent = objectMapper.writeValueAsString(error);

        //when
        //then
        mockMvc.perform(post(url).contentType("application/json").content(requestJson1))
            .andExpect(status().isBadRequest()).andExpect(content().json(responseContent))
            .andDo(log());
        mockMvc.perform(post(url).contentType("application/json").content(requestJson2))
            .andExpect(status().isBadRequest()).andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("validation name 공백이면 회원가입 실패 Test")
    void JoinMember5() throws Exception {
        //given
        String url = baseUrl + "/member/join";

        String requestJson1 = "{\"loginName\":\"asddf21213\", \"password\":\"aA!123123123\""
            + ", \"name\":\"\"}";
        String requestJson2 = "{\"loginName\":\"asddf21213\", \"password\":\"aA!123123123\""
            + ", \"name\":\"  \"}";

        ResponseError error = new ResponseError(COMPANY_JOIN_NAME_VALIDATION.getErrorType(),
            COMPANY_JOIN_NAME_VALIDATION.getErrorTitle(), 400, COMPANY_JOIN_NAME_VALIDATION.getErrormessage(),
            "/api/delivery-service/company/member/join");

        String responseContent = objectMapper.writeValueAsString(error);
        //when
        //then
        mockMvc.perform(post(url).contentType("application/json").content(requestJson1))
            .andExpect(status().isBadRequest()).andExpect(content().json(responseContent))
            .andDo(log());
        mockMvc.perform(post(url).contentType("application/json").content(requestJson2))
            .andExpect(status().isBadRequest()).andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("모든 멤버 가져오기 Test")
    void findAllMember() throws Exception {
        //given
        String url = baseUrl + "/member/allMember";

        companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        companyMemberRepository.save(
            new CompanyMemberEntity("loginName2", "password2", "name2", false));

        List<CompanyMemberDto> allMember = companyMemberService.findAllMember();

        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(200, allMember,
            null);
        String responseContent = objectMapper.writeValueAsString(success);
        //when

        //then
        mockMvc.perform(get(url)).andExpect(status().isOk())
            .andExpect(content().json(responseContent)).andDo(log());
    }

    @Test
    @DisplayName("개인 멤버 정보 가져오기 성공 Test")
    void findMember1() throws Exception {
        //given
        String url = baseUrl + "/member/information";

        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));

        CompanyMemberDto findCompanyMember = companyMemberService.findMember(
            saveCompanyMember.getId());
        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(200, null,
            findCompanyMember);
        String responseContent = objectMapper.writeValueAsString(success);
        //when

        //then
        mockMvc.perform(get(url).param("memberId", String.valueOf(saveCompanyMember.getId())))
            .andExpect(status().isOk())
            .andExpect(content().json(responseContent)).andDo(log());
    }
}