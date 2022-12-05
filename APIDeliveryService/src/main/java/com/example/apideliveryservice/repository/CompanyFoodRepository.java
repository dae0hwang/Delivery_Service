package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyFoodPriceEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompanyFoodRepository {

    public void add(EntityManager em, CompanyFoodEntity companyFoodEntity, BigDecimal price) {
        em.persist(companyFoodEntity);
        CompanyFoodPriceEntity companyFoodPriceEntity = new CompanyFoodPriceEntity(null, companyFoodEntity,
            price, new Timestamp(System.currentTimeMillis()));
        em.persist(companyFoodPriceEntity);
    }

    public Optional<CompanyFoodEntity> findByNameAndMemberId(EntityManager em, Long memberId,
        String findName) {
        String jpql = "SELECT f FROM CompanyFoodEntity f WHERE f.name=:name AND f.memberId=:memberId";
        try {
            CompanyFoodEntity findCompanyFood = em.createQuery(jpql, CompanyFoodEntity.class)
                .setParameter("name", findName).setParameter("memberId", memberId)
                .getSingleResult();
            return Optional.ofNullable(findCompanyFood);
        } catch (NoResultException e) {
            log.info("ex", e);
            return Optional.empty();
        }
    }

    public BigDecimal findPriceByFoodId(EntityManager em, Long foodId) {
        CompanyFoodEntity companyFoodEntity = em.find(CompanyFoodEntity.class, foodId);

        String jpql = "SELECT f FROM CompanyFoodPriceEntity f WHERE f.companyFood=:companyFood "
            + "order by f.updateDate desc";
        try {
            CompanyFoodPriceEntity companyFoodPriceEntity = em.createQuery(jpql,
                    CompanyFoodPriceEntity.class).setParameter("companyFood", companyFoodEntity)
                .setMaxResults(1)
                .getSingleResult();
            BigDecimal findPrice = companyFoodPriceEntity.getPrice();
            return findPrice;
        } catch (NoResultException e) {
            log.info("ex", e);
            throw e;
        }
    }

    public List<CompanyFoodEntity> findAllFood(EntityManager em, Long id) {
        List<CompanyFoodEntity> companyFoodEntities = new ArrayList<>();
        try {
            String jpql = "SELECT f FROM CompanyFoodEntity f WHERE f.memberId=:memberId";
            companyFoodEntities = em.createQuery(jpql, CompanyFoodEntity.class)
                .setParameter("memberId", id).getResultList();
            addListTempPrice(em, companyFoodEntities);
            return companyFoodEntities;
        } catch (NoResultException e) {
            log.info("ex", e);
            return companyFoodEntities;
        }
    }

    private void addListTempPrice(EntityManager em, List<CompanyFoodEntity> companyFoodEntities) {
        companyFoodEntities.replaceAll(companyFoodEntity -> {
            String jpql = "select p from CompanyFoodPriceEntity p "
                + "where p.companyFood=:companyFood order by p.updateDate desc";
            List<CompanyFoodPriceEntity> companyFoodPriceEntities = em.createQuery(jpql,
                    CompanyFoodPriceEntity.class).setParameter("companyFood", companyFoodEntity)
                .getResultList();
            CompanyFoodPriceEntity companyFoodPriceEntity = companyFoodPriceEntities.stream().findFirst()
                .get();
            return new CompanyFoodEntity(companyFoodEntity.getId(), companyFoodEntity.getMemberId(),
                companyFoodEntity.getName(), companyFoodEntity.getRegistrationDate(),
                companyFoodPriceEntity.getPrice());
        });
    }

    public Optional<CompanyFoodEntity> findById(EntityManager em, Long id) {
        CompanyFoodEntity companyFoodEntity = em.find(CompanyFoodEntity.class, id);
        if (companyFoodEntity == null) {
            return Optional.empty();
        }
        CompanyFoodEntity companyFoodEntityWithTempPrice = addTempPrice(em, companyFoodEntity);
        return Optional.ofNullable(companyFoodEntityWithTempPrice);
    }

    private CompanyFoodEntity addTempPrice(EntityManager em, CompanyFoodEntity companyFoodDto) {
        String foodPriceDtoJpql = "select p from CompanyFoodPriceEntity p "
            + "where p.companyFood=:companyFood order by p.updateDate desc";
        List<CompanyFoodPriceEntity> companyFoodPriceDtoList = em.createQuery(foodPriceDtoJpql,
            CompanyFoodPriceEntity.class).setParameter("companyFood", companyFoodDto).getResultList();
        CompanyFoodPriceEntity foodPriceDto = companyFoodPriceDtoList.stream().findFirst()
            .get();
        return new CompanyFoodEntity(companyFoodDto.getId(), companyFoodDto.getMemberId(),
            companyFoodDto.getName(), companyFoodDto.getRegistrationDate(),
            foodPriceDto.getPrice());
    }

    public void updatePrice(EntityManager em, Long foodId, BigDecimal price) {
        CompanyFoodEntity companyFoodDto = em.find(CompanyFoodEntity.class, foodId);
        CompanyFoodPriceEntity companyFoodPriceDto = new CompanyFoodPriceEntity(null, companyFoodDto,
            price, new Timestamp(System.currentTimeMillis()));

        em.persist(companyFoodPriceDto);
    }
}
