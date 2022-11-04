package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.exception.BlackException;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("db-h2")
class CompanyFoodServiceTest {

    @Autowired
    CompanyFoodService service;
    @Autowired
    CompanyFoodRepository repository;
    @Autowired
    RepositoryResetHelper resetHelper;
    Connection connection;

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = repository.connectJdbc();
        resetHelper.ifExistDeleteCompanyFood(connection);
        resetHelper.createCompanyFoodTable(connection);
    }

    @Test
    @DisplayName("정상 음식 등록 Test")
    void addFood1() throws SQLException {
        //given
        CompanyFoodDto companyFoodDto = new CompanyFoodDto(new BigInteger("1")
            , new BigInteger("11"), "name", new BigDecimal("3000"));
        //when
        service.addFood(companyFoodDto);
        CompanyFoodDto findFood = repository.findByNameAndMemberId(connection,
            new BigInteger("11"), "name").orElse(null);
        //then
        assertThat(findFood).isEqualTo(companyFoodDto);
    }

    @Test
    @DisplayName("비어진 Request input으로 음식 등록 실패 테스트")
    void addFood2() throws SQLException {
        //given
        CompanyFoodDto companyFoodDto1 = new CompanyFoodDto(new BigInteger("1")
            , new BigInteger("11"), "", new BigDecimal("3000"));
        CompanyFoodDto companyFoodDto2 = new CompanyFoodDto(new BigInteger("2")
            , new BigInteger("12"), "name", null);
        CompanyFoodDto companyFoodDto3 = new CompanyFoodDto(new BigInteger("2")
            , new BigInteger("12"), "", null);

        //when
        //then
        assertThatThrownBy(() ->
            //when
            service.addFood(companyFoodDto1))
            .isInstanceOf(BlackException.class);
        assertThatThrownBy(() ->
            //when
            service.addFood(companyFoodDto2))
            .isInstanceOf(BlackException.class);
        assertThatThrownBy(() ->
            //when
            service.addFood(companyFoodDto3))
            .isInstanceOf(BlackException.class);
    }

    @Test
    @DisplayName("중복된 memberId 그리고 음식명 음식 등록 실패 Test")
    void addFood3() throws SQLException {
        //given
        CompanyFoodDto companyFoodDto1 = new CompanyFoodDto(new BigInteger("1")
            , new BigInteger("11"), "name", new BigDecimal("3000"));
        repository.add(connection, companyFoodDto1);
        //when
        CompanyFoodDto companyFoodDto2 = new CompanyFoodDto(new BigInteger("2")
            , new BigInteger("11"), "name", new BigDecimal("4000"));
        //then
        assertThatThrownBy(() ->
            service.addFood(companyFoodDto2))
            .isInstanceOf(DuplicatedFoodNameException.class);
    }
}