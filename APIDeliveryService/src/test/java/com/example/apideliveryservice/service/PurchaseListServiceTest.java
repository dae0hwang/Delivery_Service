package com.example.apideliveryservice.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.apideliveryservice.PurchaseListTestHelper;
import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.PurchaseListDto;
import com.example.apideliveryservice.repository.PurchaseListRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("db-h2")
class PurchaseListServiceTest {

    @Autowired
    PurchaseListService service;
    @Autowired
    PurchaseListRepository repository;
    @Autowired
    RepositoryResetHelper resetHelper;
    @Autowired
    PurchaseListTestHelper testHelper;
    Connection connection;

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = repository.connectJdbc();
        resetHelper.ifExistDeletePurchaseList(connection);
        resetHelper.createPurchaseListTable(connection);
    }

    @Test
    @DisplayName("정상 등록 Test")
    void addList() throws SQLException {
        //given
        PurchaseListDto purchaseListDto = new PurchaseListDto(1l, 3l, 5l, 7l,
            new BigDecimal("3000"), new Timestamp(System.currentTimeMillis()));
        //when
        service.addList("3", "5", "7", "3000");

        PurchaseListDto findResult = testHelper.findById(connection, 1l).orElse(null);
        //then
        assertThat(findResult).isEqualTo(purchaseListDto);
    }
}