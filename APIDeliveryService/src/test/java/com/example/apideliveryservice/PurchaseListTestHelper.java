package com.example.apideliveryservice;


import com.example.apideliveryservice.entity.PurchaseListEntity;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PurchaseListTestHelper {

    public Optional<PurchaseListEntity> findById(Connection connection, Long primaryId) throws SQLException {
        String sql = "SELECT * FROM purchase_list WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, primaryId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong(1);
                    Long generalId = resultSet.getLong(2);
                    Long companyId = resultSet.getLong(3);
                    Long foodId = resultSet.getLong(4);
                    BigDecimal foodPrice = resultSet.getBigDecimal(5);
                    Timestamp createdAt = resultSet.getTimestamp(6);
                    PurchaseListEntity purchaseList = new PurchaseListEntity(id, generalId, companyId,
                        foodId, foodPrice, createdAt);
                    return Optional.ofNullable(purchaseList);
                }
            }
        }
        return Optional.empty();
    }
}
