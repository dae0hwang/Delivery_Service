//package com.example.apideliveryservice.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import com.example.apideliveryservice.RepositoryResetHelper;
//import com.example.apideliveryservice.dto.GeneralMemberDto;
//import com.example.apideliveryservice.entity.GeneralMemberEntity;
//import com.example.apideliveryservice.exception.DeliveryServiceException;
//import com.example.apideliveryservice.repository.GeneralMemberRepository;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Persistence;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//@SpringBootTest
//@ActiveProfiles("jpa-h2")
//class GeneralMemberServiceTest {
//
//    @Value("${persistenceName:@null}")
//    private String persistenceName;
//    @Autowired
//    GeneralMemberRepository repository;
//    @Autowired
//    GeneralMemberService service;
//    @Autowired
//    RepositoryResetHelper resetHelper;
//    Connection connection;
//    EntityManagerFactory emf;
//    EntityManager em;
//    EntityTransaction tx;
//
//    @BeforeEach
//    void beforeEach() throws SQLException {
//        connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL", "sa", "");
//        resetHelper.ifExistDeleteGeneralMembers(connection);
//        resetHelper.createGeneralMembersTable(connection);
//
//        emf = Persistence.createEntityManagerFactory(persistenceName);
//        em = emf.createEntityManager();
//        tx = em.getTransaction();
//    }
//
//    @Test
//    @DisplayName("회원 가입 성공 Test")
//    void join1() throws Exception {
//        //given
//
//        //when
//        service.join("loginName", "password", "name");
//        GeneralMemberEntity findMember = repository.findByLoginName(em, "loginName")
//            .orElse(null);
//        //then
//        assertThat(findMember).isNotNull();
//    }
//
//    @Test
//    @DisplayName("회원 가입 실패 중복된 loginName Test")
//    void join2() throws SQLException {
//        //given
//        tx.begin();
//        GeneralMemberEntity firstSaveMember = new GeneralMemberEntity(null, "loginName", "password",
//            "name", false, new Timestamp(System.currentTimeMillis()));
//        repository.create(em, firstSaveMember);
//        tx.commit();
//        //when
//        //then
//        assertThatThrownBy(() ->
//            service.join("loginName", "password2", "name"))
//            .isInstanceOf(DeliveryServiceException.class);
//    }
//
//    @Test
//    @DisplayName("모든 general member 찾기 test")
//    void findAllMember() throws Exception {
//        //given
//        tx.begin();
//        GeneralMemberEntity companyMemberDto1 = new GeneralMemberEntity(null, "loginName1", "password",
//            "name", false, new Timestamp(System.currentTimeMillis()));
//        GeneralMemberEntity companyMemberDto2 = new GeneralMemberEntity(null, "loginName2", "password",
//            "name", false, new Timestamp(System.currentTimeMillis()));
//        repository.create(em, companyMemberDto1);
//        repository.create(em, companyMemberDto2);
//        tx.commit();
//
//        List<GeneralMemberDto> result = new ArrayList<>();
//        result.add(
//            new GeneralMemberDto(1l, "loginName1", "name", companyMemberDto1.getCreatedAt()));
//        result.add(
//            new GeneralMemberDto(2l, "loginName2", "name", companyMemberDto2.getCreatedAt()));
//
//        //when
//        List<GeneralMemberDto> expected = service.findAllMember();
//
//        //then
//        assertThat(expected).isEqualTo(result);
//    }
//    //여기할 차례
//
//    @Test
//    @DisplayName("id로멤버 찾기  성공 test")
//    void findById() throws Exception {
//        //given
//        GeneralMemberEntity result = new GeneralMemberEntity(null, "loginName1", "password", "name", false
//            , new Timestamp(System.currentTimeMillis()));
//        tx.begin();
//        repository.create(em, result);
//        tx.commit();
//
//        GeneralMemberDto actual = new GeneralMemberDto(1l, "loginName1", "name",
//            result.getCreatedAt());
//        //when
//        GeneralMemberDto findMember = service.findById("1");
//        //then
//        assertThat(actual).isEqualTo(findMember);
//    }
//
//    @Test
//    @DisplayName("id로 멤버 찾기 실패  존재하지 않는 id test")
//    void findMember2() throws SQLException {
//        //given
//        //when
//        //then
//        assertThatThrownBy(() ->
//            service.findById("2"))
//            .isInstanceOf(DeliveryServiceException.class);
//    }
//}