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
        CompanyMemberEntity companyMemberDto = getCompanyMemberDto(loginName, password, name);
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

    private CompanyMemberEntity getCompanyMemberDto(String loginName, String password, String name) {
        CompanyMemberEntity companyMemberDto = new CompanyMemberEntity(null, loginName, password, name,
            false, new Timestamp(System.currentTimeMillis()));
        return companyMemberDto;
    }

    private void validateDuplicateLoginName(EntityManager em, CompanyMemberEntity companyMemberDto) {
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
            List<CompanyMemberEntity> memberEntityList = companyMemberRepository.findAllMember(em);
            tx.commit();
            List<CompanyMemberDto> memberDtoList = changeMemberEntityListToDtoList(
                memberEntityList);
            return memberDtoList;
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
            CompanyMemberEntity memberEntity = companyMemberRepository.findById(em,
                Long.valueOf(id)).orElse(null);
            if (memberEntity == null) {
                throw new NonExistentMemberIdException();
            }
            tx.commit();
            CompanyMemberDto memberDto = changeMemberEntityToDto(memberEntity);
            return memberDto;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
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
