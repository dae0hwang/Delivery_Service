package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.GeneralMemberOrderDto;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import com.example.apideliveryservice.entity.GeneralMemberEntity;
import com.example.apideliveryservice.entity.OrderDetailEntity;
import com.example.apideliveryservice.entity.OrderEntity;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("jpa-test")
@Slf4j
class OrderRepositoryTest {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CompanyFoodRepository companyFoodRepository;
    @Autowired
    CompanyMemberRepository companyMemberRepository;
    @Autowired
    RepositoryResetHelper resetHelper;
    Connection connection;
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
        resetHelper.ifExistDeleteCompanyMembers(connection);
        resetHelper.createCompanyMembersTable(connection);
        resetHelper.ifExistDeleteGeneralMembers(connection);
        resetHelper.createGeneralMembersTable(connection);

        emf = Persistence.createEntityManagerFactory(persistenceName);
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    void afterEach() {
        tx.rollback();
    }

    @Test
    void addOrder() throws Exception {
        //given
        CompanyMemberEntity saveCompanyMember = new CompanyMemberEntity(null, "loginName",
            "password", "name", false, new Timestamp(System.currentTimeMillis()));
        em.persist(saveCompanyMember);
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findById(em, 1l).get();

        CompanyFoodEntity companyFoodEntity = new CompanyFoodEntity(null, findCompanyMember, "김밥",
            new Timestamp(System.currentTimeMillis()), null);
        companyFoodRepository.add(em, companyFoodEntity, new BigDecimal("3000"));
        CompanyFoodEntity findCompanyFood = companyFoodRepository.findById(em, 1l).get();

        GeneralMemberEntity saveGeneralMember = new GeneralMemberEntity(null, "loginName",
            "password", "name", false, new Timestamp(System.currentTimeMillis()));
        em.persist(saveGeneralMember);

        //when
        List<OrderDetailEntity> list = new ArrayList<>();
        OrderDetailEntity orderDetailElement1 = new OrderDetailEntity(null, null, findCompanyMember, findCompanyFood, null, 3);
        OrderDetailEntity orderDetailElement2 = new OrderDetailEntity(null, null, findCompanyMember, findCompanyFood, null, 6);
        list.add(orderDetailElement1);
        list.add(orderDetailElement2);

        orderRepository.addOrder(em, 1l, list);
        OrderDetailEntity findOrderDetailEntity1 = em.find(OrderDetailEntity.class, 1l);
        OrderDetailEntity findOrderDetailEntity2 = em.find(OrderDetailEntity.class, 2l);

        //then
        assertThat(findOrderDetailEntity1.getFoodAmount()).isEqualTo(3);
        assertThat(findOrderDetailEntity2.getFoodAmount()).isEqualTo(6);
    }

//    @Test
//    @DisplayName("general member id별 주문 목록 list 찾기 Test")
//    void findOrderListByGeneralId() throws Exception {
//        //given
//        CompanyMemberEntity saveCompanyMember = new CompanyMemberEntity(null, "loginName",
//            "password", "name", false, new Timestamp(System.currentTimeMillis()));
//        em.persist(saveCompanyMember);
//        CompanyMemberEntity findCompanyMember = em.find(CompanyMemberEntity.class, 1l);
//
//        GeneralMemberEntity saveGeneralMember = new GeneralMemberEntity(null, "loginName",
//            "password", "name", false, new Timestamp(System.currentTimeMillis()));
//        em.persist(saveGeneralMember);
//
//        CompanyFoodEntity companyFoodEntity1 = new CompanyFoodEntity(null, findCompanyMember, "참치김밥",
//            new Timestamp(System.currentTimeMillis()), null);
//        companyFoodRepository.add(em, companyFoodEntity1, new BigDecimal("3000"));
//        CompanyFoodEntity companyFoodEntity2 = new CompanyFoodEntity(null, findCompanyMember, "고추김밥",
//            new Timestamp(System.currentTimeMillis()), null);
//        companyFoodRepository.add(em, companyFoodEntity2, new BigDecimal("4000"));
//        CompanyFoodEntity findCompanyFood1 = companyFoodRepository.findById(em, 1l).get();
//        CompanyFoodEntity findCompanyFood2 = companyFoodRepository.findById(em, 2l).get();
//        CompanyFoodEntity a = new CompanyFoodEntity()
//
//
//        List<OrderDetailEntity> list = new ArrayList<>();
//        OrderDetailEntity orderDetailElement1 = new OrderDetailEntity(null, null, findCompanyMember, findCompanyFood1, null, 3);
//        OrderDetailEntity orderDetailElement2 = new OrderDetailEntity(null, null, findCompanyMember, findCompanyFood2, null, 6);
//        list.add(orderDetailElement1);
//        list.add(orderDetailElement2);
//
//        orderRepository.addOrder(em, 1l, list);
//        OrderEntity findOrderEntity = em.find(OrderEntity.class, 1l);
//        Timestamp registrationDate = findOrderEntity.getRegistrationDate();
//        //when
//        List<GeneralMemberOrderDto> findOrderListByGeneralId =
//            orderRepository.findOrderListByGeneralId(
//            em, 1l);
//        List<GeneralMemberOrderDto> findOrderBlankList = orderRepository.findOrderListByGeneralId(
//            em, 22l);
//
//
//        List<GeneralMemberOrderDto> expectedList = new ArrayList<>();
//        GeneralMemberOrderDto generalMemberOrderDto1 = new GeneralMemberOrderDto(registrationDate,
//            1l, 1l, 1l, "참치김밥", new BigDecimal("3000"), 3, 1l);
//        GeneralMemberOrderDto generalMemberOrderDto2 = new GeneralMemberOrderDto(registrationDate,
//            1l, 1l, 2l, "고추김밥", new BigDecimal("4000"), 6, 1l);
//        expectedList.add(generalMemberOrderDto1);
//        expectedList.add(generalMemberOrderDto2);
//
//        //then
//        assertThat(findOrderListByGeneralId).isEqualTo(expectedList);
//        assertThat(findOrderBlankList).isEqualTo(new ArrayList<>());
//    }
}