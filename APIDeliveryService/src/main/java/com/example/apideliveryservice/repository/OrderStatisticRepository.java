package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.FoodPriceSumDto;
import com.zaxxer.hikari.HikariDataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderStatisticRepository {

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

    public List<FoodPriceSumDto> companyAllOfDay(Connection connection) throws SQLException {
        List<FoodPriceSumDto> list = new ArrayList<>();
        String sql = "select date_format(a.registration_date, '%Y%m%d') as 'date',\n"
            + "       sum(ad.food_price*ad.food_amount) as sum\n"
            + "       from order_list as a \n"
            + "       join order_detail_list as ad\n"
            + "       on a.id = ad.order_id\n"
            + "       group by day(a.registration_date), month(a.registration_date), "
            + "       year(a.registration_date)\n"
            + "       order by 'date' desc";
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

    public List<FoodPriceSumDto> companyAllOfMonth(Connection connection) throws SQLException {
        List<FoodPriceSumDto> list = new ArrayList<>();
        String sql = "select date_format(a.registration_date, '%Y%m') as 'date',\n"
            + "       sum(ad.food_price*ad.food_amount) as sum\n"
            + "       from order_list as a \n"
            + "       join order_detail_list as ad\n"
            + "       on a.id = ad.order_id\n"
            + "       group by  month(a.registration_date), year(a.registration_date)\n"
            + "       order by 'date' desc";
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

   public List<FoodPriceSumDto> companyIdOfDay(Connection connection, Long companyMemberId)
        throws SQLException {
        List<FoodPriceSumDto> list = new ArrayList<>();
        String sql = "select date_format(a.registration_date, '%Y%m%d') as 'date',\n"
            + "       sum(ad.food_price*ad.food_amount) as sum\n"
            + "       from order_list as a \n"
            + "       join order_detail_list as ad\n"
            + "       on a.id = ad.order_id\n"
            + "       and ad.company_id=?\n"
            + "       group by day(a.registration_date), month(a.registration_date), year(a.registration_date)\n"
            + "       order by 'date' desc";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, companyMemberId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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

    public List<FoodPriceSumDto> companyIdOfMonth(Connection connection, Long companyMemberId)
        throws SQLException {
        List<FoodPriceSumDto> list = new ArrayList<>();
        String sql = "select date_format(a.registration_date, '%Y%m%d') as 'date',\n"
            + "       sum(ad.food_price*ad.food_amount) as sum\n"
            + "       from order_list as a \n"
            + "       join order_detail_list as ad\n"
            + "       on a.id = ad.order_id\n"
            + "       and ad.company_id=?\n"
            + "       group by month(a.registration_date), year(a.registration_date)\n"
            + "       order by 'date' desc";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, companyMemberId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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

    public List<FoodPriceSumDto> generalIdOfDay(Connection connection, Long generalMemberId)
        throws SQLException {
        List<FoodPriceSumDto> list = new ArrayList<>();
        String sql = "select date_format(a.registration_date, '%Y%m%d') as 'date',\n"
            + "       sum(ad.food_price*ad.food_amount) as sum\n"
            + "       from order_list as a \n"
            + "       join order_detail_list as ad\n"
            + "       on a.id = ad.order_id\n"
            + "       and a.general_id=?\n"
            + "       group by day(a.registration_date), month(a.registration_date), year(a.registration_date)\n"
            + "       order by 'date' desc";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, generalMemberId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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

    public List<FoodPriceSumDto> generalIdOfMonth(Connection connection, Long generalMemberId)
        throws SQLException {
        List<FoodPriceSumDto> list = new ArrayList<>();
        String sql = "select date_format(a.registration_date, '%Y%m%d') as 'date',\n"
            + "       sum(ad.food_price*ad.food_amount) as sum\n"
            + "       from order_list as a \n"
            + "       join order_detail_list as ad\n"
            + "       on a.id = ad.order_id\n"
            + "       and a.general_id=?\n"
            + "       group by month(a.registration_date), year(a.registration_date)\n"
            + "       order by 'date' desc";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, generalMemberId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
