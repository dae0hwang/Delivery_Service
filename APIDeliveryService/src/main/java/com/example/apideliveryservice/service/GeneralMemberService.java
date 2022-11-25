package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.GeneralMemberDto;
import com.example.apideliveryservice.exception.DeliveryServiceException;
import com.example.apideliveryservice.exception.ExceptionMessage;
import com.example.apideliveryservice.repository.GeneralMemberRepository;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralMemberService {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    private final GeneralMemberRepository generalMemberRepository;

    /**
     * @param loginName, password, name
     * @throws Exception
     * @throws DeliveryServiceException-general member join fail due to DuplicatedLoginName
     */
    public void join(String loginName, String password, String name) throws Exception {
        GeneralMemberDto generalMemberDto = getGeneralMember(loginName, password, name);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            validateDuplicateLoginName(em, generalMemberDto);
            generalMemberRepository.create(em, generalMemberDto);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }

    private GeneralMemberDto getGeneralMember(String loginName, String password, String name) {
        GeneralMemberDto generalMemberDto = new GeneralMemberDto(null, loginName, password, name,
            false, new Timestamp(System.currentTimeMillis()));
        return generalMemberDto;
    }

    private void validateDuplicateLoginName(EntityManager em, GeneralMemberDto generalMemberDto) {
        generalMemberRepository.findByLoginName(em, generalMemberDto.getLoginName())
            .ifPresent(m -> {
                throw new DeliveryServiceException(
                    ExceptionMessage.DeliveryExceptionDuplicatedName);
            });
    }

    /**
     * @return generalMemberList
     * @throws Exception
     */
    public List<GeneralMemberDto> findAllMember() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<GeneralMemberDto> allMember = generalMemberRepository.findAll(em);
            tx.commit();
            return allMember;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }

    /**
     * @param id
     * @return findCompanyMember
     * @throws Exception
     * @throws DeliveryServiceException-general member findById fail due to
     *                                          NonExistentMemberIdException
     */
    public GeneralMemberDto findById(String id) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            GeneralMemberDto member = generalMemberRepository.findById(em, Long.parseLong(id))
                .orElse(null);
            if (member == null) {
                throw new DeliveryServiceException(
                    ExceptionMessage.DeliveryExceptionNonExistentMemberId);
            }
            tx.commit();
            return member;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }
}
