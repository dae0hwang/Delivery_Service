package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.CompanyFoodDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Slf4j
@ActiveProfiles("jpa-h2")
class CompanyFoodRepositoryTest {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    @Autowired
    CompanyFoodRepository repository;
    @Autowired
    RepositoryResetHelper repositoryResetHelper;
    Connection connection;
    EntityManagerFactory emf;
    EntityManager em;
    EntityTransaction tx;

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL", "sa", "");
        repositoryResetHelper.ifExistDeleteCompanyFood(connection);
        repositoryResetHelper.createCompanyFoodTable(connection);
        repositoryResetHelper.ifExistDeleteCompanyFoodPrice(connection);
        repositoryResetHelper.createCompanyFoodPriceTable(connection);

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
    @DisplayName("음식등록하고 찾는 테스트")
    void addAndFind() {
        //given
        CompanyFoodDto expectedFoodDto = new CompanyFoodDto(1l, 1l, "name",
            new Timestamp(System.currentTimeMillis()), new BigDecimal("5000"));

        CompanyFoodDto companyFoodDto = new CompanyFoodDto(null, 1l, "name",
            new Timestamp(System.currentTimeMillis()), null);
        BigDecimal price = new BigDecimal("5000");
        //when
        repository.add(em, companyFoodDto, price);
        CompanyFoodDto findCompanyFoodDto = repository.findById(em, 1l).orElse(null);
        //then
        assertThat(findCompanyFoodDto).isEqualTo(expectedFoodDto);
    }

    @Test
    @DisplayName("일치하는 이름이 없을 때 Test")
    void findByIdAndName() {
        //given
        //when
        Optional<CompanyFoodDto> findFoodDto = repository.findByNameAndMemberId(em, 1l,
            "name");
        CompanyFoodDto expectedFoodDto = findFoodDto.orElse(null);
        //then
        assertThat(expectedFoodDto).isNull();
    }

    @Test
    @DisplayName("memberId 일치하는 모든 음식 찾기 Test")
    void findAllFood() {
        //given
        CompanyFoodDto companyFood1 = new CompanyFoodDto(null, 1l, "name1",
            new Timestamp(System.currentTimeMillis()), null);
        CompanyFoodDto companyFood2 = new CompanyFoodDto(null, 1l, "name2",
            new Timestamp(System.currentTimeMillis()), null);
        repository.add(em, companyFood1, new BigDecimal("3000"));
        repository.add(em, companyFood2, new BigDecimal("5000"));

        CompanyFoodDto expect1 = new CompanyFoodDto(1l, 1l, "name1",
            companyFood1.getRegistrationDate(), new BigDecimal("3000"));
        CompanyFoodDto expect2 = new CompanyFoodDto(2l, 1l, "name2",
            companyFood2.getRegistrationDate(), new BigDecimal("5000"));
        List<CompanyFoodDto> resultList = new ArrayList<>();
        resultList.add(expect1);
        resultList.add(expect2);
        //when
        List<CompanyFoodDto> allFood1 = repository.findAllFood(em, 1l);
        List<CompanyFoodDto> allFood2 = repository.findAllFood(em, 2l);
        //then
        assertThat(allFood1).isEqualTo(resultList);
        assertThat(allFood2).isEqualTo(new ArrayList<CompanyFoodDto>());
    }

    @Test
    @DisplayName("foodId로 음식 정보 찾기 Test")
    void findById() {
        //given
        CompanyFoodDto saveFood = new CompanyFoodDto(null, 1l, "name", new Timestamp(System.currentTimeMillis()), null);
        repository.add(em, saveFood, new BigDecimal("3000"));
        //when
        CompanyFoodDto findFood1 = repository.findById(em, 1l).orElse(null);
        CompanyFoodDto findFood2 = repository.findById(em, 2l).orElse(null);
        //then
        assertThat(findFood1).isEqualTo(
            new CompanyFoodDto(1l, 1l, "name", saveFood.getRegistrationDate(),
                new BigDecimal("3000")));
        assertThat(findFood2).isNull();
    }

    @Test
    @DisplayName("가격 변경 Test")
    void updatePrice() {
        //given
        CompanyFoodDto saveFood = new CompanyFoodDto(null, 11l, "name", new Timestamp(System.currentTimeMillis()), null);
        repository.add(em, saveFood, new BigDecimal("3000"));
        //when
        repository.updatePrice(em, 1l, new BigDecimal("5000"));
        CompanyFoodDto findFood = repository.findById(em, 1l).orElse(null);
        //then
        assertThat(findFood.getTempPrice()).isEqualTo(new BigDecimal("5000"));
    }
}