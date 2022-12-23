package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
@Transactional
@SpringBootTest
@Slf4j
//@Rollback(value = false)
@ActiveProfiles("test")
class CompanyFoodHistoryRepositoryTest {

    @Autowired
    CompanyFoodRepository companyFoodRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("스프링데이터 save, findById")
    void saveFoodAndFindTest() {
        //given
        CompanyMemberEntity saveCompanyMember = new CompanyMemberEntity("loginName", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        em.persist(saveCompanyMember);
        CompanyFoodEntity saveCompanyFood = new CompanyFoodEntity(saveCompanyMember, "foodName",
            new BigDecimal("3000"), new Timestamp(System.currentTimeMillis()));
        //when
        companyFoodRepository.save(saveCompanyFood);
        CompanyFoodEntity findCompanyFood = companyFoodRepository.findById(1l).orElse(null);
        //then
        assertThat(saveCompanyFood).isEqualTo(findCompanyFood);
    }

}