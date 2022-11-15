package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.GeneralMemberDto;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("db-h2")
@Slf4j
class GeneralMemberRepositoryTest {

    @Autowired
    GeneralMemberRepository repository;
    @Autowired
    RepositoryResetHelper resetHelper;
    Connection connection;

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = repository.connectJdbc();
        resetHelper.ifExistDeleteGeneralMembers(connection);
        resetHelper.createGeneralMembersTable(connection);
    }

    @Test
    @DisplayName("generalMember create findByLoginId 동시 Test")
    void create() throws SQLException {
        //given
        GeneralMemberDto generalMemberDto = new GeneralMemberDto(1L, "loginName", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        //when
        repository.create(connection, generalMemberDto);
        GeneralMemberDto findMember = repository.findByLoginName(connection, "loginName")
            .orElse(null);
        //then
        assertThat(findMember.toString()).isEqualTo(generalMemberDto.toString());
    }
}