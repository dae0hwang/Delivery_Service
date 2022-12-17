package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
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
@ActiveProfiles("jpa-test")
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
    void save() {
        //given
        CompanyMemberEntity companyMemberDto = new CompanyMemberEntity(null, "loginName", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        //when
        repository.save(em, companyMemberDto);
        Optional<CompanyMemberEntity> findMember = repository.findByLoginName(em, "loginName");
        CompanyMemberEntity findMemberDto = findMember.get();
        //then
        assertThat(findMemberDto.toString()).isEqualTo(companyMemberDto.toString());
    }

    @Test
    @DisplayName("loginName 이 존재하지 않을 때")
    void findByLoginName2() throws Exception {
        //when
        Optional<CompanyMemberEntity> findMember = repository.findByLoginName(em, "loginName");
        CompanyMemberEntity findMemberDto = findMember.orElse(null);
        //then
        assertThat(findMemberDto).isNull();
    }

    @Test
    @DisplayName("Id가 존재했을 때 찾기")
    void findById1() throws Exception {
        //given
        CompanyMemberEntity companyMemberDto = new CompanyMemberEntity(null, "loginName", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        //when
        repository.save(em, companyMemberDto);
        Optional<CompanyMemberEntity> findMember = repository.findById(em, 1l);
        CompanyMemberEntity findMemberDto = findMember.get();
        //then
        assertThat(findMemberDto).isEqualTo(companyMemberDto);
    }

    @Test
    @DisplayName("Id가 존재하지 않을 때 찾기")
    void findById2() throws Exception {
        //when
        Optional<CompanyMemberEntity> findMember = repository.findById(em, 1l);
        CompanyMemberEntity findMemberDto = findMember.orElse(null);
        //then
        assertThat(findMemberDto).isNull();
    }

    @Test
    void findAllMember() throws Exception {
        //given
        CompanyMemberEntity companyMemberDto1 = new CompanyMemberEntity(null, "loginName", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        CompanyMemberEntity companyMemberDto2 = new CompanyMemberEntity(null, "loginName2", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        repository.save(em, companyMemberDto1);
        repository.save(em, companyMemberDto2);
        List<CompanyMemberEntity> resultLIst = new ArrayList<>();
        resultLIst.add(companyMemberDto1);
        resultLIst.add(companyMemberDto2);
        //when
        List<CompanyMemberEntity> allMember = repository.findAllMember(em);
        //then
        assertThat(resultLIst).isEqualTo(allMember);
    }
}