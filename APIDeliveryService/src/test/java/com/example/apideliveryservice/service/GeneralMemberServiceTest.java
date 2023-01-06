package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.apideliveryservice.dto.GeneralMemberDto;
import com.example.apideliveryservice.dto.RequestGeneralMember;
import com.example.apideliveryservice.entity.GeneralMemberEntity;
import com.example.apideliveryservice.exception.GeneralMemberException;
import com.example.apideliveryservice.repository.GeneralMemberRepository;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class GeneralMemberServiceTest {

    @Autowired
    GeneralMemberRepository generalMemberRepository;
    @Autowired
    GeneralMemberService generalMemberService;

    @Test
    @DisplayName("일반 멤버 가입 성공 테스트")
    void joinTest1() {
        //given
        RequestGeneralMember requestGeneralMember = new RequestGeneralMember("loginName",
            "password", "name");
        //when
        generalMemberService.join(requestGeneralMember.getLoginName(),
            requestGeneralMember.getPassword(), requestGeneralMember.getName());
        //then
        GeneralMemberEntity findGeneralMember = generalMemberRepository.findAll().get(0);
        assertThat(findGeneralMember).isNotNull();
    }

    @Test
    @DisplayName("중복된 loginName 회원 가입 실패 테스트")
    void joinTest2() {
        //given
        generalMemberRepository.save(
            new GeneralMemberEntity("loginName", "password", "name", false));
        //when
        //then
        assertThatThrownBy(() ->
            generalMemberService.join("loginName", "password2", "name"))
            .isInstanceOf(GeneralMemberException.class);
    }

    @Test
    @DisplayName("모든 general member 찾기 test")
    void findAllMember() {
        //given
        List<GeneralMemberEntity> saveCompanyMemberList = Arrays.asList(
            new GeneralMemberEntity("loginName", "password", "name", false),
            new GeneralMemberEntity("loginName2", "password2", "name2", false));

        List<GeneralMemberEntity> saveGeneralMemberList = generalMemberRepository.saveAll(
            saveCompanyMemberList);

        //when
        List<GeneralMemberDto> expectedList= generalMemberService.findAllMember();
        //then
        assertThat(expectedList).extracting("loginName").containsExactly("loginName", "loginName2");
    }

    @Test
    @DisplayName("company member 찾기 성공과 실패 test")
    void findMember1() {
        //given
        GeneralMemberEntity saveGeneralMember = generalMemberRepository.save(
            new GeneralMemberEntity("loginName", "password", "name", false));

        //when
        GeneralMemberDto findCompanyMemberDto = generalMemberService.findById(
            saveGeneralMember.getId());
        //then
        assertThat(findCompanyMemberDto.getLoginName()).isEqualTo(saveGeneralMember.getLoginName());
        assertThatThrownBy(
            () -> generalMemberService.findById(saveGeneralMember.getId() + 1L)).isInstanceOf(
            NoSuchElementException.class);
    }
}