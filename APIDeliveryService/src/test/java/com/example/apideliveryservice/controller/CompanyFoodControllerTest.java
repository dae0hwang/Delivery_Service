package com.example.apideliveryservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.dto.RequestCompanyFoodDto;
import com.example.apideliveryservice.dto.ResponseCompanyFoodError;
import com.example.apideliveryservice.dto.ResponseCompanyFoodSuccess;
import com.example.apideliveryservice.dto.ResponseCompanyMemberSuccess;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import com.example.apideliveryservice.service.CompanyFoodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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

@SpringBootTest
@Slf4j
@ActiveProfiles("db-h2")
class CompanyFoodControllerTest {

    @Autowired
    CompanyFoodController controller;
    @Autowired
    CompanyFoodRepository repository;
    @Autowired
    RepositoryResetHelper resetHelper;
    @Autowired
    CompanyFoodService service;
    Connection connection;
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    String baseUrl;

    @BeforeEach
    void beforeEach() throws SQLException {
        baseUrl = "/api/delivery-service/company";
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        connection = repository.connectJdbc();
        resetHelper.ifExistDeleteCompanyFood(connection);
        resetHelper.createCompanyFoodTable(connection);
    }

    @Test
    @DisplayName("음식 등록 성공 Test")
    void addFood1() throws Exception {
        //given
        String url = baseUrl + "/food/addFood";
        RequestCompanyFoodDto requestCompanyFoodDto = new RequestCompanyFoodDto(
            "1", "foodName", "3000");
        String requestJson = objectMapper.writeValueAsString(requestCompanyFoodDto);
        ResponseCompanyFoodSuccess success
            = new ResponseCompanyFoodSuccess(201, null, null);
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
    @DisplayName("중복된 foodName(같은 memberId에서) 회원 가입 실패 Test")
    void addFood2() throws Exception {
        //given
        String url = baseUrl + "/food/addFood";

        CompanyFoodDto firstSaveFood = new CompanyFoodDto(null, new BigInteger("1")
            , "foodName", new BigDecimal("3000"));
        repository.add(connection, firstSaveFood);

        RequestCompanyFoodDto requestCompanyFoodDto = new RequestCompanyFoodDto(
            "1", "foodName", "5000");
        String requestJson = objectMapper.writeValueAsString(requestCompanyFoodDto);

        ResponseCompanyFoodError error = new ResponseCompanyFoodError(
            "/errors/food/add/duplicate-name"
            , "DuplicatedFoodNameException", 409
            , "company food add fail due to duplicated name"
            , "/api/delivery-service/company/food/addFood");
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
    @DisplayName("공백 input 회원 가입 실패 Test")
    void joinMember3() throws Exception {
        //given
        String url = baseUrl + "/food/addFood";

        RequestCompanyFoodDto requestCompanyFoodDto1 = new RequestCompanyFoodDto(
            "", "foodName", "5000");
        String requestJson1 = objectMapper.writeValueAsString(requestCompanyFoodDto1);
        RequestCompanyFoodDto requestCompanyFoodDto2 = new RequestCompanyFoodDto(
            "1", "", "5000");
        String requestJson2 = objectMapper.writeValueAsString(requestCompanyFoodDto2);
        RequestCompanyFoodDto requestCompanyFoodDto3 = new RequestCompanyFoodDto(
            "1", "foodName", "");
        String requestJson3 = objectMapper.writeValueAsString(requestCompanyFoodDto3);

        ResponseCompanyFoodError error
            = new ResponseCompanyFoodError("/errors/food/add/blank-input"
            , "BlackException", 400, "company food add fail due to blank input"
            , "/api/delivery-service/company/food/addFood");
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
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestJson3))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("memberId 별 음식 리스트 가져오기 Test")
    void allFood() throws Exception {
        //given
        String url = baseUrl + "/food/allFood";
        CompanyFoodDto food1 = new CompanyFoodDto(new BigInteger("1"), new BigInteger("1")
            , "ramen", new BigDecimal("3000"));
        CompanyFoodDto food2 = new CompanyFoodDto(new BigInteger("2"), new BigInteger("1")
            , "spaghetti", new BigDecimal("4000"));
        repository.add(connection, food1);
        repository.add(connection, food2);
        List<CompanyFoodDto> list = new ArrayList<>();
        list.add(food1);
        list.add(food2);
        ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(200, list, null);
        String responseContent = objectMapper.writeValueAsString(success);
        //when
        //then
        mockMvc.perform(get(url)
                .param("memberId", "1"))
            .andExpect(status().isOk())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }
}