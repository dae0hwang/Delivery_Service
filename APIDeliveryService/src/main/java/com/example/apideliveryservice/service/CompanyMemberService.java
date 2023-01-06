package com.example.apideliveryservice.service;

import static com.example.apideliveryservice.exception.CompanyMemberExceptionEnum.*;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import com.example.apideliveryservice.exception.CompanyMemberException;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyMemberService {

    private final CompanyMemberRepository companyMemberRepository;
    @Transactional
    public void join(String loginName, String password, String name){
        validateDuplicateLoginName(loginName);
        CompanyMemberEntity companyMemberEntity = getCompanyMemberEntity(loginName, password, name);
        companyMemberRepository.save(companyMemberEntity);
    }

    private CompanyMemberEntity getCompanyMemberEntity(String loginName, String password, String name) {
        return new CompanyMemberEntity(loginName, password, name,
            false);
    }

    private void validateDuplicateLoginName(String loginName) {
        companyMemberRepository.findByLoginName(loginName)
            .ifPresent(m -> {
                throw new CompanyMemberException(COMPANY_JOIN_DUPLICATED_LOGIN_NAME.getErrormessage());
            });
    }
    @Transactional(readOnly = true)
    public List<CompanyMemberDto> findAllMember(){
        List<CompanyMemberEntity> allCompanyMember = companyMemberRepository.findAll();
        return changeMemberEntityListToDtoList(
            allCompanyMember);
    }
    @Transactional(readOnly = true)
    public CompanyMemberDto findMember(Long id) {
        CompanyMemberEntity findCompanyMember = companyMemberRepository.findById(id)
            .orElseThrow();
        return changeMemberEntityToDto(findCompanyMember);
    }

    private CompanyMemberDto changeMemberEntityToDto(CompanyMemberEntity memberEntity) {
        return new CompanyMemberDto(memberEntity.getId(),
            memberEntity.getName(), memberEntity.getRegistrationDate());
    }

    private List<CompanyMemberDto> changeMemberEntityListToDtoList(
        List<CompanyMemberEntity> memberEntityList) {
        return memberEntityList.stream()
            .map(m -> new CompanyMemberDto(m.getId(), m.getName(), m.getRegistrationDate())).collect(Collectors.toList());
    }
}
