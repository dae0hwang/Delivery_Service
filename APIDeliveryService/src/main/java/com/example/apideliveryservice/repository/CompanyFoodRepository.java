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

    public void add(EntityManager em, CompanyFoodEntity companyFoodDto, BigDecimal price) {
        em.persist(companyFoodDto);
        CompanyFoodPriceEntity companyFoodPriceDto = new CompanyFoodPriceEntity(null, companyFoodDto,
            price, new Timestamp(System.currentTimeMillis()));
        em.persist(companyFoodPriceDto);
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
        List<CompanyFoodEntity> foodDtoList = new ArrayList<>();
        try {
            String foodDtoJpql = "SELECT f FROM CompanyFoodEntity f WHERE f.memberId=:memberId";
            foodDtoList = em.createQuery(foodDtoJpql, CompanyFoodEntity.class)
                .setParameter("memberId", id).getResultList();
            addListTempPrice(em, foodDtoList);
            return foodDtoList;
        } catch (NoResultException e) {
            log.info("ex", e);
            return foodDtoList;
        }
    }

    private void addListTempPrice(EntityManager em, List<CompanyFoodEntity> foodDtoList) {
        foodDtoList.replaceAll(companyFoodDto -> {
            String foodPriceDtoJpql = "select p from CompanyFoodPriceEntity p "
                + "where p.companyFood=:companyFood order by p.updateDate desc";
            List<CompanyFoodPriceEntity> companyFoodPriceDtoList = em.createQuery(foodPriceDtoJpql, CompanyFoodPriceEntity.class)
                .setParameter("companyFood", companyFoodDto).getResultList();
            CompanyFoodPriceEntity foodPriceDto = companyFoodPriceDtoList.stream().findFirst()
                .get();
            return new CompanyFoodEntity(companyFoodDto.getId(), companyFoodDto.getMemberId(),
                companyFoodDto.getName(), companyFoodDto.getRegistrationDate(),
                foodPriceDto.getPrice());
        });
    }

    public Optional<CompanyFoodEntity> findById(EntityManager em, Long id) {
        CompanyFoodEntity companyFoodDto = em.find(CompanyFoodEntity.class, id);
        if (companyFoodDto == null) {
            return Optional.empty();
        }
        CompanyFoodEntity companyFoodDtoWithTempPrice = addTempPrice(em, companyFoodDto);
        return Optional.ofNullable(companyFoodDtoWithTempPrice);
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
