package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyJoinService {

    private final CompanyMemberRepository companyMemberRepository;

    public void join(CompanyMemberDto companyMemberDto) throws SQLException {
        validateDuplicateLoginNameOrName(companyMemberDto);
        companyMemberRepository.save(companyMemberDto);
    }

    private void validateDuplicateLoginNameOrName(CompanyMemberDto companyMemberDto) {
        companyMemberRepository.findByLoginName(companyMemberDto.getLoginName())
            .ifPresent(m -> {
                throw new DuplicatedLoginNameException();
            });
        companyMemberRepository.findByName(companyMemberDto.getName())
            .ifPresent(m -> {
                throw new DuplicatedNameException();
            });
    }
}
