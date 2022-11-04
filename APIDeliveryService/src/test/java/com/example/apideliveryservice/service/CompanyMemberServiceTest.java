package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.exception.BlackException;
import com.example.apideliveryservice.exception.DuplicatedLoginNameException;
import com.example.apideliveryservice.exception.NonExistentMemberIdException;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
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
class CompanyMemberServiceTest {

    @Autowired
    CompanyMemberService service;
    @Autowired
    CompanyMemberRepository repository;
    @Autowired
    RepositoryResetHelper resetHelper;
    Connection connection;


    @BeforeEach
    void beforeEach() throws SQLException {
        connection = repository.connectJdbc();
        resetHelper.ifExistDeleteCompanyMembers(connection);
        resetHelper.createCompanyMembersTable(connection);
    }

    @Test
    @DisplayName("정상 회원 가입 테스트")
    void joinTest1() throws SQLException {
        //given
        CompanyMemberDto companyMemberDto = new CompanyMemberDto(new BigInteger("1")
            , "loginName", "password", "name", 0
            , new Date(System.currentTimeMillis()));
        //when
        service.join(companyMemberDto);
        CompanyMemberDto findMember
            = repository.findByLoginName(connection, "loginName").orElse(null);
        //then
        assertThat(companyMemberDto.toString()).isEqualTo(findMember.toString());
    }

    @Test
    @DisplayName("중복된 loginName 회원 가입 실패 테스트")
    void joinTest2() throws SQLException {
        //given
        CompanyMemberDto companyMemberDto1 = new CompanyMemberDto(new BigInteger("1")
            , "loginName", "password1", "name1", 0
            , new Date(System.currentTimeMillis()));
        repository.save(connection, companyMemberDto1);

        CompanyMemberDto companyMemberDto2 = new CompanyMemberDto(new BigInteger("2")
            , "loginName", "password2", "name2", 0
            , new Date(System.currentTimeMillis()));
        //then
        assertThatThrownBy(() ->
            //when
            service.join(companyMemberDto2))
            .isInstanceOf(DuplicatedLoginNameException.class);
    }

    @Test
    @DisplayName("비어진 Request input으로 회원 가입 실패 테스트")
    void joinTest3() throws SQLException {
        //given
        CompanyMemberDto companyMemberDto1 = new CompanyMemberDto(new BigInteger("1")
            , "", "password", "name", 0
            , new Date(System.currentTimeMillis()));
        CompanyMemberDto companyMemberDto2 = new CompanyMemberDto(new BigInteger("1")
            , "loginName", "", "name", 0
            , new Date(System.currentTimeMillis()));
        CompanyMemberDto companyMemberDto3 = new CompanyMemberDto(new BigInteger("1")
            , "loginName", "password", "", 0
            , new Date(System.currentTimeMillis()));

        //then
        assertThatThrownBy(() ->
            //when
            service.join(companyMemberDto1))
            .isInstanceOf(BlackException.class);
        assertThatThrownBy(() ->
            //when
            service.join(companyMemberDto2))
            .isInstanceOf(BlackException.class);
        assertThatThrownBy(() ->
            //when
            service.join(companyMemberDto3))
            .isInstanceOf(BlackException.class);
    }

    @Test
    @DisplayName("모든 company member 찾기 test")
    void findAllMember() throws SQLException {
        //given
        CompanyMemberDto companyMemberDto1 = new CompanyMemberDto(new BigInteger("1")
            , "loginName1", "password", "name", 0
            , new Date(System.currentTimeMillis()));
        CompanyMemberDto companyMemberDto2 = new CompanyMemberDto(new BigInteger("2")
            , "loginName2", "password", "name", 0
            , new Date(System.currentTimeMillis()));
        CompanyMemberDto companyMemberDto3 = new CompanyMemberDto(new BigInteger("3")
            , "loginName3", "password", "name", 0
            , new Date(System.currentTimeMillis()));
        repository.save(connection, companyMemberDto1);
        repository.save(connection, companyMemberDto2);
        repository.save(connection, companyMemberDto3);
        List<CompanyMemberDto> result = new ArrayList<>();
        result.add(companyMemberDto1);
        result.add(companyMemberDto2);
        result.add(companyMemberDto3);

        //when
        List<CompanyMemberDto> expected = service.findAllMember();

        //then
        assertThat(expected.toString()).isEqualTo(result.toString());
    }

    @Test
    @DisplayName("company member 찾기 test")
    void findMember1() throws SQLException {
        //given
        CompanyMemberDto result = new CompanyMemberDto(new BigInteger("1")
            , "loginName1", "password", "name", 0
            , new Date(System.currentTimeMillis()));

        repository.save(connection, result);
        //when
        CompanyMemberDto expected = service.findMember("1");
        //then
        assertThat(expected.toString()).isEqualTo(result.toString());
    }

    @Test
    @DisplayName("company member 실패 test")
    void findMember2() throws SQLException {
        //given
        CompanyMemberDto result = new CompanyMemberDto(new BigInteger("1")
            , "loginName1", "password", "name", 0
            , new Date(System.currentTimeMillis()));

        repository.save(connection, result);
        //when
        //then
        assertThatThrownBy(() ->
            service.findMember("2"))
            .isInstanceOf(NonExistentMemberIdException.class);
    }
}