package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.exception.DuplicatedLoginNameException;
import com.example.apideliveryservice.exception.NonExistentMemberIdException;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyMemberService {

    private final CompanyMemberRepository companyMemberRepository;

    /**
     * @param loginName, password, name
     * @throws SQLException
     * @throws DuplicatedLoginNameException
     */
    public void join(String loginName, String password, String name) throws SQLException {
        Connection connection = companyMemberRepository.connectJdbc();
        CompanyMemberDto companyMemberDto = getCompanyMemberDto(loginName, password, name);
        try {
            connection.setAutoCommit(false);
            validateDuplicateLoginName(connection, companyMemberDto);
            companyMemberRepository.save(connection, companyMemberDto);
            connection.commit();
        } catch (DuplicatedLoginNameException e) {
            connection.rollback();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private CompanyMemberDto getCompanyMemberDto(String loginName, String password, String name) {
        CompanyMemberDto companyMemberDto = new CompanyMemberDto(null, loginName, password, name,
            false, new Timestamp(System.currentTimeMillis()));
        return companyMemberDto;
    }

    private void validateDuplicateLoginName(Connection connection
        , CompanyMemberDto companyMemberDto) throws SQLException {
        companyMemberRepository.findByLoginName(connection, companyMemberDto.getLoginName())
            .ifPresent(m -> {
                throw new DuplicatedLoginNameException();
            });
    }

    /**
     * @return companyMemberList
     * @throws SQLException
     */
    public List<CompanyMemberDto> findAllMember() throws SQLException {
        try (Connection connection = companyMemberRepository.connectJdbc()) {
            List<CompanyMemberDto> allMember = companyMemberRepository.findAllMember(
                connection).orElse(new ArrayList<>());
            return allMember;
        }
    }

    /**
     * @param id
     * @return findCompanyMember
     * @throws SQLException
     * @throws NonExistentMemberIdException
     */
    public CompanyMemberDto findMember(String id) throws SQLException {
        try (Connection connection = companyMemberRepository.connectJdbc()) {
            CompanyMemberDto member = companyMemberRepository.findById(
                connection, Long.parseLong(id)).orElse(null);
            if (member == null) {
                throw new NonExistentMemberIdException();
            }
            return member;
        }
    }
}
