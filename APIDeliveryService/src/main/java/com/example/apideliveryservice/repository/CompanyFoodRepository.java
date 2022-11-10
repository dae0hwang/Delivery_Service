package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import java.math.BigDecimal;
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
            preparedStatement.setLong(1, companyFoodDto.getMemberId());
            preparedStatement.setString(2, companyFoodDto.getName());
            preparedStatement.setBigDecimal(3, companyFoodDto.getPrice());
            preparedStatement.executeUpdate();
        }
    }

    public Optional<CompanyFoodDto> findByNameAndMemberId(Connection connection, Long memberId
        , String findName) throws SQLException {
        String sql = "SELECT * FROM company_food WHERE member_id=? AND name=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, memberId);
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
        Long id = resultSet.getLong(1);
        Long memberId = resultSet.getLong(2);
        String name = resultSet.getString(3);
        BigDecimal price = resultSet.getBigDecimal(4);
        return new CompanyFoodDto(id, memberId, name, price);
    }

    public Optional<List<CompanyFoodDto>> findAllFood(Connection connection, Long id)
        throws SQLException {
        String sql = "SELECT * FROM company_food WHERE member_id = ?";
        List<CompanyFoodDto> list = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    CompanyFoodDto companyFoodDto = getCompanyFoodDto(resultSet);
                    list.add(companyFoodDto);
                }
                return Optional.ofNullable(list);
            }
        }
    }

    public Optional<CompanyFoodDto> findById(Connection connection, Long id)
        throws SQLException {
        String sql = "SELECT * FROM company_food WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    CompanyFoodDto companyFoodDto = getCompanyFoodDto(resultSet);
                    return Optional.ofNullable(companyFoodDto);
                }
            }
            return Optional.empty();
        }
    }

    public void updatePrice(Connection connection, Long id,
        BigDecimal price) throws SQLException {
        String sql = "UPDATE company_food SET price=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, price);
            preparedStatement.setBigDecimal(2, new BigDecimal(id));
            preparedStatement.executeUpdate();
        }
    }
}
