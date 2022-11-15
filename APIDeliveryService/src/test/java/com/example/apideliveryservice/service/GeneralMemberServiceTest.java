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
import java.util.ArrayList;
import java.util.List;
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

    @Test
    @DisplayName("모든 general member 찾기 test")
    void findAllMember() throws SQLException {
        //given
        GeneralMemberDto companyMemberDto1 = new GeneralMemberDto(1l, "loginName1", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        GeneralMemberDto companyMemberDto2 = new GeneralMemberDto(2l, "loginName2", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        GeneralMemberDto companyMemberDto3 = new GeneralMemberDto(3l, "loginName3", "password"
            , "name", false, new Timestamp(System.currentTimeMillis()));
        repository.create(connection, companyMemberDto1);
        repository.create(connection, companyMemberDto2);
        repository.create(connection, companyMemberDto3);
        List<GeneralMemberDto> result = new ArrayList<>();
        result.add(companyMemberDto1);
        result.add(companyMemberDto2);
        result.add(companyMemberDto3);

        //when
        List<GeneralMemberDto> expected = service.findAllMember();

        //then
        assertThat(expected.toString()).isEqualTo(result.toString());
    }

    @Test
    @DisplayName("id로멤버 찾기  성공 test")
    void findById() throws SQLException {
        //given
        GeneralMemberDto result = new GeneralMemberDto(1l, "loginName1", "password", "name", false
            , new Timestamp(System.currentTimeMillis()));

        repository.create(connection, result);
        //when
        GeneralMemberDto expected = service.findById("1");
        //then
        assertThat(expected).isEqualTo(result);
    }

    @Test
    @DisplayName("id로 멤버 찾기 실패  존재하지 않는 id test")
    void findMember2() throws SQLException {
        //given
        GeneralMemberDto result = new GeneralMemberDto(1l, "loginName1", "password", "name", false
            , new Timestamp(System.currentTimeMillis()));

        repository.create(connection, result);
        //when
        //then
        assertThatThrownBy(() ->
            service.findById("2"))
            .isInstanceOf(DeliveryServiceException.class);
    }
}