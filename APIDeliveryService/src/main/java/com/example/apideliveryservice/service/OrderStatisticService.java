package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.FoodPriceSumDto;
import com.example.apideliveryservice.repository.OrderStatisticRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderStatisticService {

    private final OrderStatisticRepository orderStatisticRepository;

    @Transactional(readOnly = true)
    public List<FoodPriceSumDto> companyAllOfDay() {
        return orderStatisticRepository.companyAllOfDay();
    }
    @Transactional(readOnly = true)
    public List<FoodPriceSumDto> companyAllOfMonth() {
        return orderStatisticRepository.companyAllOfMonth();
    }
    @Transactional(readOnly = true)
    public List<FoodPriceSumDto> companyMemberOfDay() {
        return orderStatisticRepository.companyMemberOfDay();
    }
    @Transactional(readOnly = true)
    public List<FoodPriceSumDto> companyMemberOfMonth() {
        return orderStatisticRepository.companyMemberOfMonth();
    }
    @Transactional(readOnly = true)
    public List<FoodPriceSumDto> generalMemberOfDay() {
        return orderStatisticRepository.generalMemberOfDay();
    }
    @Transactional(readOnly = true)
    public List<FoodPriceSumDto> generalMemberOfMonth() {
        return orderStatisticRepository.generalMemberOfMonth();
    }
}
