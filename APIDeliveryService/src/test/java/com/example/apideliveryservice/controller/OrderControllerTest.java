package com.example.apideliveryservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.apideliveryservice.dto.GeneralMemberOrderDto;
import com.example.apideliveryservice.dto.RequestOrder;
import com.example.apideliveryservice.dto.ResponseOrderSuccess;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import com.example.apideliveryservice.entity.GeneralMemberEntity;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import com.example.apideliveryservice.repository.GeneralMemberRepository;
import com.example.apideliveryservice.repository.OrderRepository;
import com.example.apideliveryservice.service.CompanyFoodService;
import com.example.apideliveryservice.service.CompanyMemberService;
import com.example.apideliveryservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
@ActiveProfiles("test")
class OrderControllerTest {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    @Autowired
    OrderController orderController;
    @Autowired
    CompanyFoodRepository companyFoodRepository;
    @Autowired
    CompanyMemberService companyMemberService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    CompanyFoodService companyFoodService;
    @Autowired
    CompanyMemberRepository companyMemberRepository;
    @Autowired
    GeneralMemberRepository generalMemberRepository;
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    String baseUrl;


    @BeforeEach
    void beforeEach() throws SQLException {
        baseUrl = "/api/delivery-service/order";
        objectMapper = new ObjectMapper();

        //validation 미적용
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

    }

    @Test
    @DisplayName("음식 구매 성공 Test")
    void orderFood() throws Exception {
        //given
        String url = baseUrl + "/member/order";

        GeneralMemberEntity saveGeneralMember = generalMemberRepository.save(
            new GeneralMemberEntity("loginName", "password", "name", false));
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("3000")));

        List<RequestOrder> requestList = Arrays.asList(new RequestOrder(saveGeneralMember.getId(),
            saveCompanyMember.getId(), saveCompanyFood.getId(), 5));

        String requestJson = objectMapper.writeValueAsString(requestList);
        ResponseOrderSuccess success = new ResponseOrderSuccess(201, null, null);
        String responseContent = objectMapper.writeValueAsString(success);
        //when
        //then
        mockMvc.perform(post(url).contentType("application/json").content(requestJson))
            .andExpect(status().isCreated()).andExpect(content().json(responseContent))
            .andDo(log());
    }

    @Test
    @DisplayName("generalMemberId별 구매내역 Test")
    void orderList() throws Exception {
        //given
        String url = baseUrl + "/member/order/list";

        GeneralMemberEntity saveGeneralMember = generalMemberRepository.save(
            new GeneralMemberEntity("loginName", "password", "name", false));
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("3000")));

        List<RequestOrder> requestList = Arrays.asList(new RequestOrder(saveGeneralMember.getId(),
            saveCompanyMember.getId(), saveCompanyFood.getId(), 5));
        orderService.addOrder(saveGeneralMember.getId(), requestList);


        List<GeneralMemberOrderDto> findOrderList = orderService.findOrderListByGeneralId(
            saveGeneralMember.getId());
        ResponseOrderSuccess success = new ResponseOrderSuccess(201, findOrderList,
            null);
        String responseContent = objectMapper.writeValueAsString(success);
        //when
        //then
        mockMvc.perform(get(url).param("generalId", String.valueOf(saveGeneralMember.getId())))
            .andExpect(status().isOk()).andExpect(content().json(responseContent))
            .andDo(log());
    }

    //validation남음.

}