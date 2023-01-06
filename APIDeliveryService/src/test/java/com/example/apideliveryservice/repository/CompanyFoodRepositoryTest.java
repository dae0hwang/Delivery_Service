package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Slf4j
@ActiveProfiles("test")
class CompanyFoodRepositoryTest {

    @Autowired
    CompanyFoodRepository companyFoodRepository;
    @Autowired
    CompanyMemberRepository companyMemberRepository;
    @PersistenceContext
    EntityManager em;


    @Test
    @DisplayName("같은 음식점 중복된 음식 체크 Test")
    void findByNameAndCompanyMemberEntity() {
        //given
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        companyFoodRepository.save(new CompanyFoodEntity(saveCompanyMember, "foodName",
            new BigDecimal("3000")));
        //when
        CompanyFoodEntity existCompanyFood =
            companyFoodRepository.findByNameAndCompanyMemberEntity(
                "foodName", saveCompanyMember).orElse(null);
        CompanyFoodEntity noExistCompanyFood =
            companyFoodRepository.findByNameAndCompanyMemberEntity(
                "differentName", saveCompanyMember).orElse(null);
        //then
        assertThat(existCompanyFood).isNotNull();
        assertThat(noExistCompanyFood).isNull();
    }

    @Test
    @DisplayName("CompanyMember의 모든 CompanyFood List 가져오기 Test")
    void findAllByCompanyMemberEntity() {
        //given
        CompanyMemberEntity saveCompanyMember = new CompanyMemberEntity("loginName",
            "password", "name", false);

        em.persist(saveCompanyMember);
        List<CompanyFoodEntity> saveCompanyMemberList = Arrays.asList(
            new CompanyFoodEntity(saveCompanyMember, "foodName1", new BigDecimal("4000")),
            new CompanyFoodEntity(saveCompanyMember, "foodName2", new BigDecimal("5000")));
        companyFoodRepository.saveAll(saveCompanyMemberList);
        //when
        List<CompanyFoodEntity> findCompanyMemberList =
            companyFoodRepository.findAllByCompanyMemberEntity(
                saveCompanyMember);
        //then
        assertThat(findCompanyMemberList).isEqualTo(saveCompanyMemberList);
    }

}