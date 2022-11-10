package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.dto.RequestCompanyFoodDto;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.exception.NonExistentFoodIdException;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import java.math.BigDecimal;
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
        CompanyFoodDto food = new CompanyFoodDto(1l, 11l, "foodName", new BigDecimal("3000"));

        //when
        service.addFood(requestCompanyFood.getMemberId(), requestCompanyFood.getName(),
            requestCompanyFood.getPrice());
        CompanyFoodDto findFood = repository.findByNameAndMemberId(connection, 11l, "foodName")
            .orElse(null);
        //then
        assertThat(food).isEqualTo(findFood);
    }

    @Test
    @DisplayName("중복된 memberId 그리고 음식명 음식 등록 실패 Test")
    void addFood4() throws SQLException {
        //given
        CompanyFoodDto companyFoodDto1 = new CompanyFoodDto(1l, 11l, "name",
            new BigDecimal("3000"));
        repository.add(connection, companyFoodDto1);
        //when
        RequestCompanyFoodDto request = new RequestCompanyFoodDto("11", "name", "3000");
        //then
        assertThatThrownBy(() ->
            service.addFood(request.getMemberId(), request.getName(), request.getPrice()))
            .isInstanceOf(DuplicatedFoodNameException.class);

    }

    @Test
    @DisplayName("company food 찾기 test")
    void findMember() throws SQLException {
        //given
        CompanyFoodDto saveFood = new CompanyFoodDto(1l, 11l, "foodName", new BigDecimal("3000"));
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
        CompanyFoodDto saveFood = new CompanyFoodDto(1l, 11l, "foodName", new BigDecimal("3000"));
        repository.add(connection, saveFood);
        //when
        service.updatePrice("1", "5000");
        CompanyFoodDto findFood = repository.findById(connection, 1l).orElse(null);
        //then
        assertThat(findFood.getPrice()).isEqualTo("5000");
    }
}