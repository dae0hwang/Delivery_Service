package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyFoodPriceEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompanyFoodRepository {

    public static void main(String[] args) {
        CompanyFoodRepository repository = new CompanyFoodRepository();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-mysql");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        BigDecimal price = new BigDecimal("6000");
        try {
            tx.begin();
            BigDecimal price2 = repository.findPriceByNameAndMemberId(em, 26l, "참치김밥");
            tx.commit();
            log.info("price={}", price2);
        } catch (Exception e) {
            log.error("ex", e);
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

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

    //또 최신값을 가져와야하는 문제가있는데
    public BigDecimal findPriceByNameAndMemberId(EntityManager em, Long memberId,
        String findName) {
        String jpql = "SELECT f FROM CompanyFoodEntity f WHERE f.name=:name AND f.memberId=:memberId";
        try {
            CompanyFoodEntity findCompanyFood = em.createQuery(jpql, CompanyFoodEntity.class)
                .setParameter("name", findName).setParameter("memberId", memberId)
                .getSingleResult();
            CompanyFoodEntity companyFoodDtoWithTempPrice = addTempPrice(em, findCompanyFood);
            BigDecimal findPrice = companyFoodDtoWithTempPrice.getTempPrice();
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
