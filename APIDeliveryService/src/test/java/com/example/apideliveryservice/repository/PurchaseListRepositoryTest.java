package com.example.apideliveryservice.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.apideliveryservice.PurchaseListTestHelper;
import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.PurchaseListDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

    @Test
    @Disabled
    @DisplayName("companyMember 그리고 해당날짜")
    void findByCompanyMemberIdAndThisMonth() throws SQLException {
        //given
        Timestamp time1 = Timestamp.valueOf(LocalDateTime.now().withDayOfMonth(1));
        Timestamp time2 = Timestamp.valueOf(LocalDateTime.now().withDayOfMonth(1));
        Timestamp time3 = Timestamp.valueOf(LocalDateTime.now().withMonth(2));
        PurchaseListDto purchase1 = new PurchaseListDto(1l, 2l, 33l, 3l, new BigDecimal("3000"),
            time1);
//        PurchaseListDto purchase2 = new PurchaseListDto(2l, 2l, 33l, 3l, new BigDecimal("3000"),
//            time2);
        PurchaseListDto purchase3 = new PurchaseListDto(3l, 2l, 33l, 3l, new BigDecimal("3000"),
            time3);
        repository.create(connection, purchase1);
//        repository.create(connection, purchase2);
//        repository.create(connection, purchase3);

        List<PurchaseListDto> expected = new ArrayList<>();
        expected.add(purchase1);

        PurchaseListDto purchaseListDto = testHelper.findById(connection, 1l).orElse(null);
//        expected.add(purchase2);
        //when
        Date.valueOf("2022-11-01");
        List<PurchaseListDto> byCompanyMemberIdAndThisMonth = repository.findByCompanyMemberIdAndThisMonth(
            connection, 33l);

        //then
//        assertThat(byCompanyMemberIdAndThisMonth).isEqualTo(expected.toString());
        assertThat(purchaseListDto).isEqualTo(purchase1);
    }
}