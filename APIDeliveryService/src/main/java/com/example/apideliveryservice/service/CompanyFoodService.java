package com.example.apideliveryservice.service;

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
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CompanyFoodService {

    private final CompanyFoodRepository companyFoodRepository;
    private final CompanyFoodHistoryRepository companyFoodHistoryRepository;
    private final CompanyMemberRepository companyMemberRepository;

    public void addFood(Long companyMemberId, String foodName, BigDecimal price)  {
        validateDuplicateFoodName(companyMemberId, foodName);
        saveCompanyFoodAndHistory(companyMemberId, foodName, price);
    }

    public List<CompanyFoodDto> findAllFoodByCompanyMemberId(Long companyMemberId) {
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findById(companyMemberId)
            .orElseThrow();
        List<CompanyFoodEntity> findCompanyFoodList =
            companyFoodRepository.findAllByCompanyMemberEntity(findCompanyMember);
        List<CompanyFoodDto> companyFoodDtoList = changeFoodEntityListToDto(findCompanyFoodList);
        return companyFoodDtoList;
    }

    public CompanyFoodDto findFoodById(Long companyFoodId) {
        CompanyFoodEntity findCompanyFoodEntity = companyFoodRepository.findById(companyFoodId)
            .orElseThrow();
        CompanyFoodDto findCompanyFoodDto = changeFindFoodEntityToDto(findCompanyFoodEntity);
        return findCompanyFoodDto;
    }

    public void updateFoodPrice(Long companyFoodId, BigDecimal updatePrice) {
        CompanyFoodEntity findCompanyFood = companyFoodPriceUpdate(companyFoodId, updatePrice);
        saveCompanyFoodUpdatePriceHistory(updatePrice, findCompanyFood);
    }

    private void saveCompanyFoodUpdatePriceHistory(BigDecimal updatePrice, CompanyFoodEntity findCompanyFood) {
        CompanyFoodHistoryEntity saveCompanyFoodHistory = new CompanyFoodHistoryEntity(
            findCompanyFood, updatePrice, new Timestamp(System.currentTimeMillis()));
        companyFoodHistoryRepository.save(saveCompanyFoodHistory);
    }

    private CompanyFoodEntity companyFoodPriceUpdate(Long companyFoodId, BigDecimal updatePrice) {
        CompanyFoodEntity findCompanyFood = companyFoodRepository.findById(companyFoodId)
            .orElseThrow();
        findCompanyFood.setPrice(updatePrice);
        return findCompanyFood;
    }

    private List<CompanyFoodDto> changeFoodEntityListToDto(List<CompanyFoodEntity> allFoodEntity) {
        List<CompanyFoodDto> companyFoodDtoList = allFoodEntity.stream().map(
            m -> new CompanyFoodDto(m.getId(), m.getCompanyMemberEntity().getId(), m.getName(),
                m.getRegistrationDate(), m.getPrice())).collect(Collectors.toList());
        return companyFoodDtoList;
    }

    private CompanyFoodDto changeFindFoodEntityToDto(CompanyFoodEntity findFoodEntity) {
        CompanyFoodDto findFoodDto = new CompanyFoodDto(findFoodEntity.getId(),
            findFoodEntity.getCompanyMemberEntity().getId(), findFoodEntity.getName(),
            findFoodEntity.getRegistrationDate(), findFoodEntity.getPrice());
        return findFoodDto;
    }

    private void saveCompanyFoodAndHistory(Long companyMemberId, String foodName, BigDecimal price) {
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findById(companyMemberId)
            .orElseThrow();
        CompanyFoodEntity saveCompanyFood = new CompanyFoodEntity(findCompanyMember, foodName,
            price, new Timestamp(System.currentTimeMillis()));
        companyFoodRepository.save(saveCompanyFood);
        CompanyFoodHistoryEntity saveCompanyFoodHistory = new CompanyFoodHistoryEntity(
            saveCompanyFood, price, saveCompanyFood.getRegistrationDate());
        companyFoodHistoryRepository.save(saveCompanyFoodHistory);
    }

    private void validateDuplicateFoodName(Long companyMemberId, String foodName) {
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findById(companyMemberId)
            .orElseThrow();
        companyFoodRepository.findByNameAndCompanyMemberEntity(foodName, findCompanyMember)
            .ifPresent(companyFood->{
                throw new DuplicatedFoodNameException();
            });
    }
}
