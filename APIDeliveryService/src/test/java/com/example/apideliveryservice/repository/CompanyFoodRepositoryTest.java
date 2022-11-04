package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.CompanyFoodDto;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Slf4j
@ActiveProfiles("db-h2")
class CompanyFoodRepositoryTest {

    @Autowired
    CompanyFoodRepository repository;
    @Autowired
    RepositoryResetHelper repositoryResetHelper;

    Connection connection;

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = repository.connectJdbc();
        repositoryResetHelper.ifExistDeleteCompanyFood(connection);
        repositoryResetHelper.createCompanyFoodTable(connection);
    }

    @Test
    void connectJdbc() {
        //then
        assertThat(connection).isNotNull();
    }

    @Test
    @DisplayName("음식등록하고 찾는 테스트")
    void addAndFind() throws SQLException {
        //given
        CompanyFoodDto companyFoodDto = new CompanyFoodDto(new BigInteger("1")
            , new BigInteger("1"), "name", new BigDecimal("3000"));
        //when
        repository.add(connection, companyFoodDto);
        Optional<CompanyFoodDto> findFoodDto = repository.findByNameAndMemberId(connection
            , new BigInteger("1"), "name");
        CompanyFoodDto expectedFoodDto = findFoodDto.get();
        //then
        assertThat(companyFoodDto).isEqualTo(expectedFoodDto);
    }

    @Test
    @DisplayName("일치하는 이름이 없을 때 Test")
    void findByIdAndName() throws SQLException {
        //given
        //when
        Optional<CompanyFoodDto> findFoodDto = repository.findByNameAndMemberId(connection
            , new BigInteger("1"), "name");
        CompanyFoodDto expectedFoodDto = findFoodDto.orElse(null);
        //then
        assertThat(expectedFoodDto).isNull();
    }

    @Test
    @DisplayName("memberId 일치하는 모든 음식 찾기 Test")
    void findAllFood() throws Exception {
        //given
        CompanyFoodDto companyFoodDto1 = new CompanyFoodDto(new BigInteger("1")
            , new BigInteger("1"), "name1", new BigDecimal("3000"));
        CompanyFoodDto companyFoodDto2 = new CompanyFoodDto(new BigInteger("2")
            , new BigInteger("1"), "name2", new BigDecimal("5000"));
        repository.add(connection, companyFoodDto1);
        repository.add(connection, companyFoodDto2);
        List<CompanyFoodDto> resultList = new ArrayList<>();
        resultList.add(companyFoodDto1);
        resultList.add(companyFoodDto2);
        //when
        List<CompanyFoodDto> allFood1 = repository.findAllFood(connection,
            new BigInteger("1")).orElse(null);
        List<CompanyFoodDto> allFood2 = repository.findAllFood(connection,
            new BigInteger("2")).orElse(null);
        //then
        assertThat(allFood1).isEqualTo(resultList);
        assertThat(allFood2).isEqualTo(new ArrayList<CompanyFoodDto>());
    }

    @Test
    @DisplayName("foodId로 음식 정보 찾기 Test")
    void findById() throws SQLException {
        //given
        CompanyFoodDto saveFood = new CompanyFoodDto(new BigInteger("1"), new BigInteger("1"),
            "name", new BigDecimal("3000"));
        repository.add(connection, saveFood);
        //when
        CompanyFoodDto findFood1 = repository.findById(connection, new BigInteger("1"))
            .orElse(null);
        CompanyFoodDto findFood2 = repository.findById(connection, new BigInteger("2"))
            .orElse(null);
        //then
        assertThat(findFood1).isEqualTo(saveFood);
        assertThat(findFood2).isNull();
    }

    @Test
    @DisplayName("가격 변경 Test")
    void updatePrice() throws SQLException {
        //given
        CompanyFoodDto saveFood = new CompanyFoodDto(new BigInteger("1"), new BigInteger("11"),
            "name", new BigDecimal("3000"));
        repository.add(connection, saveFood);
        //when
        repository.updatePrice(connection, new BigInteger("1"), new BigDecimal("5000"));
        CompanyFoodDto findFood = repository.findById(connection, new BigInteger("1"))
            .orElse(null);
        //then
        assertThat(findFood.getPrice()).isEqualTo(new BigDecimal("5000"));
    }
}