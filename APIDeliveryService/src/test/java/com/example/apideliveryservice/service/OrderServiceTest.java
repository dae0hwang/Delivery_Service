//package com.example.apideliveryservice.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.example.apideliveryservice.RepositoryResetHelper;
//import com.example.apideliveryservice.dto.GeneralMemberOrderDto;
//import com.example.apideliveryservice.entity.CompanyFoodEntity;
//import com.example.apideliveryservice.entity.OrderDetailEntity;
//import com.example.apideliveryservice.entity.OrderEntity;
//import com.example.apideliveryservice.repository.CompanyFoodRepository;
//import com.example.apideliveryservice.repository.OrderRepository;
//import java.math.BigDecimal;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Persistence;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//@SpringBootTest
//@ActiveProfiles("jpa-h2")
//class OrderServiceTest {
//
//    @Value("${persistenceName:@null}")
//    private String persistenceName;
//    @Autowired
//    CompanyFoodRepository companyFoodRepository;
//    @Autowired
//    OrderRepository orderRepository;
//    @Autowired
//    OrderService orderService;
//    @Autowired
//    RepositoryResetHelper resetHelper;
//    Connection connection;
//    EntityManagerFactory emf;
//    EntityManager em;
//    EntityTransaction tx;
//
//    @BeforeEach
//    void beforeEach() throws SQLException {
//        connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL", "sa", "");
//        resetHelper.ifExistDeleteOrderList(connection);
//        resetHelper.createOrderList(connection);
//        resetHelper.ifExistDeleteOrderDetailList(connection);
//        resetHelper.createOrderDetailList(connection);
//        resetHelper.ifExistDeleteCompanyFood(connection);
//        resetHelper.createCompanyFoodTable(connection);
//        resetHelper.ifExistDeleteCompanyFoodPrice(connection);
//        resetHelper.createCompanyFoodPriceTable(connection);
//
//        emf = Persistence.createEntityManagerFactory(persistenceName);
//        em = emf.createEntityManager();
//        tx = em.getTransaction();
//    }
//
//    @Test
//    void addOrder() throws Exception {
//        //given
//        tx.begin();
//        CompanyFoodEntity companyFoodEntity = new CompanyFoodEntity(null, 1l, "김밥",
//            new Timestamp(System.currentTimeMillis()), null);
//        companyFoodRepository.add(em, companyFoodEntity, new BigDecimal("3000"));
//        tx.commit();
//        //when
//        List<OrderDetailEntity> list = new ArrayList<>();
//        OrderDetailEntity orderDetailElement1 = new OrderDetailEntity(null, null, 11l, 1l, null, 3);
//        OrderDetailEntity orderDetailElement2 = new OrderDetailEntity(null, null, 11l, 1l, null, 6);
//        list.add(orderDetailElement1);
//        list.add(orderDetailElement2);
//
//        tx.begin();
//        orderRepository.addOrder(em, 22l, list);
//        OrderDetailEntity findOrderDetailEntity1 = em.find(OrderDetailEntity.class, 1l);
//        OrderDetailEntity findOrderDetailEntity2 = em.find(OrderDetailEntity.class, 2l);
//        tx.commit();
//
//        //then
//        assertThat(findOrderDetailEntity1.getFoodAmount()).isEqualTo(3);
//        assertThat(findOrderDetailEntity2.getFoodAmount()).isEqualTo(6);
//    }
//
//    @Test
//    @DisplayName("general member id별 주문 목록 list 찾기 Test")
//    void findOrderListByGeneralId() {
//        //given
//        tx.begin();
//        CompanyFoodEntity companyFoodEntity1 = new CompanyFoodEntity(null, 11l, "참치김밥",
//            new Timestamp(System.currentTimeMillis()), null);
//        companyFoodRepository.add(em, companyFoodEntity1, new BigDecimal("3000"));
//        CompanyFoodEntity companyFoodEntity2 = new CompanyFoodEntity(null, 11l, "고추김밥",
//            new Timestamp(System.currentTimeMillis()), null);
//        companyFoodRepository.add(em, companyFoodEntity2, new BigDecimal("4000"));
//        List<OrderDetailEntity> list = new ArrayList<>();
//        OrderDetailEntity orderDetailElement1 = new OrderDetailEntity(null, null, 11l, 1l, null, 3);
//        OrderDetailEntity orderDetailElement2 = new OrderDetailEntity(null, null, 11l, 2l, null, 6);
//        list.add(orderDetailElement1);
//        list.add(orderDetailElement2);
//        orderRepository.addOrder(em, 22l, list);
//        OrderEntity findOrderEntity = em.find(OrderEntity.class, 1l);
//        Timestamp registrationDate = findOrderEntity.getRegistrationDate();
//        tx.commit();
//
//        //when
//        List<GeneralMemberOrderDto> findOrderListByGeneralId = orderService.findOrderListByGeneralId(
//            22l);
//        List<GeneralMemberOrderDto> findOrderBlankList = orderService.findOrderListByGeneralId(44l);
//
//        List<GeneralMemberOrderDto> actualList = new ArrayList<>();
//        GeneralMemberOrderDto generalMemberOrderDto1 = new GeneralMemberOrderDto(registrationDate,
//            1l, 22l, 1l, "참치김밥", new BigDecimal("3000"), 3, 11l);
//        GeneralMemberOrderDto generalMemberOrderDto2 = new GeneralMemberOrderDto(registrationDate,
//            1l, 22l, 2l, "고추김밥", new BigDecimal("4000"), 6, 11l);
//        actualList.add(generalMemberOrderDto1);
//        actualList.add(generalMemberOrderDto2);
//
//        //then
//        assertThat(findOrderListByGeneralId).isEqualTo(actualList);
//        assertThat(findOrderBlankList).isEqualTo(new ArrayList<>());
//    }
//}