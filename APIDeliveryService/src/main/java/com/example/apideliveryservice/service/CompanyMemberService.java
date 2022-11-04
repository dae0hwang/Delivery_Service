package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.exception.BlackException;
import com.example.apideliveryservice.exception.DuplicatedLoginNameException;
import com.example.apideliveryservice.exception.NonExistentMemberIdException;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyMemberService {

    private final CompanyMemberRepository companyMemberRepository;

    /**
     * @param companyMemberDto
     * @throws SQLException
     * @throws DuplicatedLoginNameException
     * @throws BlackException
     */
    public void join(CompanyMemberDto companyMemberDto) throws SQLException {
        Connection connection = companyMemberRepository.connectJdbc();
        try {
            connection.setAutoCommit(false);
            checkBlackRequestInput(companyMemberDto);
            validateDuplicateLoginName(connection, companyMemberDto);
            companyMemberRepository.save(connection, companyMemberDto);
            connection.commit();
        } catch (DuplicatedLoginNameException e) {
            connection.rollback();
            throw e;
        } catch (BlackException e) {
            connection.rollback();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void validateDuplicateLoginName(Connection connection
        , CompanyMemberDto companyMemberDto) throws SQLException {
        companyMemberRepository.findByLoginName(connection, companyMemberDto.getLoginName())
            .ifPresent(m -> {
                throw new DuplicatedLoginNameException();
            });
    }

    private void checkBlackRequestInput(CompanyMemberDto companyMemberDto) {
        if (companyMemberDto.getLoginName() == "" || companyMemberDto.getPassword() == ""
            || companyMemberDto.getName() == "") {
            throw new BlackException();
        }
    }

    /**
     *
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

    public CompanyMemberDto findMember(String id) throws SQLException {
        try (Connection connection = companyMemberRepository.connectJdbc()) {
            CompanyMemberDto member = companyMemberRepository.findById(
                connection, new BigInteger(id)).orElse(null);
            if (member == null) {
                throw new NonExistentMemberIdException();
            }
            return member;
        }
    }
}
