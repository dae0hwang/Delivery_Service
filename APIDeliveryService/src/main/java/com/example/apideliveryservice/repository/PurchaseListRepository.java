package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.FoodPriceSumDto;
import com.example.apideliveryservice.entity.PurchaseListEntity;
import com.zaxxer.hikari.HikariDataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PurchaseListRepository {

    @Value("${datasource.url:@null}")
    private String url;
    @Value("${datasource.username:@null}")
    private String username;
    @Value("${datasource.password:@null}")
    private String password;

    public Connection connectHikariCp() throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaximumPoolSize(10);
        Connection connection = dataSource.getConnection();
        return connection;
    }


    public void create(EntityManager em, PurchaseListEntity purchaseListEntity) throws Exception {
        em.persist(purchaseListEntity);
    }

    public List<FoodPriceSumDto> companyAllOfDay(Connection connection) throws SQLException {
        List<FoodPriceSumDto> list = new ArrayList<>();
        String sql = "select date_format(registration_date, '%Y%m%d') as 'date',"
            + " sum(food_price) as sum"
            + " from purchase_list"
            + " group by day(registration_date)"
            + " order by 'date' desc";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    String date = resultSet.getString(1);
                    BigDecimal sum = resultSet.getBigDecimal(2);
                    FoodPriceSumDto foodPriceSumDto = new FoodPriceSumDto(date, sum);
                    list.add(foodPriceSumDto);
                }
            }
            return list;
        }
    }
}
