package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyFoodService {

    private final CompanyFoodRepository companyFoodRepository;
    private final CompanyMemberRepository companyMemberRepository;

    public void addFood(CompanyFoodDto companyFoodDto) throws SQLException {
        Connection connection = companyFoodRepository.connectJdbc();
        try {
            connection.setAutoCommit(false);
            validateDuplicateFoodName(connection, companyFoodDto);
            companyFoodRepository.add(connection, companyFoodDto);
            connection.commit();
        } catch (DuplicatedFoodNameException e) {
            connection.rollback();
            throw new DuplicatedFoodNameException();
        }catch (NonExistentMemberIdException e) {
            connection.rollback();
            throw new NonExistentMemberIdException();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void validateDuplicateFoodName(Connection connection, CompanyFoodDto companyFoodDto) {
        companyFoodRepository.findByName(connection, companyFoodDto.getMemberId()
                ,companyFoodDto.getName())
            .ifPresent(m -> {
                throw new DuplicatedFoodNameException();
            });
    }
}
