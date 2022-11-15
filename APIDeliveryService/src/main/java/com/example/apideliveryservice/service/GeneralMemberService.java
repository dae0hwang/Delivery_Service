package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.GeneralMemberDto;
import com.example.apideliveryservice.exception.DeliveryServiceException;
import com.example.apideliveryservice.repository.GeneralMemberRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralMemberService {

    private final GeneralMemberRepository generalMemberRepository;

    /**
     * @param loginName, password, name
     * @throws SQLException
     * @throws DeliveryServiceException-general member join fail due to DuplicatedLoginName
     */
    public void join(String loginName, String password, String name) throws SQLException {
        Connection connection = generalMemberRepository.connectJdbc();
        GeneralMemberDto generalMemberDto = getGeneralMember(loginName, password, name);
        try {
            connection.setAutoCommit(false);
            validateDuplicateLoginName(connection, generalMemberDto);
            generalMemberRepository.create(connection, generalMemberDto);
            connection.commit();
        } catch (DeliveryServiceException e) {
            connection.rollback();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private GeneralMemberDto getGeneralMember(String loginName, String password, String name) {
        GeneralMemberDto generalMemberDto = new GeneralMemberDto(null, loginName, password, name,
            false, new Timestamp(System.currentTimeMillis()));
        return generalMemberDto;
    }

    private void validateDuplicateLoginName(Connection connection
        , GeneralMemberDto generalMemberDto) throws SQLException {
        generalMemberRepository.findByLoginName(connection, generalMemberDto.getLoginName())
            .ifPresent(m -> {
                throw new DeliveryServiceException(
                    "general member join fail due to DuplicatedLoginName");
            });
    }

    /**
     * @return generalMemberList
     * @throws SQLException
     */
    public List<GeneralMemberDto> findAllMember() throws SQLException {
        try (Connection connection = generalMemberRepository.connectJdbc()) {
            List<GeneralMemberDto> allMember = generalMemberRepository.findAll(connection)
                .orElse(new ArrayList<>());
            return allMember;
        }
    }

    /**
     * @param id
     * @return findCompanyMember
     * @throws SQLException
     * @throws DeliveryServiceException-general member findById fail due to NonExistentMemberIdException
     */
    public GeneralMemberDto findById(String id) throws SQLException {
        try (Connection connection = generalMemberRepository.connectJdbc()) {
            GeneralMemberDto member = generalMemberRepository.findById(
                connection, Long.parseLong(id)).orElse(null);
            if (member == null) {
                throw new DeliveryServiceException(
                    "general member findById fail due to NonExistentMemberIdException");
            }
            return member;
        }
    }
}
