package com.example.apideliveryservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.controllerExceptionAdvice.CompanyFoodControllerExceptionAdvice;
import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.dto.RequestCompanyFoodDto;
import com.example.apideliveryservice.dto.RequestCompanyFoodPriceDto;
import com.example.apideliveryservice.dto.ResponseCompanyFoodError;
import com.example.apideliveryservice.dto.ResponseCompanyFoodSuccess;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import com.example.apideliveryservice.service.CompanyFoodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
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

        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(
            CompanyFoodControllerExceptionAdvice.class).build();

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

        CompanyFoodDto firstSaveFood = new CompanyFoodDto(null, 1l, "foodName",
            new BigDecimal("3000"));
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
    @DisplayName("이름 공백 회원 가입 실패 Test")
    void joinMember3() throws Exception {
        //given
        String url = baseUrl + "/food/addFood";

        RequestCompanyFoodDto requestCompanyFoodDto1 = new RequestCompanyFoodDto(
            "1", "", "5000");
        String requestJson1 = objectMapper.writeValueAsString(requestCompanyFoodDto1);
        RequestCompanyFoodDto requestCompanyFoodDto2 = new RequestCompanyFoodDto(
            "1", "   ", "50000");
        String requestJson2 = objectMapper.writeValueAsString(requestCompanyFoodDto2);

        ResponseCompanyFoodError error
            = new ResponseCompanyFoodError("/errors/food/add/name-blank"
            , "MethodArgumentNotValidException", 400, "requestCompanyFood name must not be blank"
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
    }

    @Test
    @DisplayName("숫자가 price 회원 가입 실패 Test")
    void joinMember4() throws Exception {
        //given
        String url = baseUrl + "/food/addFood";

        RequestCompanyFoodDto requestCompanyFoodDto1 = new RequestCompanyFoodDto(
            "1", "foodName", "notDigit");
        String requestJson1 = objectMapper.writeValueAsString(requestCompanyFoodDto1);

        ResponseCompanyFoodError error = new ResponseCompanyFoodError(
            "/errors/food/add/price-notDigit", "MethodArgumentNotValidException", 400,
            "requestCompanyFood price must be digit"
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
    }

    @Test
    @DisplayName("memberId 별 음식 리스트 가져오기 Test")
    void allFood() throws Exception {
        //given
        String url = baseUrl + "/food/allFood";
        CompanyFoodDto food1 = new CompanyFoodDto(1l, 1l, "ramen", new BigDecimal("3000"));
        CompanyFoodDto food2 = new CompanyFoodDto(2l, 1l, "spaghetti", new BigDecimal("4000"));
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

    @Test
    @DisplayName("음식 정보 가져오기 성공 Test")
    void findFood1() throws Exception {
        //given
        String url = baseUrl + "/food/information";

        CompanyFoodDto saveFood = new CompanyFoodDto(null, 1l, "foodName", new BigDecimal("3000"));
        repository.add(connection, saveFood);

        String foodId = "1";
        CompanyFoodDto findFood = service.findFood(foodId);
        ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(200, null,
            findFood);
        String responseContent = objectMapper.writeValueAsString(success);
        //when

        //then
        mockMvc.perform(get(url).param("foodId", foodId))
            .andExpect(status().isOk())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("음식 정보 가져오기 실패 Test")
    void findFood2() throws Exception {
        //given
        String url = baseUrl + "/food/information";

        String foodId = "1";

        ResponseCompanyFoodError error = new ResponseCompanyFoodError("/errors/food/find/no-id"
            , "NonExistentFoodIdException", 404, "find food fail due to no exist food id"
            , "/api/delivery-service/company/food/information");
        String responseContent = objectMapper.writeValueAsString(error);
        //when

        //then
        mockMvc.perform(get(url).param("foodId", foodId))
            .andExpect(status().isNotFound())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("음식 가격 update 성공 Test")
    void updatePrice1() throws Exception {
        //given
        String url = baseUrl + "/food/update";

        RequestCompanyFoodPriceDto request = new RequestCompanyFoodPriceDto("1", "5000");
        String requestJson = objectMapper.writeValueAsString(request);
        ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(200, null, null);
        String responseContent = objectMapper.writeValueAsString(success);
        //when

        //then
        mockMvc.perform(put(url)
                .contentType("application/json")
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("음식 가격 update 실패 빈 price request Test")
    void updatePrice2() throws Exception {
        //given
        String url = baseUrl + "/food/update";

        RequestCompanyFoodPriceDto request = new RequestCompanyFoodPriceDto("1", "notDigit");
        String requestJson = objectMapper.writeValueAsString(request);
        ResponseCompanyFoodError error = new ResponseCompanyFoodError(
            "/errors/food/update/price-notDigit"
            , "MethodArgumentNotValidException", 400, "requestCompanyFoodPrice price must be digit"
            , "/api/delivery-service/company/food/update");
        String responseContent = objectMapper.writeValueAsString(error);
        //when

        //then
        mockMvc.perform(put(url)
                .contentType("application/json")
                .content(requestJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }
}