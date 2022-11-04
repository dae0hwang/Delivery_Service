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
            + "     id BIGINT AUTO_INCREMENT PRIMARY KEY ,\n"
            + "     member_id BIGINT NOT NULL ,\n"
            + "     name VARCHAR(30) NOT NULL ,\n"
            + "     price DECIMAL NOT NULL\n"
            + ")";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void ifExistDeleteCompanyMembers(Connection connection) {
        String sql ="DROP TABLE IF EXISTS company_members;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }

    public void createCompanyMembersTable(Connection connection) {
        String sql = "CREATE TABLE company_members(\n"
            + "    id BIGINT AUTO_INCREMENT PRIMARY KEY ,\n"
            + "    login_name VARCHAR(30) NOT NULL UNIQUE ,\n"
            + "    password VARCHAR(128) NOT NULL ,\n"
            + "    name VARCHAR(30) NOT NULL ,\n"
            + "    phone_verification TINYINT(1) NOT NULL,\n"
            + "    registration_date DATE NOT NULL\n"
            + ")";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            log.error("error message={}", e.getMessage(), e);
        }
    }
}
