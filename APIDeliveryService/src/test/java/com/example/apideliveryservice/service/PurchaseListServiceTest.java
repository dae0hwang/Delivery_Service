package com.example.apideliveryservice.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.apideliveryservice.PurchaseListTestHelper;
import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.entity.PurchaseListEntity;
import com.example.apideliveryservice.repository.PurchaseListRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("jpa-h2")
class PurchaseListServiceTest {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    @Autowired
    PurchaseListService service;
    @Autowired
    PurchaseListRepository repository;
    @Autowired
    RepositoryResetHelper resetHelper;
    @Autowired
    PurchaseListTestHelper testHelper;
    Connection connection;
    EntityManagerFactory emf;
    EntityManager em;
    EntityTransaction tx;

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL", "sa", "");
        resetHelper.ifExistDeletePurchaseList(connection);
        resetHelper.createPurchaseListTable(connection);

        emf = Persistence.createEntityManagerFactory(persistenceName);
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }

    @Test
    @DisplayName("정상 등록 Test")
    void addList() throws Exception {
        //given
        PurchaseListEntity purchaseListDto = new PurchaseListEntity(1l, 3l, 5l, 7l,
            new BigDecimal("3000"), new Timestamp(System.currentTimeMillis()));
        //when
        service.addList("3", "5", "7", "3000");

        PurchaseListEntity findResult = testHelper.findById(connection, 1l).orElse(null);
        //then
        assertThat(findResult).isEqualTo(purchaseListDto);
    }
}