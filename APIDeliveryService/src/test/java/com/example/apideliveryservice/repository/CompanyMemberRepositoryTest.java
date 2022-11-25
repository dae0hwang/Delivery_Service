package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.CompanyMemberDto;
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
class CompanyMemberRepositoryTest {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    @Autowired
    CompanyMemberRepository repository;
    @Autowired
    RepositoryResetHelper resetHelper;
    Connection connection;
    EntityManagerFactory emf;
    EntityManager em;
    EntityTransaction tx;

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL", "sa", "");
        resetHelper.ifExistDeleteCompanyMembers(connection);
        resetHelper.createCompanyMembersTable(connection);

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
    @DisplayName("save and findByLonginName Test")
    void save() throws Exception {
        //given
        CompanyMemberDto companyMemberDto = new CompanyMemberDto(null, "loginName", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        //when
        repository.save(em, companyMemberDto);
        Optional<CompanyMemberDto> findMember = repository.findByLoginName(em, "loginName");
        CompanyMemberDto findMemberDto = findMember.get();
        //then
        assertThat(findMemberDto.toString()).isEqualTo(companyMemberDto.toString());
    }

    @Test
    @DisplayName("loginName 이 존재하지 않을 때")
    void findByLoginName2() throws Exception {
        //when
        Optional<CompanyMemberDto> findMember = repository.findByLoginName(em, "loginName");
        CompanyMemberDto findMemberDto = findMember.orElse(null);
        //then
        assertThat(findMemberDto).isNull();
    }

    @Test
    @DisplayName("Id가 존재했을 때 찾기")
    void findById1() throws Exception {
        //given
        CompanyMemberDto companyMemberDto = new CompanyMemberDto(null, "loginName", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        //when
        repository.save(em, companyMemberDto);
        Optional<CompanyMemberDto> findMember = repository.findById(em, 1l);
        CompanyMemberDto findMemberDto = findMember.get();
        //then
        assertThat(findMemberDto).isEqualTo(companyMemberDto);
    }

    @Test
    @DisplayName("Id가 존재하지 않을 때 찾기")
    void findById2() throws Exception {
        //when
        Optional<CompanyMemberDto> findMember = repository.findById(em, 1l);
        CompanyMemberDto findMemberDto = findMember.orElse(null);
        //then
        assertThat(findMemberDto).isNull();
    }

    @Test
    void findAllMember() throws Exception {
        //given
        CompanyMemberDto companyMemberDto1 = new CompanyMemberDto(null, "loginName", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        CompanyMemberDto companyMemberDto2 = new CompanyMemberDto(null, "loginName2", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        repository.save(em, companyMemberDto1);
        repository.save(em, companyMemberDto2);
        List<CompanyMemberDto> resultLIst = new ArrayList<>();
        resultLIst.add(companyMemberDto1);
        resultLIst.add(companyMemberDto2);
        //when
        List<CompanyMemberDto> allMember = repository.findAllMember(em);
        //then
        assertThat(resultLIst).isEqualTo(allMember);
    }
}