package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.FoodPriceSumDto;
import com.example.apideliveryservice.repository.OrderStatisticRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderStatisticService {

    private final OrderStatisticRepository orderStatisticRepository;

    public List<FoodPriceSumDto> companyAllOfDay() throws SQLException {
        try (Connection connection = orderStatisticRepository.connectHikariCp()){
            List<FoodPriceSumDto> list = orderStatisticRepository.companyAllOfDay(connection);
            return list;
        }
    }
}
