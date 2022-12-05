package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.exception.NonExistentFoodIdException;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import java.math.BigDecimal;
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


@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyFoodService {

    @Value("${persistenceName:@null}")
    private String persistenceName;

    private final CompanyFoodRepository companyFoodRepository;

    /**
     * @param memberId, name, price
     * @throws Exception
     * @throws DuplicatedFoodNameException
     */
    public void addFood(String memberId, String name, BigDecimal price) throws Exception {
        CompanyFoodEntity companyFoodEntity = new CompanyFoodEntity(null, Long.valueOf(memberId), name,
            new Timestamp(System.currentTimeMillis()), null);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            validateDuplicateFoodName(em, Long.valueOf(memberId), name);
            companyFoodRepository.add(em, companyFoodEntity, price);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }

    private void validateDuplicateFoodName(EntityManager em, Long memberId, String foodName) {
        companyFoodRepository.findByNameAndMemberId(em, memberId, foodName)
            .ifPresent(m -> {
                throw new DuplicatedFoodNameException();
            });
    }

    /**
     * @param memberId
     * @return foodList found by memberId
     * @throws Exception
     */
    public List<CompanyFoodDto> findAllFood(String memberId) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<CompanyFoodEntity> allFoodEntity = companyFoodRepository.findAllFood(em,
                Long.valueOf(memberId));
            tx.commit();
            List<CompanyFoodDto> allFoodDto = changeAllFoodEntityToDto(allFoodEntity);
            return allFoodDto;
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
     * @return findFood
     * @throws Exception
     * @throws NonExistentFoodIdException
     */
    public CompanyFoodDto findFood(String id) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            CompanyFoodEntity findFoodEntity = companyFoodRepository.findById(em, Long.valueOf(id))
                .orElse(null);
            if (findFoodEntity == null) {
                throw new NonExistentFoodIdException();
            }
            tx.commit();
            CompanyFoodDto findFoodDto = changeFindFoodEntityToDto(findFoodEntity);
            return findFoodDto;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }

    /**
     * @param foodId
     * @param price
     * @throws Exception
     */
    public void updatePrice(String foodId, String price) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            companyFoodRepository.updatePrice(em, Long.valueOf(foodId), new BigDecimal(price));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }

    private List<CompanyFoodDto> changeAllFoodEntityToDto(List<CompanyFoodEntity> allFoodEntity) {
        List<CompanyFoodDto> allFoodDto = allFoodEntity.stream().map(
            m -> new CompanyFoodDto(m.getId(), m.getMemberId(), m.getName(),
                m.getRegistrationDate(), m.getTempPrice())).collect(Collectors.toList());
        return allFoodDto;
    }

    private CompanyFoodDto changeFindFoodEntityToDto(CompanyFoodEntity findFoodEntity) {
        CompanyFoodDto findFoodDto = new CompanyFoodDto(findFoodEntity.getId(),
            findFoodEntity.getMemberId(), findFoodEntity.getName(),
            findFoodEntity.getRegistrationDate(), findFoodEntity.getTempPrice());
        return findFoodDto;
    }
}
