package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.PurchaseListDto;
import com.example.apideliveryservice.repository.PurchaseListRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseListService {

    private final PurchaseListRepository repository;

    public void addList(String generalId, String companyId, String foodId, String foodPrice)
        throws SQLException {
        PurchaseListDto purchaseListDto = new PurchaseListDto(null, Long.valueOf(generalId),
            Long.valueOf(companyId), Long.valueOf(foodId), new BigDecimal(foodPrice),
            new Timestamp(System.currentTimeMillis()));
        try (Connection connection = repository.connectJdbc()){
            repository.create(connection, purchaseListDto);
        }
    }

    public List<PurchaseListDto> findByCompanyMemberIdAndThisMonth(String companyMemberId)
        throws SQLException {
        try (Connection connection = repository.connectJdbc()) {
            List<PurchaseListDto> byCompanyMemberIdAndThisMonth
                = repository.findByCompanyMemberIdAndThisMonth(connection,
                Long.valueOf(companyMemberId));
            return byCompanyMemberIdAndThisMonth;
        }
    }

    public List<PurchaseListDto> findByGeneralMemberIdAndThisMonth(String generalMemberId)
        throws SQLException {
        try (Connection connection = repository.connectJdbc()) {
            List<PurchaseListDto> byGeneralMemberIdAndThisMonth = repository.findByGeneralMemberIdAndThisMonth(
                connection, Long.valueOf(generalMemberId));
            return byGeneralMemberIdAndThisMonth;
        }
    }
}
