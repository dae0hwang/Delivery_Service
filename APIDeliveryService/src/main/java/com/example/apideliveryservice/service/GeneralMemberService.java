package com.example.apideliveryservice.service;

import static com.example.apideliveryservice.exception.GeneralMemberExceptionEnum.GENERAL_JOIN_DUPLICATED_LOGIN_NAME;

import com.example.apideliveryservice.dto.GeneralMemberDto;
import com.example.apideliveryservice.entity.GeneralMemberEntity;
import com.example.apideliveryservice.exception.GeneralMemberException;
import com.example.apideliveryservice.repository.GeneralMemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GeneralMemberService {

    private final GeneralMemberRepository generalMemberRepository;
    @Transactional
    public void join(String loginName, String password, String name){
        validateDuplicateLoginName(loginName);
        generalMemberRepository.save(new GeneralMemberEntity(loginName, password, name, false));
    }

    @Transactional(readOnly = true)
    public List<GeneralMemberDto> findAllMember() {
        return changeAllMemberEntityToDto(generalMemberRepository.findAll());
    }

    @Transactional(readOnly = true)
    public GeneralMemberDto findById(Long id) {
        GeneralMemberEntity findGeneralMemberEntity = generalMemberRepository.findById(id)
            .orElseThrow();
        return changeMemberEntityToDto(findGeneralMemberEntity);
    }

    private GeneralMemberDto changeMemberEntityToDto(GeneralMemberEntity memberEntity) {
        return new GeneralMemberDto(memberEntity.getId(),
            memberEntity.getLoginName(), memberEntity.getName(), memberEntity.getRegistrationDate());
    }

    private List<GeneralMemberDto> changeAllMemberEntityToDto(List<GeneralMemberEntity> allMemberEntity) {
        return allMemberEntity.stream().map(
            m -> new GeneralMemberDto(m.getId(), m.getLoginName(), m.getName(),
                m.getRegistrationDate())).collect(Collectors.toList());
    }

    private void validateDuplicateLoginName(String loginName) {
        generalMemberRepository.findByLoginName(loginName)
            .ifPresent(m -> {
                throw new GeneralMemberException(
                    GENERAL_JOIN_DUPLICATED_LOGIN_NAME.getErrormessage());
            });
    }
}
