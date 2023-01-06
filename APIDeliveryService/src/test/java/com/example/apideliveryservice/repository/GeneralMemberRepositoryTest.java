package com.example.apideliveryservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.apideliveryservice.entity.GeneralMemberEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Slf4j
class GeneralMemberRepositoryTest {
    @Autowired
    GeneralMemberRepository generalMemberRepository;

    @Test
    void findByLoginName() {
        //given
        GeneralMemberEntity saveGeneralMember = generalMemberRepository.save(
            new GeneralMemberEntity("loginName", "password", "name", false));
        //when
        GeneralMemberEntity findGeneralMember = generalMemberRepository.findByLoginName(
            "loginName").orElseThrow();
        //then
        assertThat(findGeneralMember).isEqualTo(saveGeneralMember);
    }
}