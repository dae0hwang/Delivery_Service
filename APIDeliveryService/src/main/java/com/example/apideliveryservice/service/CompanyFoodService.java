package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.exception.NonExistentFoodIdException;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyFoodService {

    private final CompanyFoodRepository companyFoodRepository;

    /**
     * @param memberId, name, price
     * @throws SQLException
     * @throws DuplicatedFoodNameException
     */
    public void addFood(String memberId, String name, String price) throws SQLException {
        Connection connection = companyFoodRepository.connectJdbc();
        CompanyFoodDto companyFoodDto = new CompanyFoodDto(null, Long.valueOf(memberId), name,
            new BigDecimal(price));
        try {
            connection.setAutoCommit(false);
            validateDuplicateFoodName(connection, companyFoodDto.getMemberId(),
                companyFoodDto.getName());
            companyFoodRepository.add(connection, companyFoodDto);
            connection.commit();
        } catch (DuplicatedFoodNameException e) {
            connection.rollback();
            throw new DuplicatedFoodNameException();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void validateDuplicateFoodName(Connection connection, Long memberId, String foodName)
        throws SQLException {
        companyFoodRepository.findByNameAndMemberId(connection, memberId, foodName)
            .ifPresent(m -> {
                throw new DuplicatedFoodNameException();
            });
    }

    /**
     * @param memberId
     * @return foodList found by memberId
     * @throws SQLException
     */
    public List<CompanyFoodDto> findAllFood(String memberId) throws SQLException {
        try (Connection connection = companyFoodRepository.connectJdbc()) {
            List<CompanyFoodDto> foodList = companyFoodRepository.findAllFood(connection,
                Long.valueOf(memberId)).orElse(new ArrayList<CompanyFoodDto>());
            return foodList;
        }
    }

    /**
     * @param id
     * @return findFood
     * @throws SQLException
     * @throws NonExistentFoodIdException
     */
    public CompanyFoodDto findFood(String id) throws SQLException {
        try (Connection connection = companyFoodRepository.connectJdbc()) {
            CompanyFoodDto findFood = companyFoodRepository.findById(connection, Long.valueOf(id))
                .orElse(null);
            if (findFood == null) {
                throw new NonExistentFoodIdException();
            }
            return findFood;
        }
    }

    /**
     * @param foodId
     * @param price
     * @throws SQLException
     */
    public void updatePrice(String foodId, String price) throws SQLException {
        try (Connection connection = companyFoodRepository.connectJdbc()) {
            companyFoodRepository.updatePrice(connection, Long.valueOf(foodId),
                new BigDecimal(price));
        }
    }
}
