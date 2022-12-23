package com.example.apideliveryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.dto.RequestCompanyMember;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import com.example.apideliveryservice.exception.DuplicatedLoginNameException;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CompanyMemberServiceTest {

    @Autowired
    CompanyMemberRepository companyMemberRepository;
    @Autowired
    CompanyMemberService companyMemberService;


    @Test
    @DisplayName("기업 멤버 가입 성공 테스트")
    void joinTest1() {
        //given
        RequestCompanyMember requestCompanyMember = new RequestCompanyMember("loginName",
            "password", "name");
        //when
        companyMemberService.join(requestCompanyMember.getLoginName(),
            requestCompanyMember.getPassword(), requestCompanyMember.getName());
        //then
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findAll().get(0);
        assertThat(findCompanyMember).isNotNull();
    }

    @Test
    @DisplayName("중복된 loginName 회원 가입 실패 테스트")
    void joinTest2() {
        //given
        companyMemberRepository.save(new CompanyMemberEntity("loginName", "password", "name", false,
            new Timestamp(System.currentTimeMillis())));
        //when
        //then
        assertThatThrownBy(() ->
            companyMemberService.join("loginName", "password2", "name"))
            .isInstanceOf(DuplicatedLoginNameException.class);
    }

    @Test
    @DisplayName("모든 company member 찾기 test")
    void findAllMember() throws Exception {
        //given
        List<CompanyMemberEntity> saveCompanyMemberList = Arrays.asList(
            new CompanyMemberEntity("loginName", "password", "name", false,
                new Timestamp(System.currentTimeMillis())),
            new CompanyMemberEntity("loginName2", "password2", "name2", false,
                new Timestamp(System.currentTimeMillis())));
        List<CompanyMemberEntity> savedCompanyMemberList = companyMemberRepository.saveAll(
            saveCompanyMemberList);

        List<CompanyMemberDto> actualList = savedCompanyMemberList.stream()
            .map(m -> new CompanyMemberDto(m.getId(), m.getName(), m.getCreatedAt())).collect(
                Collectors.toList());
        //when
        List<CompanyMemberDto> expectedList= companyMemberService.findAllMember();
        //then
        assertThat(expectedList).isEqualTo(actualList);
    }

    @Test
    @DisplayName("company member 찾기 성공과 실패 test")
    void findMember1() {
        //given
        CompanyMemberEntity saveCompanyMember = companyMemberRepository.save(
            new CompanyMemberEntity("loginName", "password", "name", false,
                new Timestamp(System.currentTimeMillis())));

        CompanyMemberDto actualResult = new CompanyMemberDto(saveCompanyMember.getId(),
            saveCompanyMember.getName(), saveCompanyMember.getCreatedAt());
        //when
        CompanyMemberDto findCompanyMemberDto = companyMemberService.findMember(
            saveCompanyMember.getId());
        //then
        assertThat(findCompanyMemberDto).isEqualTo(actualResult);
        assertThatThrownBy(
            () -> companyMemberService.findMember(saveCompanyMember.getId() + 1l)).isInstanceOf(
            NoSuchElementException.class);
    }
}