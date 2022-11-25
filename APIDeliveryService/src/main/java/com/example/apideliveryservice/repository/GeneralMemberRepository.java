package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.GeneralMemberDto;
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

    public void create(EntityManager em, GeneralMemberDto generalMemberDto) {
        em.persist(generalMemberDto);

    }

    public Optional<GeneralMemberDto> findByLoginName(EntityManager em, String loginName) {
        String jpql = "SELECT m FROM GeneralMemberDto m where m.loginName=:loginName";
        try {
            GeneralMemberDto findMember = em.createQuery(jpql, GeneralMemberDto.class)
                .setParameter("loginName", loginName).getSingleResult();
            return Optional.ofNullable(findMember);
        } catch (NoResultException e) {
            log.info("ex", e);
            return Optional.empty();
        }
    }

    public List<GeneralMemberDto> findAll(EntityManager em) throws SQLException {
        String jpql = "SELECT m FROM GeneralMemberDto m";
        List<GeneralMemberDto> list = em.createQuery(jpql, GeneralMemberDto.class)
            .getResultList();
        return list;
    }


    public Optional<GeneralMemberDto> findById(EntityManager em, Long id) throws SQLException {
        GeneralMemberDto findMember = em.find(GeneralMemberDto.class, id);
        return Optional.ofNullable(findMember);
    }
}
