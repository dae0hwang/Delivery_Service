package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.CompanyMemberEntity;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompanyMemberRepository {

    public void save(EntityManager em, CompanyMemberEntity companyMember) {
        em.persist(companyMember);
    }

    public Optional<CompanyMemberEntity> findByLoginName(EntityManager em, String loginName) {
        String jpql = "SELECT m FROM CompanyMemberEntity m where m.loginName=:loginName";
        try {
            CompanyMemberEntity findMember = em.createQuery(jpql, CompanyMemberEntity.class)
                .setParameter("loginName", loginName).getSingleResult();
            return Optional.ofNullable(findMember);
        } catch (NoResultException e) {
            log.info("ex", e);
            return Optional.empty();
        }
    }

    public Optional<CompanyMemberEntity> findById(EntityManager em, Long id) throws Exception {
        CompanyMemberEntity findMember = em.find(CompanyMemberEntity.class, id);
        return Optional.ofNullable(findMember);
    }

    public List<CompanyMemberEntity> findAllMember(EntityManager em) throws Exception {
        String jpql = "SELECT c FROM CompanyMemberEntity c";
        List<CompanyMemberEntity> list = em.createQuery(jpql, CompanyMemberEntity.class).getResultList();
        return list;
    }
}
