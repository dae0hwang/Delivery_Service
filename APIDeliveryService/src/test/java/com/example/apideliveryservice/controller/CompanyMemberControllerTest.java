package com.example.apideliveryservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.controllerexceptionadvice.CompanyMemberControllerExceptionAdvice;
import com.example.apideliveryservice.dto.RequestCompanyMember;
import com.example.apideliveryservice.dto.ResponseCompanyMemberSuccess;
import com.example.apideliveryservice.interceptor.ExceptionResponseInterceptor;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import com.example.apideliveryservice.service.CompanyMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@Slf4j
@ActiveProfiles("jpa-h2")
class CompanyMemberControllerTest {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    @Autowired
    CompanyMemberController controller;
    @Autowired
    CompanyMemberRepository repository;
    @Autowired
    RepositoryResetHelper resetHelper;
    @Autowired
    CompanyMemberService service;
    Connection connection;
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    String baseUrl;
//    EntityManagerFactory emf;
//    EntityManager em;
//    EntityTransaction tx;

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL", "sa", "");
        resetHelper.ifExistDeleteCompanyMembers(connection);
        resetHelper.createCompanyMembersTable(connection);

        baseUrl = "/api/delivery-service/company";
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new CompanyMemberControllerExceptionAdvice())
            .addInterceptors(new ExceptionResponseInterceptor()).build();

//        emf = Persistence.createEntityManagerFactory(persistenceName);
//        em = emf.createEntityManager();
//        tx = em.getTransaction();
    }

    @Test
    @DisplayName("회원 가입 성공 Test")
    void joinMember1() throws Exception {
        //given
        String url = baseUrl + "/member/join";
        RequestCompanyMember requestCompanyMemberDto = new RequestCompanyMember("loginname1",
            "aA!1111111111", "name");
        String requestJson = objectMapper.writeValueAsString(requestCompanyMemberDto);
        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(201, null, null);
        String responseContent = objectMapper.writeValueAsString(success);
        //when
        //then
        mockMvc.perform(post(url).contentType("application/json").content(requestJson))
            .andExpect(status().isCreated()).andExpect(content().json(responseContent))
            .andDo(log());
    }

//    @Test
//    @DisplayName("중복된 loginName 회원 가입 실패 Test")
//    void joinMember2() throws Exception {
//        //given
//        String url = baseUrl + "/member/join";
//
//        CompanyMemberDto firstSaveMember = new CompanyMemberDto(null, "loginId", "123123123",
//            "name1", false, new Timestamp(System.currentTimeMillis()));
//        tx.begin();
//        repository.save(em, firstSaveMember);
//        tx.commit();
//
//        RequestCompanyMemberDto requestCompanyMemberDto = new RequestCompanyMemberDto("loginname",
//            "aA!123123123", "name2");
//        String requestJson = objectMapper.writeValueAsString(requestCompanyMemberDto);
//
//        ResponseError error = new ResponseError("/errors/member/join/duplicate-login-name",
//            "DuplicatedLoginNameException", 409,
//            "Company member join fail due to DuplicatedLoginName",
//            "/api/delivery-service/company/member/join");
//        String responseContent = objectMapper.writeValueAsString(error);
//        //when
//        //then
//        mockMvc.perform(post(url).contentType("application/json").content(requestJson))
//            .andExpect(status().isConflict()).andExpect(content().json(responseContent))
//            .andDo(log());
//    }
//
//    @Test
//    @DisplayName("validation loginName 5자이상 20자 이하 회원가입 실패 Test")
//    void JoinMember3() throws Exception {
//        //given
//        String url = baseUrl + "/member/join";
//
//        RequestCompanyMemberDto requestCompanyMemberDto1 = new RequestCompanyMemberDto("        ",
//            "aA!1234123414", "storeName");
//        String requestJson1 = objectMapper.writeValueAsString(requestCompanyMemberDto1);
//        RequestCompanyMemberDto requestCompanyMemberDto2 = new RequestCompanyMemberDto(
//            "asdfasdfasdfasdfasdfadsf", "aA!1234123414", "storeName");
//        String requestJson2 = objectMapper.writeValueAsString(requestCompanyMemberDto2);
//        ResponseError error = new ResponseError("/errors/member/join/longinName-pattern",
//            "MethodArgumentNotValidException", 400,
//            "requestCompanyMember loginName is 8 to 20 lowercase letters and numbers",
//            "/api/delivery-service/company/member/join");
//        String responseContent = objectMapper.writeValueAsString(error);
//        //when
//        //then
//        mockMvc.perform(post(url).contentType("application/json").content(requestJson1))
//            .andExpect(status().isBadRequest()).andExpect(content().json(responseContent))
//            .andDo(log());
//        mockMvc.perform(post(url).contentType("application/json").content(requestJson2))
//            .andExpect(status().isBadRequest()).andExpect(content().json(responseContent))
//            .andDo(log());
//    }
//
//    @Test
//    @DisplayName("validation password 소문자 대문자 특수문자 숫자 하나이상 포함 8자 이상 회원가입 실패 Test")
//    void JoinMember4() throws Exception {
//        //given
//        String url = baseUrl + "/member/join";
//
//        RequestCompanyMemberDto requestCompanyMemberDto1 = new RequestCompanyMemberDto("longinname",
//            "ad12344123123", "storeName");
//        String requestJson1 = objectMapper.writeValueAsString(requestCompanyMemberDto1);
//        RequestCompanyMemberDto requestCompanyMemberDto2 = new RequestCompanyMemberDto("longinname",
//            "aA!2", "storeName");
//        String requestJson2 = objectMapper.writeValueAsString(requestCompanyMemberDto2);
//
//        ResponseError error = new ResponseError("/errors/member/join/password-pattern",
//            "MethodArgumentNotValidException", 400,
//            "requestCompanyMember password is At least 8 characters, at least 1 uppercase"
//                + ", lowercase, number, and special character each",
//            "/api/delivery-service/company/member/join");
//
//        String responseContent = objectMapper.writeValueAsString(error);
//        //when
//        //then
//        mockMvc.perform(post(url).contentType("application/json").content(requestJson1))
//            .andExpect(status().isBadRequest()).andExpect(content().json(responseContent))
//            .andDo(log());
//        mockMvc.perform(post(url).contentType("application/json").content(requestJson2))
//            .andExpect(status().isBadRequest()).andExpect(content().json(responseContent))
//            .andDo(log());
//    }
//
//    @Test
//    @DisplayName("validation name 공백이면 회원가입 실패 Test")
//    void JoinMember5() throws Exception {
//        //given
//        String url = baseUrl + "/member/join";
//
//        RequestCompanyMemberDto requestCompanyMemberDto1 = new RequestCompanyMemberDto("longinname",
//            "aA!123123123", "");
//        String requestJson1 = objectMapper.writeValueAsString(requestCompanyMemberDto1);
//        RequestCompanyMemberDto requestCompanyMemberDto2 = new RequestCompanyMemberDto("longinname",
//            "aA!123123123", "   ");
//        String requestJson2 = objectMapper.writeValueAsString(requestCompanyMemberDto2);
//
//        ResponseError error = new ResponseError("/errors/member/join/name-blank",
//            "MethodArgumentNotValidException", 400, "requestCompanyMember name must not be blank",
//            "/api/delivery-service/company/member/join");
//
//        String responseContent = objectMapper.writeValueAsString(error);
//        //when
//        //then
//        mockMvc.perform(post(url).contentType("application/json").content(requestJson1))
//            .andExpect(status().isBadRequest()).andExpect(content().json(responseContent))
//            .andDo(log());
//        mockMvc.perform(post(url).contentType("application/json").content(requestJson2))
//            .andExpect(status().isBadRequest()).andExpect(content().json(responseContent))
//            .andDo(log());
//    }
//
//    @Test
//    @DisplayName("모든 멤버 가져오기 Test")
//    void findAllMember() throws Exception {
//        //given
//        String url = baseUrl + "/member/allMember";
//
//        CompanyMemberDto companyMemberDto1 = new CompanyMemberDto(1l, "loginId1", "password",
//            "name", false, new Timestamp(System.currentTimeMillis()));
//        CompanyMemberDto companyMemberDto2 = new CompanyMemberDto(2l, "loginId2", "password",
//            "name", false, new Timestamp(System.currentTimeMillis()));
//        tx.begin();
//        repository.save(em, companyMemberDto1);
//        repository.save(em, companyMemberDto2);
//        tx.commit();
//        List<CompanyMemberDto> allMember = service.findAllMember();
//
//        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(200, allMember,
//            null);
//        String responseContent = objectMapper.writeValueAsString(success);
//        //when
//
//        //then
//        mockMvc.perform(get(url)).andExpect(status().isOk())
//            .andExpect(content().json(responseContent)).andDo(log());
//    }
//
//    @Test
//    @DisplayName("개인 멤버 정보 가져오기 성공 Test")
//    void findMember1() throws Exception {
//        //given
//        String url = baseUrl + "/member/information";
//
//        CompanyMemberDto firstSaveMember = new CompanyMemberDto(null, "loginId", "password",
//            "name1", false, new Timestamp(System.currentTimeMillis()));
//        tx.begin();
//        repository.save(em, firstSaveMember);
//        tx.commit();
//        String findId = "1";
//        CompanyMemberDto findMember = service.findMember(findId);
//        ResponseCompanyMemberSuccess success = new ResponseCompanyMemberSuccess(200, null,
//            findMember);
//        String responseContent = objectMapper.writeValueAsString(success);
//        //when
//
//        //then
//        mockMvc.perform(get(url).param("memberId", findId)).andExpect(status().isOk())
//            .andExpect(content().json(responseContent)).andDo(log());
//    }
//
//    @Test
//    @DisplayName("개인 멤버 정보 가져오기 존재하지 않는 id 실패 Test")
//    void findMember2() throws Exception {
//        //given
//        String url = baseUrl + "/member/information";
//
//        CompanyMemberDto firstSaveMember = new CompanyMemberDto(null, "loginId", "password",
//            "name1", false, new Timestamp(System.currentTimeMillis()));
//        tx.begin();
//        repository.save(em, firstSaveMember);
//        tx.commit();
//        String findId = "2";
//        ResponseError error = new ResponseError("/errors/member/find/non-exist",
//            "NonExistentMemberIdException", 404,
//            "Company member find fail due to no exist member Id",
//            "/api/delivery-service/company/member/information");
//        String responseContent = objectMapper.writeValueAsString(error);
//        //when
//
//        //then
//        mockMvc.perform(get(url).param("memberId", findId)).andExpect(status().isNotFound())
//            .andExpect(content().json(responseContent)).andDo(log());
//    }
}