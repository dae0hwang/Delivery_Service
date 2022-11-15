package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.GeneralMemberDto;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Test
    void findAllMember() throws SQLException {
        //given
        GeneralMemberDto companyMemberDto1 = new GeneralMemberDto(1l, "loginName", "password", "name"
            , false, new Timestamp(System.currentTimeMillis()));
        GeneralMemberDto companyMemberDto2 = new GeneralMemberDto(2l, "loginName2", "password", "name"
            , false, new Timestamp(System.currentTimeMillis()));
        repository.create(connection, companyMemberDto1);
        repository.create(connection, companyMemberDto2);
        List<GeneralMemberDto> resultLIst = new ArrayList<>();
        resultLIst.add(companyMemberDto1);
        resultLIst.add(companyMemberDto2);
        //when
        Optional<List<GeneralMemberDto>> allMember = repository.findAll(connection);
        List<GeneralMemberDto> expectedList = allMember.orElse(null);
        //then
        assertThat(expectedList.toString()).isEqualTo(resultLIst.toString());
    }

    @Test
    @DisplayName("Id가 존재했을 때 찾기")
    void findById1() throws SQLException {
        //given
        GeneralMemberDto companyMemberDto = new GeneralMemberDto(1l, "loginName", "password", "name"
            , false, new Timestamp(System.currentTimeMillis()));
        //when
        repository.create(connection, companyMemberDto);
        Optional<GeneralMemberDto> findMember = repository.findById(connection,1l);
        GeneralMemberDto findMemberDto = findMember.get();
        //then
        assertThat(findMemberDto).isEqualTo(companyMemberDto);
    }

    @Test
    @DisplayName("Id가 존재하지 않을 때 찾기")
    void findById2() throws SQLException {
        //when
        Optional<GeneralMemberDto> findMember = repository.findById(connection, 1l);
        GeneralMemberDto findMemberDto = findMember.orElse(null);
        //then
        assertThat(findMemberDto).isNull();
    }
}