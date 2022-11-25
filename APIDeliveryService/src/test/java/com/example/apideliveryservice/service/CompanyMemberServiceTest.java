package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.*;

import com.example.apideliveryservice.RepositoryResetHelper;
import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.dto.RequestCompanyMemberDto;
import com.example.apideliveryservice.exception.DuplicatedLoginNameException;
import com.example.apideliveryservice.exception.NonExistentMemberIdException;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("jpa-h2")
class CompanyMemberServiceTest {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    @Autowired
    CompanyMemberRepository repository;
    @Autowired
    CompanyMemberService service;
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
    }

    @Test
    @DisplayName("정상 회원 가입 테스트")
    void joinTest1() throws Exception {
        //given
        RequestCompanyMemberDto requestCompanyMember = new RequestCompanyMemberDto("loginName",
            "password", "name");
        //when
        service.join(requestCompanyMember.getLoginName(), requestCompanyMember.getPassword(),
            requestCompanyMember.getName());
        CompanyMemberDto findMember = repository.findByLoginName(em, "loginName").orElse(null);
        //then
        assertThat(findMember).isNotNull();
    }

    @Test
    @DisplayName("중복된 loginName 회원 가입 실패 테스트")
    void joinTest2() {
        //given
        tx.begin();
        CompanyMemberDto companyMemberDto1 = new CompanyMemberDto(null, "loginName", "password1",
            "name1", false, new Timestamp(System.currentTimeMillis()));
        repository.save(em, companyMemberDto1);
        tx.commit();

        RequestCompanyMemberDto requestCompanyMember = new RequestCompanyMemberDto("loginName",
            "password2", "name2");
        //then
        assertThatThrownBy(() ->
            //when
            service.join(requestCompanyMember.getLoginName(), requestCompanyMember.getPassword(),
                requestCompanyMember.getName())).isInstanceOf(DuplicatedLoginNameException.class);
    }

    @Test
    @DisplayName("모든 company member 찾기 test")
    void findAllMember() throws Exception {
        //given
        CompanyMemberDto companyMemberDto1 = new CompanyMemberDto(null, "loginName1", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        CompanyMemberDto companyMemberDto2 = new CompanyMemberDto(null, "loginName2", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        CompanyMemberDto companyMemberDto3 = new CompanyMemberDto(null, "loginName3", "password",
            "name", false, new Timestamp(System.currentTimeMillis()));
        tx.begin();
        repository.save(em, companyMemberDto1);
        repository.save(em, companyMemberDto2);
        repository.save(em, companyMemberDto3);
        tx.commit();
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
    void findMember1() throws Exception {
        //given
        CompanyMemberDto result = new CompanyMemberDto(null, "loginName1", "password", "name",
            false, new Timestamp(System.currentTimeMillis()));
        tx.begin();
        repository.save(em, result);
        tx.commit();
        //when
        CompanyMemberDto expected = service.findMember("1");
        //then
        assertThat(expected.toString()).isEqualTo(result.toString());
    }

    @Test
    @DisplayName("company member 실패 test")
    void findMember2() throws SQLException {
        //given
        CompanyMemberDto result = new CompanyMemberDto(null, "loginName1", "password", "name",
            false, new Timestamp(System.currentTimeMillis()));
        tx.begin();
        repository.save(em, result);
        tx.commit();
        //when
        //then
        assertThatThrownBy(() -> service.findMember("2")).isInstanceOf(
            NonExistentMemberIdException.class);
    }
}