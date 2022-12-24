package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyFoodHistoryEntity;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.repository.CompanyFoodHistoryRepository;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
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

    @Test
    @DisplayName("음식 등록 성공 Test")
    void addFood() {
        //given
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false,
                new Timestamp(System.currentTimeMillis())));
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
            new CompanyMemberEntity("loginName", "password", "name", false,
                new Timestamp(System.currentTimeMillis())));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("5000"),
                new Timestamp(System.currentTimeMillis())));
        //when
        //then
        assertThatThrownBy(() -> companyFoodService.addFood(saveCompanyMember.getId(), "foodName",
            new BigDecimal("3000"))).isInstanceOf(DuplicatedFoodNameException.class);
    }

    @Test
    @DisplayName("CompanyMemberId로 모든 음식 찾기 Test")
    void findAllFoodByCompanyMemberId() {
        //given
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false,
                new Timestamp(System.currentTimeMillis())));
        List<CompanyFoodEntity> saveCompanyMemberList = Arrays.asList(
            new CompanyFoodEntity(saveCompanyMember, "foodName1", new BigDecimal("4000"),
                new Timestamp(System.currentTimeMillis())),
            new CompanyFoodEntity(saveCompanyMember, "foodName2", new BigDecimal("5000"),
                new Timestamp(System.currentTimeMillis())));
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
            new CompanyMemberEntity("loginName", "password", "name", false,
                new Timestamp(System.currentTimeMillis())));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("5000"),
                new Timestamp(System.currentTimeMillis())));

        CompanyFoodDto actualCompanyFoodDto = new CompanyFoodDto(saveCompanyFood.getId(),
            saveCompanyFood.getCompanyMemberEntity().getId(), saveCompanyFood.getName(),
            saveCompanyFood.getRegistrationDate(), saveCompanyFood.getPrice());
        //when
        CompanyFoodDto findFoodDto = companyFoodService.findFoodById(saveCompanyFood.getId());
        //then
        assertThat(findFoodDto).isEqualTo(actualCompanyFoodDto);
        assertThatThrownBy(
            () -> companyFoodService.findFoodById(saveCompanyFood.getId() + 1l)).isInstanceOf(
            NoSuchElementException.class);
    }

    @Test
    @DisplayName("food price 변경 history등록 Test")
    void updatePrice() {
        //given
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false,
                new Timestamp(System.currentTimeMillis())));
        CompanyFoodEntity saveCompanyFood = companyFoodRepository.save(
            new CompanyFoodEntity(saveCompanyMember, "foodName", new BigDecimal("5000"),
                new Timestamp(System.currentTimeMillis())));
        //when
        companyFoodService.updateFoodPrice(saveCompanyFood.getId(), new BigDecimal("5000"));

        CompanyFoodEntity findCompanyFood = companyFoodRepository.findById(saveCompanyFood.getId())
            .orElseThrow();
        CompanyFoodHistoryEntity findCompanyFoodHistory = companyFoodHistoryRepository.findById(
            saveCompanyFood.getId()).orElseThrow();
        //then
        assertThat(findCompanyFood.getPrice()).isEqualTo(new BigDecimal("5000"));
        assertThat(findCompanyFoodHistory.getPrice()).isEqualTo(new BigDecimal("5000"));
    }
}