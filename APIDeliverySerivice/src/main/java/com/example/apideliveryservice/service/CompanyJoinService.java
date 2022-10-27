package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyJoinService {

    private final CompanyMemberRepository companyMemberRepository;

    public void join(CompanyMemberDto companyMemberDto) throws SQLException {
        Connection connection = companyMemberRepository.connectJdbc();
        try {
            connection.setAutoCommit(false);
            validateDuplicateLoginNameOrName(connection, companyMemberDto);
            companyMemberRepository.save(connection, companyMemberDto);
            connection.commit();
        } catch (DuplicatedNameException e) {
            connection.rollback();
            throw new DuplicatedNameException();
        } catch (DuplicatedLoginNameException e) {
            throw new DuplicatedLoginNameException();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void validateDuplicateLoginNameOrName(Connection connection
        , CompanyMemberDto companyMemberDto) {
        companyMemberRepository.findByLoginName(connection, companyMemberDto.getLoginName())
            .ifPresent(m -> {
                throw new DuplicatedLoginNameException();
            });
        companyMemberRepository.findByName(connection, companyMemberDto.getName())
            .ifPresent(m -> {
                throw new DuplicatedNameException();
            });
    }
}
