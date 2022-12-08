package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.dto.RequestCompanyFood;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.exception.NonExistentFoodIdException;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
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
class CompanyFoodServiceTest {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    @Autowired
    CompanyFoodService service;
    @Autowired
    CompanyFoodRepository repository;
    @Autowired
    RepositoryResetHelper resetHelper;
    Connection connection;
    EntityManagerFactory emf;
    EntityManager em;
    EntityTransaction tx;

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL", "sa", "");
        resetHelper.ifExistDeleteCompanyFood(connection);
        resetHelper.createCompanyFoodTable(connection);
        resetHelper.ifExistDeleteCompanyFoodPrice(connection);
        resetHelper.createCompanyFoodPriceTable(connection);

        emf = Persistence.createEntityManagerFactory(persistenceName);
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }

    @Test
    @DisplayName("정상 음식 등록 Test")
    void addFood1() throws Exception {
        //given
        RequestCompanyFood requestCompanyFood = new RequestCompanyFood("11", "foodName",
            new BigDecimal("3000"));
        CompanyFoodEntity food = new CompanyFoodEntity(1l, 11l, "foodName",
            new Timestamp(System.currentTimeMillis()), null);

        //when
        service.addFood(requestCompanyFood.getMemberId(), requestCompanyFood.getName(),
            requestCompanyFood.getPrice());
        CompanyFoodEntity findFood = repository.findByNameAndMemberId(em, 11l, "foodName")
            .orElse(null);
        //then
        assertThat(food).isEqualTo(findFood);
    }

    @Test
    @DisplayName("중복된 memberId 그리고 음식명 음식 등록 실패 Test")
    void addFood4() throws SQLException {
        //given
        tx.begin();
        CompanyFoodEntity companyFoodDto1 = new CompanyFoodEntity(null, 11l, "name",
            new Timestamp(System.currentTimeMillis()), null);
        repository.add(em, companyFoodDto1, new BigDecimal("3000"));
        tx.commit();
        //when
        RequestCompanyFood request = new RequestCompanyFood("11", "name", new BigDecimal("3000"));
        //then
        assertThatThrownBy(() -> service.addFood(request.getMemberId(), request.getName(),
            request.getPrice())).isInstanceOf(DuplicatedFoodNameException.class);
    }

    @Test
    @DisplayName("company food 찾기 test")
    void findMember() throws Exception {
        //given
        tx.begin();
        CompanyFoodEntity saveFood = new CompanyFoodEntity(null, 11l, "name",
            new Timestamp(System.currentTimeMillis()), null);
        repository.add(em, saveFood, new BigDecimal("3000"));
        tx.commit();
        CompanyFoodDto actual = new CompanyFoodDto(1l, 11l, "name", saveFood.getRegistrationDate(),
            new BigDecimal("3000"));
        //when
        CompanyFoodDto findFood = service.findFood("1");
        //then
        assertThat(findFood).isEqualTo(actual);
        assertThatThrownBy(() -> service.findFood("2")).isInstanceOf(
            NonExistentFoodIdException.class);
    }

    @Test
    @DisplayName("모든 음식 찾기 test")
    void findAllFood() throws Exception {
        //given
        tx.begin();
        CompanyFoodEntity saveFood1 = new CompanyFoodEntity(null, 11l, "name1",
            new Timestamp(System.currentTimeMillis()), null);
        CompanyFoodEntity saveFood2 = new CompanyFoodEntity(null, 11l, "name2",
            new Timestamp(System.currentTimeMillis()), null);
        repository.add(em, saveFood1, new BigDecimal("3000"));
        repository.add(em, saveFood2, new BigDecimal("5000"));
        tx.commit();
        List<CompanyFoodDto> actualList = new ArrayList<>();
        CompanyFoodDto actual1 = new CompanyFoodDto(1l, 11l, "name1", saveFood1.getRegistrationDate(),
            new BigDecimal("3000"));
        CompanyFoodDto actual2 = new CompanyFoodDto(2l, 11l, "name2", saveFood2.getRegistrationDate(),
            new BigDecimal("5000"));
        actualList.add(actual1);
        actualList.add(actual2);

        //when
        List<CompanyFoodDto> findAllFoodList = service.findAllFood("11");
        List<CompanyFoodDto> findAllFoodList2 = service.findAllFood("12");
        //then
        assertThat(findAllFoodList).isEqualTo(actualList);
        assertThat(findAllFoodList2).isEqualTo(new ArrayList<>());
    }



    @Test
    @DisplayName("food price 변경 Test")
    void updatePrice() throws Exception {
        //given
        tx.begin();
        CompanyFoodEntity saveFood = new CompanyFoodEntity(null, 11l, "foodName",
            new Timestamp(System.currentTimeMillis()), null);
        repository.add(em, saveFood, new BigDecimal("3000"));
        tx.commit();
        //when
        service.updatePrice("1", "5000");
        CompanyFoodEntity findFood = repository.findById(em, 1l).orElse(null);
        //then
        assertThat(findFood.getTempPrice()).isEqualTo("5000");
    }
}