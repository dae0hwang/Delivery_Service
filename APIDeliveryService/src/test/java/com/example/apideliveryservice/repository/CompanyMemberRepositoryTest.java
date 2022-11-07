package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.CompanyMemberDto;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("db-h2")
class CompanyMemberRepositoryTest {

    @Autowired
    CompanyMemberRepository repository;
    @Autowired
    RepositoryResetHelper resetHelper;

    Connection connection;

    //Mockito.spy 사용하기.
    @BeforeEach
    void beforeEach() throws SQLException {
        repository = Mockito.spy(new CompanyMemberRepository());
        connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL", "sa", "");
        Mockito.doReturn(connection).when(repository).connectJdbc();

        resetHelper.ifExistDeleteCompanyMembers(connection);
        resetHelper.createCompanyMembersTable(connection);
    }

    @Test
    void connectJdbc() {
        //then
        assertThat(connection).isNotNull();
    }

    @Test
    @DisplayName("save and findByLonginName Test")
    void save() throws SQLException {
        //given
        CompanyMemberDto companyMemberDto = new CompanyMemberDto(new BigInteger("1")
            , "loginName", "password", "name", 0
            , new Date(System.currentTimeMillis()));
        //when
        repository.save(connection, companyMemberDto);
        Optional<CompanyMemberDto> findMember = repository.findByLoginName(connection
            , "loginName");
        CompanyMemberDto findMemberDto = findMember.get();
        //then
        assertThat(findMemberDto.toString()).isEqualTo(companyMemberDto.toString());
    }

    @Test
    @DisplayName("loginName 이 존재하지 않을 때")
    void findByLoginName2() throws SQLException {
        //when
        Optional<CompanyMemberDto> findMember = repository.findByLoginName(connection
            , "loginName");
        CompanyMemberDto findMemberDto = findMember.orElse(null);
        //then
        assertThat(findMemberDto).isNull();
    }

    @Test
    @DisplayName("Id가 존재했을 때 찾기")
    void findById1() throws SQLException {
        //given
        CompanyMemberDto companyMemberDto = new CompanyMemberDto(new BigInteger("1")
            , "loginName", "password", "name", 0
            , new Date(System.currentTimeMillis()));
        //when
        repository.save(connection, companyMemberDto);
        Optional<CompanyMemberDto> findMember = repository.findById(connection
            , new BigInteger("1"));
        CompanyMemberDto findMemberDto = findMember.get();
        //then
        assertThat(findMemberDto.toString()).isEqualTo(companyMemberDto.toString());
    }

    @Test
    @DisplayName("Id가 존재하지 않을 때 찾기")
    void findById2() throws SQLException {
        //when
        Optional<CompanyMemberDto> findMember = repository.findById(connection
            , new BigInteger("1"));
        CompanyMemberDto findMemberDto = findMember.orElse(null);
        //then
        assertThat(findMemberDto).isNull();
    }

    @Test
    void findAllMember() throws SQLException {
        //given
        CompanyMemberDto companyMemberDto1 = new CompanyMemberDto(new BigInteger("1")
            , "loginName1", "password", "name", 0
            , new Date(System.currentTimeMillis()));
        CompanyMemberDto companyMemberDto2 = new CompanyMemberDto(new BigInteger("2")
            , "loginName2", "password", "name", 0
            , new Date(System.currentTimeMillis()));
        repository.save(connection, companyMemberDto1);
        repository.save(connection, companyMemberDto2);
        List<CompanyMemberDto> resultLIst = new ArrayList<>();
        resultLIst.add(companyMemberDto1);
        resultLIst.add(companyMemberDto2);
        //when
        Optional<List<CompanyMemberDto>> allMember = repository.findAllMember(connection);
        List<CompanyMemberDto> expectedList = allMember.orElse(null);
        //then
        assertThat(expectedList.toString()).isEqualTo(resultLIst.toString());
    }
}