package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.dto.RequestCompanyFoodDto;
import com.example.apideliveryservice.exception.BlackException;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.exception.NonExistentFoodIdException;
import com.example.apideliveryservice.exception.NotDigitException;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyFoodService {

    private final CompanyFoodRepository companyFoodRepository;

    /**
     *
     * @param requestCompanyFood
     * @throws SQLException
     * @throws BlackException
     * @throws DuplicatedFoodNameException
     * @throws NotDigitException
     */
    public void addFood(RequestCompanyFoodDto requestCompanyFood) throws SQLException {
        Connection connection = companyFoodRepository.connectJdbc();
        try {
            connection.setAutoCommit(false);
            validateBlankCheck(requestCompanyFood);
            validateNotDigit(requestCompanyFood);
            validateDuplicateFoodName(connection, requestCompanyFood);
            CompanyFoodDto companyFoodDto = new CompanyFoodDto(null, new BigInteger(
                requestCompanyFood.getMemberId()), requestCompanyFood.getName()
                , new BigDecimal(requestCompanyFood.getPrice()));
            companyFoodRepository.add(connection, companyFoodDto);
            connection.commit();
        } catch (BlackException e) {
            connection.rollback();
            throw new BlackException();
        } catch (NotDigitException e) {
            connection.rollback();
            throw new NotDigitException();
        } catch (DuplicatedFoodNameException e) {
            connection.rollback();
            throw new DuplicatedFoodNameException();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void validateBlankCheck(RequestCompanyFoodDto requestCompanyFoodDto) {
        if (requestCompanyFoodDto.getMemberId() == "" || requestCompanyFoodDto.getName() == ""
            || requestCompanyFoodDto.getPrice() == "") {
            throw new BlackException();
        }
    }

    private void validateNotDigit(RequestCompanyFoodDto requestCompanyFoodDto) {
        if (!requestCompanyFoodDto.getMemberId().chars().allMatch(Character::isDigit) ||
            !requestCompanyFoodDto.getPrice().chars().allMatch(Character::isDigit)) {
            throw new NotDigitException();
        }
    }

    private void validateDuplicateFoodName(Connection connection,
        RequestCompanyFoodDto requestCompanyFoodDto)
        throws SQLException {
        companyFoodRepository.findByNameAndMemberId(connection,
                new BigInteger(requestCompanyFoodDto.getMemberId())
                , requestCompanyFoodDto.getName())
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

    /**
     *
     * @param id
     * @return findFood
     * @throws SQLException
     * @throws NonExistentFoodIdException
     */
    public CompanyFoodDto findFood(String id) throws SQLException {
        try (Connection connection = companyFoodRepository.connectJdbc()) {
            CompanyFoodDto findFood = companyFoodRepository.findById(
                connection, new BigInteger(id)).orElse(null);
            if (findFood == null) {
                throw new NonExistentFoodIdException();
            }
            return findFood;
        }
    }

    /**
     *
     * @param foodId
     * @param price
     * @throws SQLException
     * @throws BlackException
     * @throws NotDigitException
     */
    public void updatePrice(String foodId, String price) throws SQLException {
        try (Connection connection = companyFoodRepository.connectJdbc()) {
            if (price == "") {
                throw new BlackException();
            } else if (!price.chars().allMatch(Character::isDigit)) {
                throw new NotDigitException();
            } else {
                companyFoodRepository.updatePrice(connection, new BigInteger(foodId),
                    new BigDecimal(price));
            }
        }
    }
}
