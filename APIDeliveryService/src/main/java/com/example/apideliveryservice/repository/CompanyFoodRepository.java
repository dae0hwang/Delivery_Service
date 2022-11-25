package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.dto.CompanyFoodPriceDto;
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

//    public static void main(String[] args) {
//        CompanyFoodRepository repository = new CompanyFoodRepository();
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-mysql");
//        EntityManager em = emf.createEntityManager();
//        EntityTransaction tx = em.getTransaction();
//        CompanyFoodDto companyFoodDto = new CompanyFoodDto(null, 11l, "핫도그",
//            new Timestamp(System.currentTimeMillis()), null);
//        BigDecimal price = new BigDecimal("6000");
//        try {
//            tx.begin();
//            repository.add(em, companyFoodDto, price);
//            tx.commit();
//        } catch (Exception e) {
//            log.error("ex", e);
//            tx.rollback();
//        } finally {
//            em.close();
//            emf.close();
//        }
//    }

    public void add(EntityManager em, CompanyFoodDto companyFoodDto, BigDecimal price) {
        em.persist(companyFoodDto);
        CompanyFoodPriceDto companyFoodPriceDto = new CompanyFoodPriceDto(null, companyFoodDto,
            price, new Timestamp(System.currentTimeMillis()));
        em.persist(companyFoodPriceDto);
    }

    public Optional<CompanyFoodDto> findByNameAndMemberId(EntityManager em, Long memberId,
        String findName) {
        String jpql = "SELECT f FROM CompanyFoodDto f WHERE f.name=:name AND f.memberId=:memberId";
        try {
            CompanyFoodDto findCompanyFood = em.createQuery(jpql, CompanyFoodDto.class)
                .setParameter("name", findName).setParameter("memberId", memberId)
                .getSingleResult();
            return Optional.ofNullable(findCompanyFood);
        } catch (NoResultException e) {
            log.info("ex", e);
            return Optional.empty();
        }

    }

    public List<CompanyFoodDto> findAllFood(EntityManager em, Long id) {
        List<CompanyFoodDto> foodDtoList = new ArrayList<>();
        try {
            String foodDtoJpql = "SELECT f FROM CompanyFoodDto f WHERE f.memberId=:memberId";
            foodDtoList = em.createQuery(foodDtoJpql, CompanyFoodDto.class)
                .setParameter("memberId", id).getResultList();
            addListTempPrice(em, foodDtoList);
            return foodDtoList;
        } catch (NoResultException e) {
            log.info("ex", e);
            return foodDtoList;
        }
    }

    private void addListTempPrice(EntityManager em, List<CompanyFoodDto> foodDtoList) {
        foodDtoList.replaceAll(companyFoodDto -> {
            String foodPriceDtoJpql = "select p from CompanyFoodPriceDto p "
                + "where p.companyFood=:companyFood order by p.updateDate desc";
            List<CompanyFoodPriceDto> companyFoodPriceDtoList = em.createQuery(foodPriceDtoJpql, CompanyFoodPriceDto.class)
                .setParameter("companyFood", companyFoodDto).getResultList();
            CompanyFoodPriceDto foodPriceDto = companyFoodPriceDtoList.stream().findFirst()
                .get();
            return new CompanyFoodDto(companyFoodDto.getId(), companyFoodDto.getMemberId(),
                companyFoodDto.getName(), companyFoodDto.getRegistrationDate(),
                foodPriceDto.getPrice());
        });
    }

    public Optional<CompanyFoodDto> findById(EntityManager em, Long id) {
        CompanyFoodDto companyFoodDto = em.find(CompanyFoodDto.class, id);
        if (companyFoodDto == null) {
            return Optional.empty();
        }
        CompanyFoodDto companyFoodDtoWithTempPrice = addTempPrice(em, companyFoodDto);
        return Optional.ofNullable(companyFoodDtoWithTempPrice);
    }

    private CompanyFoodDto addTempPrice(EntityManager em, CompanyFoodDto companyFoodDto) {
        String foodPriceDtoJpql = "select p from CompanyFoodPriceDto p "
            + "where p.companyFood=:companyFood order by p.updateDate desc";
        List<CompanyFoodPriceDto> companyFoodPriceDtoList = em.createQuery(foodPriceDtoJpql,
            CompanyFoodPriceDto.class).setParameter("companyFood", companyFoodDto).getResultList();
        CompanyFoodPriceDto foodPriceDto = companyFoodPriceDtoList.stream().findFirst()
            .get();
        return new CompanyFoodDto(companyFoodDto.getId(), companyFoodDto.getMemberId(),
            companyFoodDto.getName(), companyFoodDto.getRegistrationDate(),
            foodPriceDto.getPrice());
    }

    public void updatePrice(EntityManager em, Long foodId, BigDecimal price) {
        CompanyFoodDto companyFoodDto = em.find(CompanyFoodDto.class, foodId);
        CompanyFoodPriceDto companyFoodPriceDto = new CompanyFoodPriceDto(null, companyFoodDto,
            price, new Timestamp(System.currentTimeMillis()));

        em.persist(companyFoodPriceDto);
    }
}
