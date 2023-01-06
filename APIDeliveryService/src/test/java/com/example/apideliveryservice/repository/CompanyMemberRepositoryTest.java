package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.apideliveryservice.entity.CompanyMemberEntity;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Commit
@ActiveProfiles("test")
@Slf4j
class CompanyMemberRepositoryTest {

    @Autowired
    CompanyMemberRepository companyMemberRepository;

    @Test
    @DisplayName("loginName 으로 find Test")
    void findByLoginName(){
        //given
        companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password","name",false));

        //when
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findByLoginName("loginName")
            .orElseThrow();
        //then
        assertThat(findCompanyMember).isNotNull();
        assertThatThrownBy(() -> companyMemberRepository.findByLoginName("differentLoginName")
            .orElseThrow()).isInstanceOf(NoSuchElementException.class);
    }
}