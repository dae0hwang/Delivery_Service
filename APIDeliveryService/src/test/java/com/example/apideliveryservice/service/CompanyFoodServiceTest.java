package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyFoodHistoryEntity;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import com.example.apideliveryservice.exception.CompanyFoodException;
import com.example.apideliveryservice.repository.CompanyFoodHistoryRepository;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
@Transactional
class CompanyFoodServiceTest {

    @Autowired
    CompanyFoodService companyFoodService;
    @Autowired
    CompanyFoodRepository companyFoodRepository;
    @Autowired
    CompanyFoodHistoryRepository companyFoodHistoryRepository;
    @Autowired
    CompanyMemberRepository companyMemberRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("음식 등록 성공 Test")
    void addFood() {
        //given
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        //when
        companyFoodService.addFood(saveCompanyMember.getId(), "foodName", new BigDecimal("3000"));
        //then
        CompanyFoodEntity savedCompanyFood = companyFoodRepository.findAll().get(0);
        CompanyFoodHistoryEntity savedCompanyFoodHistory =
            companyFoodHistoryRepository.findAll()
            .get(0);
        assertThat(savedCompanyFood).isNotNull();
        assertThat(savedCompanyFoodHistory).isNotNull();
    }

    @Test
    @DisplayName("음직 등록 실패-중복된 음식점 음식이름 Test")
    void addFood2() {
        //given
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("5000")));
        //when
        //then
        assertThatThrownBy(() -> companyFoodService.addFood(saveCompanyMember.getId(), "foodName",
            new BigDecimal("3000"))).isInstanceOf(CompanyFoodException.class);
    }

    @Test
    @DisplayName("CompanyMemberId로 모든 음식 찾기 Test")
    void findAllFoodByCompanyMemberId() {
        //given
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        List<CompanyFoodEntity> saveCompanyMemberList = Arrays.asList(
            new CompanyFoodEntity(saveCompanyMember, "foodName1", new BigDecimal("4000")),
            new CompanyFoodEntity(saveCompanyMember, "foodName2", new BigDecimal("5000")));
        companyFoodRepository.saveAll(saveCompanyMemberList);

        List<CompanyFoodDto> actualList = saveCompanyMemberList.stream().map(
                companyFoodEntity -> new CompanyFoodDto(companyFoodEntity.getId(),
                    companyFoodEntity.getCompanyMemberEntity().getId(), companyFoodEntity.getName(),
                    companyFoodEntity.getRegistrationDate(), companyFoodEntity.getPrice()))
            .collect(Collectors.toList());
        //when
        List<CompanyFoodDto> allFoodByCompanyMemberId =
            companyFoodService.findAllFoodByCompanyMemberId(saveCompanyMember.getId());
        //then
        assertThat(allFoodByCompanyMemberId).isEqualTo(actualList);
    }

    @Test
    @DisplayName("음식 찾기 id로 -존재하지 않을 경우 포함 test")
    void findFoodById() {
        //given
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("5000")));

        CompanyFoodDto actualCompanyFoodDto = new CompanyFoodDto(saveCompanyFood.getId(),
            saveCompanyFood.getCompanyMemberEntity().getId(), saveCompanyFood.getName(),
            saveCompanyFood.getRegistrationDate(), saveCompanyFood.getPrice());
        //when
        CompanyFoodDto findFoodDto = companyFoodService.findFoodById(saveCompanyFood.getId());
        //then
        assertThat(findFoodDto).isEqualTo(actualCompanyFoodDto);
        assertThatThrownBy(
            () -> companyFoodService.findFoodById(saveCompanyFood.getId() + 1L)).isInstanceOf(
            NoSuchElementException.class);
    }

    @Test
    @DisplayName("food price 변경 history 등록 Test")
    void updatePrice() {
        //given
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("3000")));
        //when
        companyFoodService.updateFoodPrice(saveCompanyFood.getId(), new BigDecimal("5000"));
        //then
        em.flush();
        em.clear();
        CompanyFoodEntity findCompanyFood = companyFoodRepository.findById(
            saveCompanyFood.getId()).orElseThrow();
        CompanyFoodHistoryEntity findCompanyFoodHistory
            = findCompanyFood.getCompanyFoodHistoryEntityList().get(0);

        assertThat(findCompanyFood.getPrice()).usingComparator(BigDecimal::compareTo)
        .isEqualTo(new BigDecimal("5000"));
        assertThat(findCompanyFoodHistory.getPrice()).usingComparator(BigDecimal::compareTo)
        .isEqualTo(new BigDecimal("5000"));
    }
}