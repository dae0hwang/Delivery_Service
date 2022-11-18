package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.PurchaseListDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
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

    public List<PurchaseListDto> findByCompanyMemberIdAndThisMonth(Connection connection,
        Long companyMemberId) throws SQLException {
        List<PurchaseListDto> list = new ArrayList<>();
        Date firstDay = Date.valueOf(YearMonth.now().atDay(1));
        Date lastDay = Date.valueOf(YearMonth.now().atEndOfMonth());
        String sql = "SELECT * FROM purchase_list"
            + " WHERE company_id= ?"
            + " AND DATE(registration_date) >= ? "
            + " AND DATE(registration_date) <= ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, companyMemberId);
            preparedStatement.setDate(2, firstDay);
            preparedStatement.setDate(3, lastDay);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    PurchaseListDto purchaseListDto = getPurchaseListDto(resultSet);
                    list.add(purchaseListDto);
                }
                return list;
            }
        }
    }

    public List<PurchaseListDto> findByGeneralMemberIdAndThisMonth(Connection connection,
        Long generalMemberId) throws SQLException {
        List<PurchaseListDto> list = new ArrayList<>();
        Date firstDay = Date.valueOf(YearMonth.now().atDay(1));
        Date lastDay = Date.valueOf(YearMonth.now().atEndOfMonth());
        String sql = "SELECT * FROM purchase_list"
            + " WHERE general_id= ?"
            + " AND DATE(registration_date) >= ? "
            + " AND DATE(registration_date) <= ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, generalMemberId);
            preparedStatement.setDate(2, firstDay);
            preparedStatement.setDate(3, lastDay);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    PurchaseListDto purchaseListDto = getPurchaseListDto(resultSet);
                    list.add(purchaseListDto);
                }
                return list;
            }
        }
    }

    private PurchaseListDto getPurchaseListDto(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(1);
        long generalId = resultSet.getLong(2);
        long companyId = resultSet.getLong(3);
        long foodId = resultSet.getLong(4);
        BigDecimal foodPrice = resultSet.getBigDecimal(5);
        Timestamp createdAt = resultSet.getTimestamp(6);
        PurchaseListDto purchaseListDto = new PurchaseListDto(id, generalId, companyId,
            foodId, foodPrice, createdAt);
        return purchaseListDto;
    }


}
