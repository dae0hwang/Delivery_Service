package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.GeneralMemberDto;
import com.example.apideliveryservice.exception.DeliveryServiceException;
import com.example.apideliveryservice.repository.GeneralMemberRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("db-h2")
class GeneralMemberServiceTest {

    @Autowired
    GeneralMemberRepository repository;
    @Autowired
    GeneralMemberService service;
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
    @DisplayName("회원 가입 성공 Test")
    void join1() throws SQLException {
        //given

        //when
        service.join("loginName", "password", "name");
        GeneralMemberDto findMember = repository.findByLoginName(connection, "loginName")
            .orElse(null);
        //then
        assertThat(findMember).isNotNull();
    }

    @Test
    @DisplayName("회원 가입 실패 중복된 loginName Test")
    void join2() throws SQLException {
        //given
        GeneralMemberDto firstSaveMember = new GeneralMemberDto(null, "loginName", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        repository.create(connection, firstSaveMember);
        //when
        //then
        assertThatThrownBy(() ->
            service.join("loginName", "password2", "name"))
            .isInstanceOf(DeliveryServiceException.class);
    }
}