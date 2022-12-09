package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyFoodPriceEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    //criteria적용
    public BigDecimal findPriceByFoodId(EntityManager em, Long foodId){
        CompanyFoodEntity companyFoodEntity = em.find(CompanyFoodEntity.class, foodId);

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CompanyFoodPriceEntity> query = builder.createQuery(
            CompanyFoodPriceEntity.class);
        Root<CompanyFoodPriceEntity> root = query.from(CompanyFoodPriceEntity.class);

//        Predicate companyFoodEqual = builder.equal(root.get("companyFood"), companyFoodEntity);
//        Order updateDateDesc = builder.desc(root.get("updateDate"));

        query.select(root).where(builder.equal(root.get("companyFood"), companyFoodEntity))
            .orderBy(builder.desc(root.get("updateDate")));

        TypedQuery<CompanyFoodPriceEntity> typedQuery = em.createQuery(query);
        List<CompanyFoodPriceEntity> resultList = typedQuery.getResultList();
        CompanyFoodPriceEntity companyFoodPriceEntity = resultList.stream().findFirst().get();
        BigDecimal price = companyFoodPriceEntity.getPrice();
        return price;
    }

    public List<CompanyFoodEntity> findAllFood(EntityManager em, Long id) {
        String jpql = "SELECT f FROM CompanyFoodEntity f WHERE f.memberId=:memberId";
        List<CompanyFoodEntity> companyFoodEntities = em.createQuery(jpql, CompanyFoodEntity.class)
            .setParameter("memberId", id).getResultList();
        addListTempPrice(em, companyFoodEntities);
        return companyFoodEntities;
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
