package com.example.apideliveryservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.RequestOrder;
import com.example.apideliveryservice.dto.ResponseOrderSuccess;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import com.example.apideliveryservice.service.CompanyMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
class OrderControllerTest {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    @Autowired
    OrderController orderController;
    @Autowired
    CompanyFoodRepository companyFoodRepository;
    @Autowired
    RepositoryResetHelper resetHelper;
    @Autowired
    CompanyMemberService service;
    Connection connection;
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    String baseUrl;
    EntityManagerFactory emf;
    EntityManager em;
    EntityTransaction tx;


    @BeforeEach
    void beforeEach() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL", "sa", "");
        resetHelper.ifExistDeleteOrderList(connection);
        resetHelper.createOrderList(connection);
        resetHelper.ifExistDeleteOrderDetailList(connection);
        resetHelper.createOrderDetailList(connection);
        resetHelper.ifExistDeleteCompanyFood(connection);
        resetHelper.createCompanyFoodTable(connection);
        resetHelper.ifExistDeleteCompanyFoodPrice(connection);
        resetHelper.createCompanyFoodPriceTable(connection);

        baseUrl = "/api/delivery-service/order";
        objectMapper = new ObjectMapper();

        //validation 미적용
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        emf = Persistence.createEntityManagerFactory(persistenceName);
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }

    @Test
    @DisplayName("음식 구매 성공 Test")
    void orderFood() throws Exception {
        //given
        tx.begin();
        CompanyFoodEntity companyFoodEntity = new CompanyFoodEntity(null, 2l, "김밥",
            new Timestamp(System.currentTimeMillis()), null);
        companyFoodRepository.add(em, companyFoodEntity, new BigDecimal("3000"));
        tx.commit();

        String url = baseUrl + "/member/order";
        List<RequestOrder> requestList = new ArrayList<>();
        RequestOrder order1 = new RequestOrder(1l, 2l, 1l, 4);
        RequestOrder order2 = new RequestOrder(5l, 2l, 1l, 1);
        requestList.add(order1);
        requestList.add(order2);

        String requestJson = objectMapper.writeValueAsString(requestList);
        ResponseOrderSuccess success = new ResponseOrderSuccess(201, null, null);
        String responseContent = objectMapper.writeValueAsString(success);
        //when
        //then
        mockMvc.perform(post(url).contentType("application/json").content(requestJson))
            .andExpect(status().isCreated()).andExpect(content().json(responseContent))
            .andDo(log());
    }
}