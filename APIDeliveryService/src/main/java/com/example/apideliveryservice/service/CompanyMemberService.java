package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import com.example.apideliveryservice.exception.DuplicatedLoginNameException;
import com.example.apideliveryservice.exception.NonExistentMemberIdException;
import com.example.apideliveryservice.repository.CompanyMemberRepository;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyMemberService {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    private final CompanyMemberRepository companyMemberRepository;

    /**
     * @param loginName, password, name
     * @throws Exception
     * @throws DuplicatedLoginNameException
     */
    public void join(String loginName, String password, String name) throws Exception {
        CompanyMemberDto companyMemberDto = getCompanyMemberDto(loginName, password, name);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            validateDuplicateLoginName(em, companyMemberDto);
            companyMemberRepository.save(em, companyMemberDto);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }

    private CompanyMemberDto getCompanyMemberDto(String loginName, String password, String name) {
        CompanyMemberDto companyMemberDto = new CompanyMemberDto(null, loginName, password, name,
            false, new Timestamp(System.currentTimeMillis()));
        return companyMemberDto;
    }

    private void validateDuplicateLoginName(EntityManager em, CompanyMemberDto companyMemberDto) {
        companyMemberRepository.findByLoginName(em, companyMemberDto.getLoginName())
            .ifPresent(m -> {
                throw new DuplicatedLoginNameException();
            });
    }

    /**
     * @return companyMemberList
     * @throws Exception
     */
    public List<CompanyMemberDto> findAllMember() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<CompanyMemberDto> memberList = companyMemberRepository.findAllMember(em);
            tx.commit();
            return memberList;
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
     * @throws NonExistentMemberIdException
     */
    public CompanyMemberDto findMember(String id) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            CompanyMemberDto member = companyMemberRepository.findById(em,
                Long.valueOf(id)).orElse(null);
            if (member == null) {
                throw new NonExistentMemberIdException();
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
