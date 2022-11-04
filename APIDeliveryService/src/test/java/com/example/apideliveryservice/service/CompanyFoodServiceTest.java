package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.dto.RequestCompanyFoodDto;
import com.example.apideliveryservice.exception.BlackException;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.exception.NonExistentFoodIdException;
import com.example.apideliveryservice.exception.NotDigitException;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Slf4j
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
        RequestCompanyFoodDto requestCompanyFood = new RequestCompanyFoodDto("11", "foodName",
            "3000");
        CompanyFoodDto food = new CompanyFoodDto(new BigInteger("1"), new BigInteger("11"),
            "foodName", new BigDecimal("3000"));

        //when
        service.addFood(requestCompanyFood);
        CompanyFoodDto findFood = repository.findByNameAndMemberId(connection,
            new BigInteger("11"), "foodName").orElse(null);
        //then
        assertThat(food).isEqualTo(findFood);
    }

    @Test
    @DisplayName("비어진 Request input으로 음식 등록 실패 테스트")
    void addFood2() throws SQLException {
        //given
        RequestCompanyFoodDto request1 = new RequestCompanyFoodDto("", "1", "3000");
        RequestCompanyFoodDto request2 = new RequestCompanyFoodDto("1", "", "3000");
        RequestCompanyFoodDto request3 = new RequestCompanyFoodDto("1", "1", "");
        //when
        //then
        assertThatThrownBy(() ->
            //when
            service.addFood(request1))
            .isInstanceOf(BlackException.class);
        assertThatThrownBy(() ->
            //when
            service.addFood(request2))
            .isInstanceOf(BlackException.class);
        assertThatThrownBy(() ->
            //when
            service.addFood(request3))
            .isInstanceOf(BlackException.class);
    }

    @Test
    @DisplayName("숫자가 아닌 memberId price 으로 음식 등록 실패 테스트")
    void addFood3() throws SQLException {
        //given
        RequestCompanyFoodDto request1 = new RequestCompanyFoodDto("notDigit", "name", "3000");
        RequestCompanyFoodDto request2 = new RequestCompanyFoodDto("1", "name", "notDigit");
        RequestCompanyFoodDto request3 = new RequestCompanyFoodDto("notDigit", "1", "notDigit");
        //when
        //then
        assertThatThrownBy(() ->
            //when
            service.addFood(request1))
            .isInstanceOf(NotDigitException.class);
        assertThatThrownBy(() ->
            //when
            service.addFood(request2))
            .isInstanceOf(NotDigitException.class);
        assertThatThrownBy(() ->
            //when
            service.addFood(request3))
            .isInstanceOf(NotDigitException.class);
    }

    @Test
    @DisplayName("중복된 memberId 그리고 음식명 음식 등록 실패 Test")
    void addFood4() throws SQLException {
        //given
        CompanyFoodDto companyFoodDto1 = new CompanyFoodDto(new BigInteger("1")
            , new BigInteger("11"), "name", new BigDecimal("3000"));
        repository.add(connection, companyFoodDto1);
        //when
        RequestCompanyFoodDto request = new RequestCompanyFoodDto("11", "name", "3000");
        //then
        assertThatThrownBy(() ->
            service.addFood(request))
            .isInstanceOf(DuplicatedFoodNameException.class);
    }

    @Test
    @DisplayName("company food 찾기 test")
    void findMember() throws SQLException {
        //given
        CompanyFoodDto saveFood = new CompanyFoodDto(new BigInteger("1"), new BigInteger("11"),
            "foodName", new BigDecimal("3000"));
        repository.add(connection, saveFood);
        //when
        CompanyFoodDto findFood = service.findFood("1");
        //then
        assertThat(findFood).isEqualTo(saveFood);
        assertThatThrownBy(() ->
            service.findFood("2"))
            .isInstanceOf(NonExistentFoodIdException.class);
    }

    @Test
    @DisplayName("food price 변경 Test")
    void updatePrice() throws SQLException {
        //given
        CompanyFoodDto saveFood = new CompanyFoodDto(new BigInteger("1"), new BigInteger("11"),
            "foodName", new BigDecimal("3000"));
        repository.add(connection, saveFood);
        //when
        service.updatePrice("1", "5000");
        CompanyFoodDto findFood = repository.findById(connection, new BigInteger("1")).orElse(null);
        //then
        assertThat(findFood.getPrice()).isEqualTo("5000");
        assertThatThrownBy(() ->
            service.updatePrice("1", ""))
            .isInstanceOf(BlackException.class);
        assertThatThrownBy(() ->
            service.updatePrice("1", "a11a"))
            .isInstanceOf(NotDigitException.class);
    }
}