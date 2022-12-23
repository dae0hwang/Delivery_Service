package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.apideliveryservice.entity.CompanyMemberEntity;
import java.sql.Timestamp;
import java.util.NoSuchElementException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
//@Transactional
@Commit
@ActiveProfiles("test")
@Slf4j
class CompanyMemberRepositoryTest {

    @Autowired
    CompanyMemberRepository companyMemberRepository;


    @Test
    @DisplayName("save and findByLonginName Test")
    void save() throws Exception {
    }

    @Test
    @DisplayName("loginName 으로 find Test")
    void findByLoginName() throws Exception {
        //given
        companyMemberRepository.save(new CompanyMemberEntity("loginName", "password", "name", false,
            new Timestamp(System.currentTimeMillis())));
        //when
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findByLoginName(
            "loginName").orElseThrow();
        //then
        assertThat(findCompanyMember).isNotNull();
        assertThatThrownBy(() -> companyMemberRepository.findByLoginName("differentLoginName")
            .orElseThrow()).isInstanceOf(NoSuchElementException.class);
    }

//    @Test
//    @DisplayName("Id가 존재했을 때 찾기")
//    void findById1() throws Exception {
//        //given
//        CompanyMemberEntity companyMemberDto = new CompanyMemberEntity(null, "loginName", "password",
//            "name", false, new Timestamp(System.currentTimeMillis()));
//        //when
//        repository.save(em, companyMemberDto);
//        Optional<CompanyMemberEntity> findMember = repository.findById(em, 1l);
//        CompanyMemberEntity findMemberDto = findMember.get();
//        //then
//        assertThat(findMemberDto).isEqualTo(companyMemberDto);
//    }
//
//    @Test
//    @DisplayName("Id가 존재하지 않을 때 찾기")
//    void findById2() throws Exception {
//        //when
//        Optional<CompanyMemberEntity> findMember = repository.findById(em, 1l);
//        CompanyMemberEntity findMemberDto = findMember.orElse(null);
//        //then
//        assertThat(findMemberDto).isNull();
//    }
//
//    @Test
//    void findAllMember() throws Exception {
//        //given
//        CompanyMemberEntity companyMemberDto1 = new CompanyMemberEntity(null, "loginName", "password",
//            "name", false, new Timestamp(System.currentTimeMillis()));
//        CompanyMemberEntity companyMemberDto2 = new CompanyMemberEntity(null, "loginName2", "password",
//            "name", false, new Timestamp(System.currentTimeMillis()));
//        repository.save(em, companyMemberDto1);
//        repository.save(em, companyMemberDto2);
//        List<CompanyMemberEntity> resultLIst = new ArrayList<>();
//        resultLIst.add(companyMemberDto1);
//        resultLIst.add(companyMemberDto2);
//        //when
//        List<CompanyMemberEntity> allMember = repository.findAllMember(em);
//        //then
//        assertThat(resultLIst).isEqualTo(allMember);
//    }
}