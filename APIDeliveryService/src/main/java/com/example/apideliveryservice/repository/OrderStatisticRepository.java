package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.FoodPriceSumDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
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
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    public List<FoodPriceSumDto> companyAllOfDay(Connection connection) throws SQLException {
        List<FoodPriceSumDto> list = new ArrayList<>();
        String sql = "select date_format(o.registration_date, '%Y%m%d') as 'date',\n"
            + "       sum(od.food_price * od.food_amount) as sum\n"
            + "       from orders as o \n"
            + "       join order_detail as od\n"
            + "       on o.id = od.order_id\n"
            + "       group by  date_format(o.registration_date, '%Y%m%d')\n"
            + "       order by o.registration_date desc";
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
        String sql = "select date_format(o.registration_date, '%Y%m') as 'date',\n"
            + "       sum(od.food_price * od.food_amount) as sum\n"
            + "       from orders as o \n"
            + "       join order_detail_list as od\n"
            + "       on o.id = od.order_id\n"
            + "       group by date_format(o.registration_date, '%Y%m')\n"
            + "       order by o.registration_date desc";
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

    //일단 스키마 정리부터하기ㅣ
   //전부 보내기
    public List<FoodPriceSumDto> companyIdOfDayTemp(Connection connection, Long companyMemberId)
        throws SQLException {
        List<FoodPriceSumDto> list = new ArrayList<>();
        String sql = "select date_format(a.registration_date, '%Y%m%d') as 'date',\n"
            + "       sum(ad.food_price*ad.food_amount) as sum\n"
            + "       from order_list as a \n"
            + "       join order_detail_list as ad\n"
            + "       on a.id = ad.order_id\n"
            + "       and ad.company_id=?\n"
            + "       group by date_format(a.registration_date, '%Y%m%d')\n"
            + "       order by a.registration_date desc";
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
