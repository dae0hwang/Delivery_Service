package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.PurchaseListDto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PurchaseListRepository {

    @Value("${datasource.url:@null}")
    private String url;
    @Value("${datasource.username:@null}")
    private String username;
    @Value("${datasource.password:@null}")
    private String password;

    public Connection connectJdbc() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void create(Connection connection, PurchaseListDto purchaseListDto) throws SQLException {
        String sql = "INSERT INTO purchase_list (general_id, company_id, food_id, food_price,"
            + " registration_date) VALUES (?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, purchaseListDto.getGeneralId());
            preparedStatement.setLong(2, purchaseListDto.getCompanyId());
            preparedStatement.setLong(3, purchaseListDto.getFoodId());
            preparedStatement.setBigDecimal(4, purchaseListDto.getFoodPrice());
            preparedStatement.setTimestamp(5, purchaseListDto.getCreatedAt());
            preparedStatement.executeUpdate();
        }
    }
}
