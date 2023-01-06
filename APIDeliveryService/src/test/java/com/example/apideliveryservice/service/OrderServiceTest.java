package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.dto.GeneralMemberOrderDto;
import com.example.apideliveryservice.dto.RequestOrder;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import com.example.apideliveryservice.entity.GeneralMemberEntity;
import com.example.apideliveryservice.entity.OrderDetailEntity;
import com.example.apideliveryservice.entity.OrderEntity;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import com.example.apideliveryservice.repository.GeneralMemberRepository;
import com.example.apideliveryservice.repository.OrderDetailRepository;
import com.example.apideliveryservice.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    CompanyFoodRepository companyFoodRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    GeneralMemberRepository generalMemberRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    CompanyMemberRepository companyMemberRepository;
    @Test
    @DisplayName("음식 주문 성공 테스트")
    void addOrder() {
        //given
        GeneralMemberEntity saveGeneralMember = generalMemberRepository.save(
            new GeneralMemberEntity("generalMember", "password", "generalName", false));
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("companyMember", "password", "companyName", false));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName1", new BigDecimal("3000")));
        List<RequestOrder> requestList = Arrays.asList(
            new RequestOrder(saveGeneralMember.getId(), saveCompanyMember.getId(),
                saveCompanyFood.getId(), 3),
            new RequestOrder(saveGeneralMember.getId(), saveCompanyMember.getId(),
                saveCompanyFood.getId(), 5));

        //when
        orderService.addOrder(saveGeneralMember.getId(), requestList);
        //then
        em.flush();
        em.clear();
        List<OrderEntity> orderEntityList = orderRepository.findAll();
        List<OrderDetailEntity> orderDetailEntityList = orderDetailRepository.findAll();
        assertThat(orderEntityList.size()).isEqualTo(1);
        assertThat(orderDetailEntityList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("general member id별 주문 목록 list 찾기 Test")
    void findOrderListByGeneralId() {
        //given
        GeneralMemberEntity saveGeneralMember1 = generalMemberRepository.save(
            new GeneralMemberEntity("general1", "password", "general1", false));
        GeneralMemberEntity saveGeneralMember2 = generalMemberRepository.save(
            new GeneralMemberEntity("general2", "password", "general1", false));
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("companyMember", "password", "companyName", false));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName1", new BigDecimal("3000")));
        List<RequestOrder> requestList1 = Arrays.asList(
            new RequestOrder(saveGeneralMember1.getId(), saveCompanyMember.getId(),
                saveCompanyFood.getId(), 3),
            new RequestOrder(saveGeneralMember1.getId(), saveCompanyMember.getId(),
                saveCompanyFood.getId(), 5));
        List<RequestOrder> requestList2 = Arrays.asList(
            new RequestOrder(saveGeneralMember2.getId(), saveCompanyMember.getId(),
                saveCompanyFood.getId(), 3));

        orderService.addOrder(saveGeneralMember1.getId(), requestList1);
        orderService.addOrder(saveGeneralMember2.getId(), requestList2);
        //when
        em.flush();
        em.clear();
        List<GeneralMemberOrderDto> orderListByGeneralId1 = orderService.findOrderListByGeneralId(
            saveGeneralMember1.getId());
        List<GeneralMemberOrderDto> orderListByGeneralId2 = orderService.findOrderListByGeneralId(
                    saveGeneralMember2.getId());
        //then
        assertThat(orderListByGeneralId1.size()).isEqualTo(2);
        assertThat(orderListByGeneralId2.size()).isEqualTo(1);
    }
}