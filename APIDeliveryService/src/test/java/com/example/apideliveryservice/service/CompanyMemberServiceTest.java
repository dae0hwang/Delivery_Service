package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.*;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.dto.RequestCompanyMemberDto;
import com.example.apideliveryservice.exception.DuplicatedLoginNameException;
import com.example.apideliveryservice.exception.NonExistentMemberIdException;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
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
        RequestCompanyMemberDto requestCompanyMember = new RequestCompanyMemberDto("loginName",
            "password", "name");
        //when
        service.join(requestCompanyMember.getLoginName(), requestCompanyMember.getPassword(),
            requestCompanyMember.getName());
        CompanyMemberDto findMember
            = repository.findByLoginName(connection, "loginName").orElse(null);
        //then
        assertThat(findMember).isNotNull();
    }

    @Test
    @DisplayName("중복된 loginName 회원 가입 실패 테스트")
    void joinTest2() throws SQLException {
        //given
        CompanyMemberDto companyMemberDto1 = new CompanyMemberDto(1l, "loginName", "password1",
            "name1", false, new Timestamp(System.currentTimeMillis()));
        repository.save(connection, companyMemberDto1);

        RequestCompanyMemberDto requestCompanyMember = new RequestCompanyMemberDto("loginName",
            "password2", "name2");
        //then
        assertThatThrownBy(() ->
            //when
            service.join(requestCompanyMember.getLoginName(), requestCompanyMember.getPassword(),
                requestCompanyMember.getName()))
            .isInstanceOf(DuplicatedLoginNameException.class);
    }

    @Test
    @DisplayName("모든 company member 찾기 test")
    void findAllMember() throws SQLException {
        //given
        CompanyMemberDto companyMemberDto1 = new CompanyMemberDto(1l, "loginName1", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        CompanyMemberDto companyMemberDto2 = new CompanyMemberDto(2l, "loginName2", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        CompanyMemberDto companyMemberDto3 = new CompanyMemberDto(3l, "loginName3", "password"
            , "name", false, new Timestamp(System.currentTimeMillis()));
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
        CompanyMemberDto result = new CompanyMemberDto(1l, "loginName1", "password", "name", false
            , new Timestamp(System.currentTimeMillis()));

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
        CompanyMemberDto result = new CompanyMemberDto(1l, "loginName1", "password", "name", false
            , new Timestamp(System.currentTimeMillis()));

        repository.save(connection, result);
        //when
        //then
        assertThatThrownBy(() ->
            service.findMember("2"))
            .isInstanceOf(NonExistentMemberIdException.class);
    }
}