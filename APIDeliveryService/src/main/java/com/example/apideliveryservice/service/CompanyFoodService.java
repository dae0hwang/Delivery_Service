package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.exception.BlackException;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import java.math.BigInteger;
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
     *
     * @param companyFoodDto
     * @throws SQLException
     * @throws BlackException
     * @throws DuplicatedFoodNameException
     */
    public void addFood(CompanyFoodDto companyFoodDto) throws SQLException {
        Connection connection = companyFoodRepository.connectJdbc();
        try {
            connection.setAutoCommit(false);
            validateBlankCheck(companyFoodDto);
            validateDuplicateFoodName(connection, companyFoodDto);
            companyFoodRepository.add(connection, companyFoodDto);
            connection.commit();
        } catch (BlackException e) {
            connection.rollback();
            throw new BlackException();
        } catch (DuplicatedFoodNameException e) {
            connection.rollback();
            throw new DuplicatedFoodNameException();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void validateBlankCheck(CompanyFoodDto companyFoodDto) {
        if (companyFoodDto.getMemberId() == null || companyFoodDto.getName() == ""
            || companyFoodDto.getPrice() == null) {
            throw new BlackException();
        }
    }

    private void validateDuplicateFoodName(Connection connection, CompanyFoodDto companyFoodDto)
        throws SQLException {
        companyFoodRepository.findByNameAndMemberId(connection, companyFoodDto.getMemberId()
                , companyFoodDto.getName())
            .ifPresent(m -> {
                throw new DuplicatedFoodNameException();
            });
    }

    /**
     *
     * @param memberId
     * @return foodList found by memberId
     * @throws SQLException
     */
    public List<CompanyFoodDto> findAllFood(String memberId) throws SQLException {
        try (Connection connection = companyFoodRepository.connectJdbc()){
            List<CompanyFoodDto> foodList = companyFoodRepository.findAllFood(connection,
                new BigInteger(memberId)).orElse(new ArrayList<CompanyFoodDto>());
            return foodList;
        }
    }
}
