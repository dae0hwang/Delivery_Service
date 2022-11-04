package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
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
public class CompanyMemberRepository {

    @Value("${datasource.url}")
    private String url;
    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;

    public Connection connectJdbc() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void save(Connection connection, CompanyMemberDto companyMember) throws SQLException {
        String sql = "INSERT INTO company_members "
            + "(login_name, password, name, phone_verification, registration_date)"
            + "VALUES(?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, companyMember.getLoginName());
            preparedStatement.setString(2, companyMember.getPassword());
            preparedStatement.setString(3, companyMember.getName());
            preparedStatement.setInt(4, companyMember.getPhoneVerification());
            preparedStatement.setDate(5, companyMember.getCreatedAt());

            preparedStatement.executeUpdate();
        }
    }

    public Optional<CompanyMemberDto> findByLoginName(Connection connection, String loginName)
    throws SQLException{
        String sql = "SELECT * FROM company_members WHERE login_name=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, loginName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    CompanyMemberDto companyMemberDto = getCompanyMemberDto(resultSet);
                    return Optional.ofNullable(companyMemberDto);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<CompanyMemberDto> findById(Connection connection, BigInteger id)
    throws SQLException{
        String sql = "SELECT * FROM company_members WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, new BigDecimal(id));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    CompanyMemberDto companyMemberDto = getCompanyMemberDto(resultSet);
                    return Optional.ofNullable(companyMemberDto);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<List<CompanyMemberDto>> findAllMember(Connection connection)
    throws SQLException{
        String sql = "SELECT * FROM company_members";
        List<CompanyMemberDto> list = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    CompanyMemberDto companyMemberDto = getCompanyMemberDto(resultSet);
                    list.add(companyMemberDto);
                }
                return Optional.ofNullable(list);
            }
        }
    }

    private CompanyMemberDto getCompanyMemberDto(ResultSet resultSet) throws SQLException {
        BigInteger id = resultSet.getBigDecimal(1).toBigInteger();
        String loginName = resultSet.getString(2);
        String password = resultSet.getString(3);
        String name = resultSet.getString(4);
        int phoneVerification = resultSet.getInt(5);
        Date createAt = resultSet.getDate(6);
        return new CompanyMemberDto(id, loginName, password, name, phoneVerification
            , createAt);
    }
}
