package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.PurchaseListDto;
import com.example.apideliveryservice.repository.PurchaseListRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseListService {

    private final PurchaseListRepository repository;

    public void addList(String general_id, String company_id, String food_id, String foodPrice)
        throws SQLException {
        PurchaseListDto purchaseListDto = new PurchaseListDto(null, Long.valueOf(general_id),
            Long.valueOf(company_id), Long.valueOf(food_id), new BigDecimal(foodPrice),
            new Timestamp(System.currentTimeMillis()));
        try (Connection connection = repository.connectJdbc();){
            repository.create(connection, purchaseListDto);
        }
    }
}
