package com.example.apideliveryservice.controller;

import static com.example.apideliveryservice.exception.GeneralMemberExceptionEnum.GENERAL_JOIN_DUPLICATED_LOGIN_NAME;
import static com.example.apideliveryservice.exception.GeneralMemberExceptionEnum.GENERAL_JOIN_LOGIN_NAME_VALIDATION;
import static com.example.apideliveryservice.exception.GeneralMemberExceptionEnum.GENERAL_JOIN_NAME_VALIDATION;
import static com.example.apideliveryservice.exception.GeneralMemberExceptionEnum.GENERAL_JOIN_PASSWORD_VALIDATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.apideliveryservice.controllerexceptionadvice.GeneralMemberControllerExceptionAdvice;
import com.example.apideliveryservice.dto.GeneralMemberDto;
import com.example.apideliveryservice.dto.ResponseError;
import com.example.apideliveryservice.dto.ResponseGeneralMemberSuccess;
import com.example.apideliveryservice.entity.GeneralMemberEntity;
import com.example.apideliveryservice.interceptor.ExceptionResponseInterceptor;
import com.example.apideliveryservice.repository.GeneralMemberRepository;
import com.example.apideliveryservice.service.GeneralMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
@Transactional
@ActiveProfiles("test")
class GeneralMemberControllerTest {

    @Autowired
    GeneralMemberController controller;
    @Autowired
    GeneralMemberService generalMemberService;
    @Autowired
    GeneralMemberRepository generalMemberRepository;
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    String baseUrl;

    @BeforeEach
    void beforeEach() {
        baseUrl = "/api/delivery-service/general";
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new GeneralMemberControllerExceptionAdvice())
            .addInterceptors(new ExceptionResponseInterceptor())
            .build();
    }

    @Test
    @DisplayName("회원 가입 성공 Test")
    void joinMember1() throws Exception {
        //given
        String url = baseUrl + "/member/join";

        String requestJson = "{\"loginName\":\"asdfgge2233\", \"password\":\"AsS@!#12344\""
            + ", \"name\":\"name\"}";
        ResponseGeneralMemberSuccess success = new ResponseGeneralMemberSuccess(201, null, null);
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

        generalMemberRepository.save(
            new GeneralMemberEntity("asddf21213", "password", "name", false));

        String requestJson = "{\"loginName\":\"asddf21213\", \"password\":\"AsS@!#12344\""
            + ", \"name\":\"name\"}";

        ResponseError error
            = new ResponseError(GENERAL_JOIN_DUPLICATED_LOGIN_NAME.getErrorType()
            , GENERAL_JOIN_DUPLICATED_LOGIN_NAME.getErrorTitle(), 409,
            GENERAL_JOIN_DUPLICATED_LOGIN_NAME.getErrormessage()
            , "/api/delivery-service/general/member/join");
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

        ResponseError error = new ResponseError(GENERAL_JOIN_LOGIN_NAME_VALIDATION.getErrorType(),
            GENERAL_JOIN_LOGIN_NAME_VALIDATION.getErrorTitle(), 400,
            GENERAL_JOIN_LOGIN_NAME_VALIDATION.getErrormessage(),
            "/api/delivery-service/general/member/join");
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

        ResponseError error = new ResponseError(GENERAL_JOIN_PASSWORD_VALIDATION.getErrorType(),
            GENERAL_JOIN_PASSWORD_VALIDATION.getErrorTitle(), 400,
            GENERAL_JOIN_PASSWORD_VALIDATION.getErrormessage(),
            "/api/delivery-service/general/member/join");
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

        ResponseError error = new ResponseError(GENERAL_JOIN_NAME_VALIDATION.getErrorType(),
            GENERAL_JOIN_NAME_VALIDATION.getErrorTitle(), 400,
            GENERAL_JOIN_NAME_VALIDATION.getErrormessage(),
            "/api/delivery-service/general/member/join");

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
        String url = baseUrl + "/member/all";

        generalMemberRepository.save(
            new GeneralMemberEntity("loginName", "password", "name", false));
        generalMemberRepository.save(
            new GeneralMemberEntity("loginName2", "password2", "name2", false));

        List<GeneralMemberDto> allMember = generalMemberService.findAllMember();

        ResponseGeneralMemberSuccess success = new ResponseGeneralMemberSuccess(200, allMember,
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

        GeneralMemberEntity saveGeneralMember = generalMemberRepository.save(
            new GeneralMemberEntity("loginName", "password", "name", false));

        GeneralMemberDto findGeneralMember = generalMemberService.findById(
            saveGeneralMember.getId());
        ResponseGeneralMemberSuccess success = new ResponseGeneralMemberSuccess(200, null,
            findGeneralMember);
        String responseContent = objectMapper.writeValueAsString(success);
        //when

        //then
        mockMvc.perform(get(url).param("memberId", String.valueOf(saveGeneralMember.getId())))
            .andExpect(status().isOk())
            .andExpect(content().json(responseContent)).andDo(log());
    }
}