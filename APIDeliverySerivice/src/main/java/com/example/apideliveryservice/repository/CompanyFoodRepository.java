package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompanyFoodRepository {

    public Connection connectJdbc() throws SQLException {
        String server = "localhost:3307";
        String database = "delivery_service";
        String user_name = "root";
        String password = "111111";
        return DriverManager.getConnection(
            "jdbc:mysql://" + server + "/" + database + "?useSSL=false", user_name, password);
    }

    public void add(Connection connection, CompanyFoodDto companyFoodDto) throws SQLException {
        String sql = "INSERT INTO company_food "
            + "(member_id, name, price) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, companyFoodDto.getMemberId());
            preparedStatement.setString(2, companyFoodDto.getName());
            preparedStatement.setInt(3, companyFoodDto.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw e;
        }
    }

    public Optional<CompanyFoodDto> findByName(Connection connection, int memberId
        , String findName) {
        String sql = "SELECT * FROM company_food WHERE member_id=? AND name=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, memberId);
            preparedStatement.setString(2, findName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    CompanyFoodDto companyFoodDto = getCompanyFoodDto(resultSet);
                    return Optional.ofNullable(companyFoodDto);
                }
            }
        } catch (SQLException e) {
            log.error("SQLException", e);
        }
        return Optional.empty();
    }

    private CompanyFoodDto getCompanyFoodDto(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt(1);
        int memberId = resultSet.getInt(2);
        String name = resultSet.getString(3);
        int price = resultSet.getInt(4);
        return new CompanyFoodDto(id, memberId, name, price);
    }
}
