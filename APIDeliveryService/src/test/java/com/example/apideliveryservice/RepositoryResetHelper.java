package com.example.apideliveryservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RepositoryResetHelper {

    public void ifExistDeleteCompanyFood(Connection connection) {
        String sql ="DROP TABLE IF EXISTS company_food";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void createCompanyFoodTable(Connection connection) {
        String sql = "CREATE TABLE company_food(\n"
            + "         id BIGINT AUTO_INCREMENT PRIMARY KEY ,\n"
            + "         company_member_id BIGINT NOT NULL ,\n"
            + "         name VARCHAR(30) NOT NULL ,\n"
            + "         registration_date TIMESTAMP NOT NULL)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void ifExistDeleteCompanyFoodPrice(Connection connection) {
        String sql ="DROP TABLE IF EXISTS company_food_price";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void createCompanyFoodPriceTable(Connection connection) {
        String sql = "CREATE TABLE company_food_price(\n"
            + "       id BIGINT AUTO_INCREMENT PRIMARY KEY ,\n"
            + "       company_food_id BIGINT NOT NULL ,\n"
            + "       price DECIMAL NOT NULL,\n"
            + "       update_date TIMESTAMP NOT NULL)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void ifExistDeleteCompanyMembers(Connection connection) {
        String sql ="DROP TABLE IF EXISTS company_member";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void createCompanyMembersTable(Connection connection) {
        String sql = "CREATE TABLE company_member(\n"
            + "       id BIGINT AUTO_INCREMENT PRIMARY KEY ,\n"
            + "       login_name VARCHAR(30) NOT NULL UNIQUE ,\n"
            + "       password VARCHAR(128) NOT NULL ,\n"
            + "       name VARCHAR(30) NOT NULL ,\n"
            + "       phone_verification TINYINT(1) NOT NULL,\n"
            + "       registration_date TIMESTAMP NOT NULL\n)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void ifExistDeleteGeneralMembers(Connection connection) {
        String sql ="DROP TABLE IF EXISTS general_member";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void createGeneralMembersTable(Connection connection) {
        String sql = "CREATE TABLE general_member(\n"
            + "    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n"
            + "    login_name VARCHAR(30) NOT NULL UNIQUE ,\n"
            + "    password VARCHAR(128) NOT NULL ,\n"
            + "    name VARCHAR(30) NOT NULL ,\n"
            + "    phone_verification TINYINT(1) NOT NULL,\n"
            + "    registration_date TIMESTAMP NOT NULL)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void ifExistDeleteOrderList(Connection connection) {
        String sql ="DROP TABLE IF EXISTS orders";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void createOrderList(Connection connection) {
        String sql = "CREATE TABLE orders(\n"
            + "      id BIGINT AUTO_INCREMENT PRIMARY KEY,\n"
            + "      general_member_id BIGINT NOT NULL ,\n"
            + "      registration_date TIMESTAMP NOT NULL)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void ifExistDeleteOrderDetailList(Connection connection) {
        String sql ="DROP TABLE IF EXISTS order_detail";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void createOrderDetailList(Connection connection) {
        String sql = "CREATE TABLE order_detail(\n"
            + "      id BIGINT AUTO_INCREMENT PRIMARY KEY,\n"
            + "      order_id BIGINT NOT NULL,\n"
            + "      company_member_id BIGINT NOT NULL ,\n"
            + "      company_food_id BIGINT NOT NULL ,\n"
            + "      food_price DECIMAL NOT NULL,\n"
            + "      food_amount  INT NOT NULL\n)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }
}
