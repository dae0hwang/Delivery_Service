package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import com.example.apideliveryservice.exception.DuplicatedLoginNameException;
import com.example.apideliveryservice.exception.NonExistentMemberIdException;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompanyMemberService {

    private final CompanyMemberRepository companyMemberRepository;

    public void join(String loginName, String password, String name){
        validateDuplicateLoginName(loginName);
        CompanyMemberEntity companyMemberEntity = getCompanyMemberEntity(loginName, password, name);
        companyMemberRepository.save(companyMemberEntity);
    }

    private CompanyMemberEntity getCompanyMemberEntity(String loginName, String password, String name) {
        CompanyMemberEntity companyMemberEntity = new CompanyMemberEntity(loginName, password, name,
            false, new Timestamp(System.currentTimeMillis()));
        return companyMemberEntity;
    }

    private void validateDuplicateLoginName(String loginName) {
        companyMemberRepository.findByLoginName(loginName)
            .ifPresent(m -> {
                throw new DuplicatedLoginNameException();
            });
    }

    public List<CompanyMemberDto> findAllMember(){
        List<CompanyMemberEntity> allCompanyMember = companyMemberRepository.findAll();
        List<CompanyMemberDto> companyMemberDtoList = changeMemberEntityListToDtoList(
            allCompanyMember);
        return companyMemberDtoList;
    }

    public CompanyMemberDto findMember(Long id) {
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findById(id)
            .orElseThrow();
        return changeMemberEntityToDto(findCompanyMember);
    }

    private CompanyMemberDto changeMemberEntityToDto(CompanyMemberEntity memberEntity) {
        CompanyMemberDto memberDto = new CompanyMemberDto(memberEntity.getId(),
            memberEntity.getName(), memberEntity.getCreatedAt());
        return memberDto;
    }

    private List<CompanyMemberDto> changeMemberEntityListToDtoList(
        List<CompanyMemberEntity> memberEntityList) {
        List<CompanyMemberDto> memberDtoList = memberEntityList.stream()
            .map(m -> new CompanyMemberDto(m.getId(), m.getName(), m.getCreatedAt())).collect(Collectors.toList());
        return memberDtoList;
    }
}
