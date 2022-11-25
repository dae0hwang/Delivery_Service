package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.GeneralMemberDto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("jpa-h2")
@Slf4j
class GeneralMemberRepositoryTest {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    @Autowired
    GeneralMemberRepository repository;
    @Autowired
    RepositoryResetHelper resetHelper;
    Connection connection;
    EntityManagerFactory emf;
    EntityManager em;
    EntityTransaction tx;

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL", "sa", "");
        resetHelper.ifExistDeleteGeneralMembers(connection);
        resetHelper.createGeneralMembersTable(connection);

        emf = Persistence.createEntityManagerFactory(persistenceName);
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    void afterEach() {
        tx.rollback();
    }

    @Test
    @DisplayName("generalMember create findByLoginId 동시 Test")
    void create() throws SQLException {
        //given
        GeneralMemberDto generalMemberDto = new GeneralMemberDto(null, "loginName", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        //when
        repository.create(em, generalMemberDto);
        GeneralMemberDto findMember = repository.findByLoginName(em, "loginName")
            .orElse(null);
        //then
        assertThat(findMember.toString()).isEqualTo(generalMemberDto.toString());
    }

    @Test
    void findAllMember() throws SQLException {
        //given
        GeneralMemberDto companyMemberDto1 = new GeneralMemberDto(null, "loginName", "password", "name"
            , false, new Timestamp(System.currentTimeMillis()));
        GeneralMemberDto companyMemberDto2 = new GeneralMemberDto(null, "loginName2", "password", "name"
            , false, new Timestamp(System.currentTimeMillis()));
        repository.create(em, companyMemberDto1);
        repository.create(em, companyMemberDto2);
        List<GeneralMemberDto> resultLIst = new ArrayList<>();
        resultLIst.add(companyMemberDto1);
        resultLIst.add(companyMemberDto2);
        //when
        List<GeneralMemberDto>allMember = repository.findAll(em);
        //then
        assertThat(allMember).isEqualTo(resultLIst);
    }

    @Test
    @DisplayName("Id가 존재했을 때 찾기")
    void findById1() throws SQLException {
        //given
        GeneralMemberDto companyMemberDto = new GeneralMemberDto(null, "loginName", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        //when
        repository.create(em, companyMemberDto);
        Optional<GeneralMemberDto> findMember = repository.findById(em,1l);
        GeneralMemberDto findMemberDto = findMember.get();
        //then
        assertThat(findMemberDto).isEqualTo(companyMemberDto);
    }

    @Test
    @DisplayName("Id가 존재하지 않을 때 찾기")
    void findById2() throws SQLException {
        //when
        Optional<GeneralMemberDto> findMember = repository.findById(em, 1l);
        GeneralMemberDto findMemberDto = findMember.orElse(null);
        //then
        assertThat(findMemberDto).isNull();
    }
}