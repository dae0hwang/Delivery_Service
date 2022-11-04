package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.dto.CompanyMemberDto;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompanyFoodRepository {

    @Value("${datasource.url}")
    private String url;
    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;

    public Connection connectJdbc() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void add(Connection connection, CompanyFoodDto companyFoodDto) throws SQLException {
        String sql = "INSERT INTO company_food "
            + "(member_id, name, price) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement
                .setBigDecimal(1, new BigDecimal(companyFoodDto.getMemberId()));
            preparedStatement.setString(2, companyFoodDto.getName());
            preparedStatement.setBigDecimal(3, companyFoodDto.getPrice());
            preparedStatement.executeUpdate();
        }
    }

    public Optional<CompanyFoodDto> findByNameAndMemberId(Connection connection, BigInteger memberId
        , String findName) throws SQLException {
        String sql = "SELECT * FROM company_food WHERE member_id=? AND name=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, new BigDecimal(memberId));
            preparedStatement.setString(2, findName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    CompanyFoodDto companyFoodDto = getCompanyFoodDto(resultSet);
                    return Optional.ofNullable(companyFoodDto);
                }
            }
        }
        return Optional.empty();
    }

    private CompanyFoodDto getCompanyFoodDto(ResultSet resultSet) throws SQLException {
        BigInteger id = resultSet.getBigDecimal(1).toBigInteger();
        BigInteger memberId = resultSet.getBigDecimal(2).toBigInteger();
        String name = resultSet.getString(3);
        BigDecimal price = resultSet.getBigDecimal(4);
        return new CompanyFoodDto(id, memberId, name, price);
    }

    public Optional<List<CompanyFoodDto>> findAllFood(Connection connection, BigInteger id)
        throws SQLException {
        String sql = "SELECT * FROM company_food WHERE member_id = ?";
        List<CompanyFoodDto> list = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, new BigDecimal(id));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    CompanyFoodDto companyFoodDto = getCompanyFoodDto(resultSet);
                    list.add(companyFoodDto);
                }
                return Optional.ofNullable(list);
            }
        }
    }
}
