package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.GeneralMemberDto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GeneralMemberRepository {

    @Value("${datasource.url:@null}")
    private String url;
    @Value("${datasource.username:@null}")
    private String username;
    @Value("${datasource.password:@null}")
    private String password;

    public Connection connectJdbc() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void create(Connection connection, GeneralMemberDto generalMemberDto)
        throws SQLException {
        String sql = "INSERT INTO general_members "
            + "(login_name, password, name, phone_verification, registration_date)"
            + "VALUES(?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, generalMemberDto.getLoginName());
            preparedStatement.setString(2, generalMemberDto.getPassword());
            preparedStatement.setString(3, generalMemberDto.getName());
            preparedStatement.setBoolean(4, generalMemberDto.getPhoneVerification());
            preparedStatement.setTimestamp(5, generalMemberDto.getCreatedAt());
            preparedStatement.executeUpdate();
        }
    }

    public Optional<GeneralMemberDto> findByLoginName(Connection connection, String loginName)
        throws SQLException {
        String sql = "SELECT * FROM general_members WHERE login_name=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, loginName);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()) {
                    GeneralMemberDto generalMemberDto = getGeneralMember(resultSet);
                    return Optional.ofNullable(generalMemberDto);
                }
            }
        }
        return Optional.empty();
    }

    private GeneralMemberDto getGeneralMember(ResultSet resultSet) throws SQLException {
        log.info(resultSet.toString());
        Long id = resultSet.getLong(1);
        String loginName = resultSet.getString(2);
        String password = resultSet.getString(3);
        String name = resultSet.getString(4);
        Boolean phoneVerification = resultSet.getBoolean(5);
        Timestamp createAt = resultSet.getTimestamp(6);
        return new GeneralMemberDto(id, loginName, password, name, phoneVerification
            , createAt);
    }

    public Optional<List<GeneralMemberDto>> findAll(Connection connection) throws SQLException {
        String sql = "SELECT * FROM general_members";
        List<GeneralMemberDto> list = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GeneralMemberDto generalMember = getGeneralMember(resultSet);
                    list.add(generalMember);
                }
                return Optional.of(list);
            }
        }
    }

    public Optional<GeneralMemberDto> findById(Connection connection, Long id) throws SQLException {
        String sql = "SELECT * FROM general_members WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()) {
                    GeneralMemberDto generalMemberDto = getGeneralMember(resultSet);
                    return Optional.ofNullable(generalMemberDto);
                }
            }
        }
        return Optional.empty();
    }
}
