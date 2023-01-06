package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.entity.GeneralMemberEntity;
import com.example.apideliveryservice.entity.OrderDetailEntity;
import com.example.apideliveryservice.entity.OrderEntity;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Slf4j
class OrderRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    GeneralMemberRepository generalMemberRepository;

    @Autowired
    CompanyMemberRepository companyMemberRepository;
    @Autowired
    CompanyFoodRepository companyFoodRepository;
    @Autowired
    CompanyFoodHistoryRepository companyFoodHistoryRepository;

    @Test
    void findByGeneralMemberId() {
        //given
        GeneralMemberEntity saveGeneralMember = generalMemberRepository.save(
            new GeneralMemberEntity("loginName", "password", "name", false));
        OrderEntity saveOrder = orderRepository.save(new OrderEntity(saveGeneralMember));

        OrderDetailEntity saveOrderDetail = orderDetailRepository.save(
            new OrderDetailEntity(saveOrder, null, null, new BigDecimal("3000"), 3));
        orderDetailRepository.save(
            new OrderDetailEntity(saveOrder, null, null, new BigDecimal("5000"), 2));
        orderDetailRepository.save(
            new OrderDetailEntity(saveOrder, null, null, new BigDecimal("2000"), 2));
        //when
        em.flush();
        em.clear();
        OrderEntity findOrder = orderRepository.findAllByGeneralMemberEntity(saveGeneralMember)
            .get(0);
        //then
        assertThat(findOrder.getOrderDetailEntityList().size()).isEqualTo(3);
    }
}