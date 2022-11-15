package com.example.apideliveryservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.controllerExceptionAdvice.GeneralMemberControllerExceptionAdvice;
import com.example.apideliveryservice.dto.GeneralMemberDto;
import com.example.apideliveryservice.dto.RequestGeneralMemberDto;
import com.example.apideliveryservice.dto.ResponseError;
import com.example.apideliveryservice.dto.ResponseGeneralMemberSuccess;
import com.example.apideliveryservice.interceptor.ExceptionResponseInterceptor;
import com.example.apideliveryservice.repository.GeneralMemberRepository;
import com.example.apideliveryservice.service.GeneralMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@ActiveProfiles("db-h2")
class GeneralMemberControllerTest {

    @Autowired
    GeneralMemberController controller;
    @Autowired
    GeneralMemberService service;
    @Autowired
    GeneralMemberRepository repository;
    @Autowired
    RepositoryResetHelper resetHelper;
    Connection connection;
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    String baseUrl;

    @BeforeEach
    void beforeEach() throws SQLException {
        baseUrl = "/api/delivery-service/general";
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new GeneralMemberControllerExceptionAdvice())
            .addInterceptors(new ExceptionResponseInterceptor())
            .build();

        connection = repository.connectJdbc();
        resetHelper.ifExistDeleteGeneralMembers(connection);
        resetHelper.createGeneralMembersTable(connection);
    }

    @Test
    @DisplayName("회원 가입 성공 Test")
    void joinMember1() throws Exception {
        //given
        String url = baseUrl + "/member/join";
        RequestGeneralMemberDto RequestGeneralMemberDto = new RequestGeneralMemberDto
            ("loginname", "aA!1111111111", "name");
        String requestJson = objectMapper.writeValueAsString(RequestGeneralMemberDto);
        ResponseGeneralMemberSuccess success
            = new ResponseGeneralMemberSuccess(201, null, null);
        String responseContent = objectMapper.writeValueAsString(success);
        //when
        //then
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestJson))
            .andExpect(status().isCreated())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("중복된 loginName 회원 가입 실패 Test")
    void joinMember2() throws Exception {
        //given
        String url = baseUrl + "/member/join";

        GeneralMemberDto firstSaveMember = new GeneralMemberDto(null, "loginname", "123123123"
            , "name1", false, new Timestamp(System.currentTimeMillis()));
        repository.create(connection, firstSaveMember);

        RequestGeneralMemberDto RequestGeneralMemberDto = new RequestGeneralMemberDto
            ("loginname", "aA!123123123", "name2");
        String requestJson = objectMapper.writeValueAsString(RequestGeneralMemberDto);

        ResponseError error
            = new ResponseError("/errors/general/member/join/duplicate-login-name"
            , "DeliveryServiceException", 409
            , "general member join fail due to DuplicatedLoginName"
            , "/api/delivery-service/general/member/join");
        String responseContent = objectMapper.writeValueAsString(error);
        //when
        //then
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestJson))
            .andExpect(status().isConflict())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }


    @Test
    @DisplayName("validation loginName 5자이상 20자 이하 회원가입 실패 Test")
    void JoinMember3() throws Exception {
        //given
        String url = baseUrl + "/member/join";

        RequestGeneralMemberDto RequestGeneralMemberDto1 = new RequestGeneralMemberDto("        ",
            "aA!1234123414", "storeName");
        String requestJson1 = objectMapper.writeValueAsString(RequestGeneralMemberDto1);
        RequestGeneralMemberDto RequestGeneralMemberDto2 = new RequestGeneralMemberDto(
            "asdfasdfasdfasdfasdfadsf",
            "aA!1234123414", "storeName");
        String requestJson2 = objectMapper.writeValueAsString(RequestGeneralMemberDto2);
        ResponseError error = new ResponseError(
            "/errors/general/member/join/longinName-pattern"
            , "MethodArgumentNotValidException", 400,
            "requestGeneralMember loginName is 8 to 20 lowercase letters and numbers"
            , "/api/delivery-service/general/member/join");
        String responseContent = objectMapper.writeValueAsString(error);
        //when
        //then
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestJson1))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestJson2))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("validation password 소문자 대문자 특수문자 숫자 하나이상 포함 8자 이상 회원가입 실패 Test")
    void JoinMember4() throws Exception {
        //given
        String url = baseUrl + "/member/join";

        RequestGeneralMemberDto RequestGeneralMemberDto1 = new RequestGeneralMemberDto("longinname",
            "ad12344123123", "storeName");
        String requestJson1 = objectMapper.writeValueAsString(RequestGeneralMemberDto1);
        RequestGeneralMemberDto RequestGeneralMemberDto2 = new RequestGeneralMemberDto("longinname",
            "aA!2", "storeName");
        String requestJson2 = objectMapper.writeValueAsString(RequestGeneralMemberDto2);

        ResponseError error = new ResponseError(
            "/errors/general/member/join/password-pattern"
            , "MethodArgumentNotValidException", 400,
            "password is At least 8 characters, at least 1 uppercase"
                + ", lowercase, number, and special character each"
            , "/api/delivery-service/general/member/join");

        String responseContent = objectMapper.writeValueAsString(error);
        //when
        //then
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestJson1))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestJson2))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("validation name 공백이면 회원가입 실패 Test")
    void JoinMember5() throws Exception {
        //given
        String url = baseUrl + "/member/join";

        RequestGeneralMemberDto RequestGeneralMemberDto1 = new RequestGeneralMemberDto("longinname",
            "aA!123123123", "");
        String requestJson1 = objectMapper.writeValueAsString(RequestGeneralMemberDto1);
        RequestGeneralMemberDto RequestGeneralMemberDto2 = new RequestGeneralMemberDto("longinname",
            "aA!123123123", "   ");
        String requestJson2 = objectMapper.writeValueAsString(RequestGeneralMemberDto2);

        ResponseError error = new ResponseError(
            "/errors/general/member/join/name-blank"
            , "MethodArgumentNotValidException", 400,
            "requestGeneralMember name must not be blank"
            , "/api/delivery-service/general/member/join");

        String responseContent = objectMapper.writeValueAsString(error);
        //when
        //then
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestJson1))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestJson2))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("모든 멤버 가져오기 Test")
    void findAllMember() throws Exception {
        //given
        String url = baseUrl + "/member/all";

        GeneralMemberDto companyMemberDto1 = new GeneralMemberDto(1l, "loginName1", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        GeneralMemberDto companyMemberDto2 = new GeneralMemberDto(2l, "loginName2", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        repository.create(connection, companyMemberDto1);
        repository.create(connection, companyMemberDto2);
        List<GeneralMemberDto> allMember = service.findAllMember();

        ResponseGeneralMemberSuccess success = new ResponseGeneralMemberSuccess(200, allMember,
            null);
        String responseContent = objectMapper.writeValueAsString(success);
        //when

        //then
        mockMvc.perform(get(url))
            .andExpect(status().isOk())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("개인 멤버 정보 가져오기 성공 Test")
    void findMember1() throws Exception {
        //given
        String url = baseUrl + "/member/information";

        GeneralMemberDto firstSaveMember = new GeneralMemberDto(null, "loginName", "password"
            , "name1", false, new Timestamp(System.currentTimeMillis()));
        repository.create(connection, firstSaveMember);

        String findId = "1";
        GeneralMemberDto findMember = service.findById(findId);
        ResponseGeneralMemberSuccess success = new ResponseGeneralMemberSuccess(200
            , null, findMember);
        String responseContent = objectMapper.writeValueAsString(success);
        //when

        //then
        mockMvc.perform(get(url).param("memberId", findId))
            .andExpect(status().isOk())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("개인 멤버 정보 가져오기 존재하지 않는 id 실패 Test")
    void findMember2() throws Exception {
        //given
        String url = baseUrl + "/member/information";

        GeneralMemberDto firstSaveMember = new GeneralMemberDto(null, "loginName", "password"
            , "name1", false, new Timestamp(System.currentTimeMillis()));
        repository.create(connection, firstSaveMember);

        String findId = "2";
        ResponseError error
            = new ResponseError("/errors/general/member/find/non-exist"
            , "DeliveryServiceException", 404
            , "general member findById fail due to NonExistentMemberIdException"
            , "/api/delivery-service/general/member/information");
        String responseContent = objectMapper.writeValueAsString(error);
        //when

        //then
        mockMvc.perform(get(url).param("memberId", findId))
            .andExpect(status().isNotFound())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }
}