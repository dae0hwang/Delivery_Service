package com.example.apideliveryservice.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.apideliveryservice.PurchaseListTestHelper;
import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.PurchaseListDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("db-h2")
@Slf4j
class PurchaseListRepositoryTest {

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
    @DisplayName("purchaseList 등록 Test")
    void create() throws SQLException {
        //given
        PurchaseListDto purchaseListDto = new PurchaseListDto(1l, 3l, 5l, 7l,
            new BigDecimal("3000"), new Timestamp(System.currentTimeMillis()));
        //when
        repository.create(connection, purchaseListDto);

        PurchaseListDto findResult = testHelper.findById(connection, 1l).orElse(null);
        //then
        assertThat(findResult).isEqualTo(purchaseListDto);
    }
}