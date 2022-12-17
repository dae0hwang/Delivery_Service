package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
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
@ActiveProfiles("jpa-test")
class CompanyFoodRepositoryTest {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    @Autowired
    CompanyFoodRepository repository;
    @Autowired
    RepositoryResetHelper repositoryResetHelper;
    @Autowired
    CompanyMemberRepository companyMemberRepository;
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
        repositoryResetHelper.ifExistDeleteCompanyMembers(connection);
        repositoryResetHelper.createCompanyMembersTable(connection);

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
    void addAndFind() throws Exception {
        //given
        CompanyMemberEntity saveCompanyMember = new CompanyMemberEntity(null, "loginName",
            "password", "name", false, new Timestamp(System.currentTimeMillis()));
        em.persist(saveCompanyMember);
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findById(em, 1l).get();

        CompanyFoodEntity saveCompanyFood = new CompanyFoodEntity(null, findCompanyMember,
            "foodName", new Timestamp(System.currentTimeMillis()), null);

        CompanyFoodEntity expectedFoodDto = new CompanyFoodEntity(1l, findCompanyMember, "foodName",
            saveCompanyFood.getRegistrationDate(), new BigDecimal("3000"));

        //when
        repository.add(em, saveCompanyFood, new BigDecimal("3000"));
        CompanyFoodEntity actualFoodEntity = repository.findById(em, 1l).orElse(null);
        //then
        assertThat(actualFoodEntity).isEqualTo(expectedFoodDto);
    }

    @Test
    @DisplayName("일치하는 이름이 없을 때 Test")
    void findByIdAndName1() {
        //given
        //when
        Optional<CompanyFoodEntity> findFoodDto = repository.findByNameAndMemberId(em, 1l,
            "name");
        CompanyFoodEntity expectedFoodDto = findFoodDto.orElse(null);
        //then
        assertThat(expectedFoodDto).isNull();
    }

    @Test
    @DisplayName("일치하는 이름이 있을 때 Test")
    void findByIdAndName2() throws Exception {
        //given
        CompanyMemberEntity saveCompanyMember = new CompanyMemberEntity(null, "loginName",
            "password", "name", false, new Timestamp(System.currentTimeMillis()));
        em.persist(saveCompanyMember);
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findById(em, 1l).get();

        CompanyFoodEntity saveFoodEntity = new CompanyFoodEntity(null, findCompanyMember, "name",
            new Timestamp(System.currentTimeMillis()), null);
        repository.add(em, saveFoodEntity, new BigDecimal("3000"));

        CompanyFoodEntity expectedCompanyFood = new CompanyFoodEntity(1l, findCompanyMember, "name",
            saveFoodEntity.getRegistrationDate(), null);
        //when
        Optional<CompanyFoodEntity> findFoodDto = repository.findByNameAndMemberId(em, 1l,
            "name");
        CompanyFoodEntity actualCompanyFood = findFoodDto.orElse(null);

        //then
        assertThat(actualCompanyFood).isEqualTo(expectedCompanyFood);
    }

    @Test
    @DisplayName("memberId 일치하는 모든 음식 찾기 Test")
    void findAllFood() throws Exception {
        //given
        CompanyMemberEntity saveCompanyMember = new CompanyMemberEntity(null, "loginName",
            "password", "name", false, new Timestamp(System.currentTimeMillis()));
        em.persist(saveCompanyMember);
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findById(em, 1l).get();

        CompanyFoodEntity companyFood1 = new CompanyFoodEntity(null, findCompanyMember, "name1",
            new Timestamp(System.currentTimeMillis()), null);
        CompanyFoodEntity companyFood2 = new CompanyFoodEntity(null, findCompanyMember, "name2",
            new Timestamp(System.currentTimeMillis()), null);
        repository.add(em, companyFood1, new BigDecimal("3000"));
        repository.add(em, companyFood2, new BigDecimal("5000"));

        CompanyFoodEntity expect1 = new CompanyFoodEntity(1l, findCompanyMember, "name1",
            companyFood1.getRegistrationDate(), new BigDecimal("3000"));
        CompanyFoodEntity expect2 = new CompanyFoodEntity(2l, findCompanyMember, "name2",
            companyFood2.getRegistrationDate(), new BigDecimal("5000"));
        List<CompanyFoodEntity> resultList = new ArrayList<>();
        resultList.add(expect1);
        resultList.add(expect2);
        //when
        List<CompanyFoodEntity> allFood1 = repository.findAllFood(em, 1l);
        List<CompanyFoodEntity> allFood2 = repository.findAllFood(em, 2l);
        //then
        assertThat(allFood1).isEqualTo(resultList);
        assertThat(allFood2).isEqualTo(new ArrayList<CompanyFoodEntity>());
    }

    @Test
    @DisplayName("foodId로 음식 정보 찾기 Test")
    void findById() throws Exception {
        //given
        CompanyMemberEntity saveCompanyMember = new CompanyMemberEntity(null, "loginName",
            "password", "name", false, new Timestamp(System.currentTimeMillis()));
        em.persist(saveCompanyMember);
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findById(em, 1l).get();

        CompanyFoodEntity saveFood = new CompanyFoodEntity(null, findCompanyMember, "name",
            new Timestamp(System.currentTimeMillis()), null);
        repository.add(em, saveFood, new BigDecimal("3000"));
        //when
        CompanyFoodEntity findFood1 = repository.findById(em, 1l).orElse(null);
        CompanyFoodEntity findFood2 = repository.findById(em, 2l).orElse(null);
        //then
        assertThat(findFood1).isEqualTo(
            new CompanyFoodEntity(1l, findCompanyMember, "name", saveFood.getRegistrationDate(),
                new BigDecimal("3000")));
        assertThat(findFood2).isNull();
    }

    @Test
    @DisplayName("가격 변경 Test")
    void updatePrice() throws Exception {
        //given
        CompanyMemberEntity saveCompanyMember = new CompanyMemberEntity(null, "loginName",
            "password", "name", false, new Timestamp(System.currentTimeMillis()));
        em.persist(saveCompanyMember);
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findById(em, 1l).get();

        CompanyFoodEntity saveFood = new CompanyFoodEntity(null, findCompanyMember, "name",
            new Timestamp(System.currentTimeMillis()), null);
        repository.add(em, saveFood, new BigDecimal("3000"));
        //when
        Thread.sleep(500);
        repository.updatePrice(em, 1l, new BigDecimal("5000"));
        CompanyFoodEntity findFood = repository.findById(em, 1l).orElse(null);
        tx.commit();
        //then
        assertThat(findFood.getTempPrice()).isEqualTo(new BigDecimal("5000"));
    }
}