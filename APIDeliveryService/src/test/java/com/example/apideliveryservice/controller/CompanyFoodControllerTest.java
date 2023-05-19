package com.example.apideliveryservice.controller;

import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.ADD_ORDER_DUPLICATED_FOOD_NAME;
import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.ADD_ORDER_PRICE_NOT_DIGIT;
import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.ADD_ORDER_REQUEST_NAME_BLANK;
import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.ADD_ORDER_REQUEST_PRICE_BLANK;
import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.UPDATE_PRICE_REQUEST_PRICE_BLANK;
import static com.example.apideliveryservice.exception.CompanyFoodExceptionEnum.UPDATE_PRICE_REQUEST_PRICE_NOT_DIGIT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.apideliveryservice.controllerexceptionadvice.CompanyFoodControllerExceptionAdvice;
import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.dto.ResponseCompanyFoodSuccess;
import com.example.apideliveryservice.dto.ResponseError;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import com.example.apideliveryservice.interceptor.ExceptionResponseInterceptor;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import com.example.apideliveryservice.service.CompanyFoodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Slf4j
@ActiveProfiles("test")
class CompanyFoodControllerTest {

    @Autowired
    CompanyFoodController controller;
    @Autowired
    CompanyFoodRepository companyFoodRepository;
    @Autowired
    CompanyFoodService companyFoodService;
    @Autowired
    CompanyMemberRepository companyMemberRepository;
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    String baseUrl;

    @BeforeEach
    void beforeEach() {
        baseUrl = "/api/delivery-service/company";
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new CompanyFoodControllerExceptionAdvice())
            .addInterceptors(new ExceptionResponseInterceptor())
            .build();
    }

    @Test
    @DisplayName("음식 등록 성공 Test")
    void addFood1() throws Exception {
        //given
        String url = baseUrl + "/food/addFood";

        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        String requestJson = "{\"memberId\":" + saveCompanyMember.getId()
            + ", \"name\":\"참치김밥\""
            + ", \"price\":5000}";

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
    @DisplayName("request name 공백 음식 등록 실패 Test")
    void addFood2() throws Exception {
        //given
        String url = baseUrl +  "/food/addFood";

        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        String requestJson1 = "{\"memberId\":" + saveCompanyMember.getId()
            + ", \"name\":\"  \""
            + ", \"price\":5000}";

        String requestJson2 = "{\"memberId\":" + saveCompanyMember.getId()
            + ", \"name\":\"\""
            + ", \"price\":5000}";

        ResponseError error
            = new ResponseError(ADD_ORDER_REQUEST_NAME_BLANK.getErrorType()
            , ADD_ORDER_REQUEST_NAME_BLANK.getErrorTitle(), 400,
            ADD_ORDER_REQUEST_NAME_BLANK.getErrormessage()
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
    @DisplayName("음식 등록 실패 - request price blank")
    void addFood3_1() throws Exception {
        //given
        String url = baseUrl + "/food/addFood";

        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        String requestJson = "{\"memberId\":" + saveCompanyMember.getId()
            + ", \"name\":\"참치김밥\""
            + ", \"price\":\"\"}";

        ResponseError error
            = new ResponseError(ADD_ORDER_REQUEST_PRICE_BLANK.getErrorType()
            , ADD_ORDER_REQUEST_PRICE_BLANK.getErrorTitle(), 400
            , ADD_ORDER_REQUEST_PRICE_BLANK.getErrormessage()
            , "/api/delivery-service/company/food/addFood");
        String responseContent = objectMapper.writeValueAsString(error);

        //when
        //then
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("음식 등록 실패 - request price blank")
    void addFood3_2() throws Exception {
        //given
        String url = baseUrl + "/food/addFood";

        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        String requestJson = "{\"memberId\":" + saveCompanyMember.getId()
            + ", \"name\":\"참치김밥\""
            + ", \"price\":\" \"}";

        ResponseError error
            = new ResponseError(ADD_ORDER_REQUEST_PRICE_BLANK.getErrorType()
            , ADD_ORDER_REQUEST_PRICE_BLANK.getErrorTitle(), 400
            , ADD_ORDER_REQUEST_PRICE_BLANK.getErrormessage()
            , "/api/delivery-service/company/food/addFood");
        String responseContent = objectMapper.writeValueAsString(error);

        //when
        //then
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("음식 등록 실패 - 중복된 foodName(같은 memberId)")
    void addFood4() throws Exception {
        //given
        String url = baseUrl + "/food/addFood";

        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name",
                false));
        companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("3000")));


        String requestJson = "{\"memberId\":" + saveCompanyMember.getId()
            + ", \"name\":\"foodName\""
            + ", \"price\":\"4000\"}";

        ResponseError error = new ResponseError(
            ADD_ORDER_DUPLICATED_FOOD_NAME.getErrorType()
            , ADD_ORDER_DUPLICATED_FOOD_NAME.getErrorTitle(), 409
            , ADD_ORDER_DUPLICATED_FOOD_NAME.getErrormessage()
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
    @DisplayName("음식 등록 실패 - 숫자가 아닌 price")
    void addFood5() throws Exception {
        //given
        String url = baseUrl + "/food/addFood";

        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        String requestJson = "{\"memberId\":" + saveCompanyMember.getId()
            + ", \"name\":\"참치김밥\""
            + ", \"price\":\"notDigit\"}";

        ResponseError error
            = new ResponseError(ADD_ORDER_PRICE_NOT_DIGIT.getErrorType()
            , ADD_ORDER_PRICE_NOT_DIGIT.getErrorTitle(), 400
            , ADD_ORDER_PRICE_NOT_DIGIT.getErrormessage()
            , "/api/delivery-service/company/food/addFood");
        String responseContent = objectMapper.writeValueAsString(error);

        //when
        //then
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("memberId 별 음식 리스트 가져오기 Test")
    void allFood() throws Exception {
        //given
        String url = baseUrl + "/food/allFood";
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name",
                false));
        companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("3000")));
        companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName2", new BigDecimal("4000")));

        List<CompanyFoodDto> allFood = companyFoodService.findAllFoodByCompanyMemberId(
            saveCompanyMember.getId());
        ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(200, allFood, null);
        String responseContent = objectMapper.writeValueAsString(success);
        //when
        //then
        mockMvc.perform(get(url)
                .param("memberId", String.valueOf(saveCompanyMember.getId())))
            .andExpect(status().isOk())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("음식 정보 가져오기 성공 Test")
    void findFood1() throws Exception {
        //given
        String url = baseUrl + "/food/information";

        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name",
                false));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("3000")));

        CompanyFoodDto findCompanyFood = companyFoodService.findFoodById(saveCompanyFood.getId());
        ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(200, null,
            findCompanyFood);
        String responseContent = objectMapper.writeValueAsString(success);
        //when

        //then
        mockMvc.perform(get(url).param("foodId", String.valueOf(saveCompanyFood.getId())))
            .andExpect(status().isOk())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("음식 가격 update 성공")
    void updatePrice1() throws Exception {
        //given
        String url = baseUrl + "/food/update";

        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name",
                false));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("3000")));

        String requestJson = "{\"foodId\":" + saveCompanyFood.getId()
            + ", \"price\": 5000}";
        ResponseCompanyFoodSuccess success = new ResponseCompanyFoodSuccess(200, null, null);
        String responseContent = objectMapper.writeValueAsString(success);
        //when

        //then
        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType("application/json")
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("음식 가격 update 실패 - request price blank Test")
    void updatePrice2() throws Exception {
        //given
        String url = baseUrl + "/food/update";

        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name",
                false));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("3000")));

        String requestJson = "{\"foodId\":" + saveCompanyFood.getId()
            + ", \"price\": \"\"}";

        ResponseError error = new ResponseError(
            UPDATE_PRICE_REQUEST_PRICE_BLANK.getErrorType()
            , UPDATE_PRICE_REQUEST_PRICE_BLANK.getErrorTitle(), 400
            , UPDATE_PRICE_REQUEST_PRICE_BLANK.getErrormessage()
            , "/api/delivery-service/company/food/update");
        String responseContent = objectMapper.writeValueAsString(error);
        //when

        //then
        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType("application/json")
                .content(requestJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("음식 가격 update 실패 - request price not digit Test")
    void updatePrice3() throws Exception {
        //given
        String url = baseUrl + "/food/update";

        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name",
                false));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("3000")));

        String requestJson = "{\"foodId\":" + saveCompanyFood.getId()
            + ", \"price\": \"notDigit\"}";

        ResponseError error = new ResponseError(
            UPDATE_PRICE_REQUEST_PRICE_NOT_DIGIT.getErrorType()
            , UPDATE_PRICE_REQUEST_PRICE_NOT_DIGIT.getErrorTitle(), 400
            , UPDATE_PRICE_REQUEST_PRICE_NOT_DIGIT.getErrormessage()
            , "/api/delivery-service/company/food/update");
        String responseContent = objectMapper.writeValueAsString(error);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType("application/json")
                .content(requestJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(responseContent))
            .andDo(log());
    }
}