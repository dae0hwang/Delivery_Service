package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.CompanyMemberDto;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompanyMemberRepository {

    public void save(EntityManager em, CompanyMemberDto companyMember) {
        em.persist(companyMember);
    }

    public Optional<CompanyMemberDto> findByLoginName(EntityManager em, String loginName) {
        String jpql = "SELECT m FROM CompanyMemberDto m where m.loginName=:loginName";
        try {
            CompanyMemberDto findMember = em.createQuery(jpql, CompanyMemberDto.class)
                .setParameter("loginName", loginName).getSingleResult();
            return Optional.ofNullable(findMember);
        } catch (NoResultException e) {
            log.info("ex", e);
            return Optional.empty();
        }
    }

    public Optional<CompanyMemberDto> findById(EntityManager em, Long id) throws Exception {
        CompanyMemberDto findMember = em.find(CompanyMemberDto.class, id);
        return Optional.ofNullable(findMember);
    }

    public List<CompanyMemberDto> findAllMember(EntityManager em) throws Exception {
        String jpql = "SELECT c FROM CompanyMemberDto c";
        List<CompanyMemberDto> list = em.createQuery(jpql, CompanyMemberDto.class).getResultList();
        return list;
    }
}
