package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.GeneralMemberEntity;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GeneralMemberRepository {

    public void create(EntityManager em, GeneralMemberEntity generalMemberDto) {
        em.persist(generalMemberDto);

    }

    public Optional<GeneralMemberEntity> findByLoginName(EntityManager em, String loginName) {
        String jpql = "SELECT m FROM GeneralMemberEntity m where m.loginName=:loginName";
        try {
            GeneralMemberEntity findMember = em.createQuery(jpql, GeneralMemberEntity.class)
                .setParameter("loginName", loginName).getSingleResult();
            return Optional.ofNullable(findMember);
        } catch (NoResultException e) {
            log.info("ex", e);
            return Optional.empty();
        }
    }

    public List<GeneralMemberEntity> findAll(EntityManager em) throws SQLException {
        String jpql = "SELECT m FROM GeneralMemberEntity m";
        List<GeneralMemberEntity> list = em.createQuery(jpql, GeneralMemberEntity.class)
            .getResultList();
        return list;
    }


    public Optional<GeneralMemberEntity> findById(EntityManager em, Long id) throws SQLException {
        GeneralMemberEntity findMember = em.find(GeneralMemberEntity.class, id);
        return Optional.ofNullable(findMember);
    }
}
