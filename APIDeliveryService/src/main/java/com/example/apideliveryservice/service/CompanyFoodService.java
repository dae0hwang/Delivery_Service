package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.CompanyFoodDto;
import com.example.apideliveryservice.exception.DuplicatedFoodNameException;
import com.example.apideliveryservice.exception.NonExistentFoodIdException;
import com.example.apideliveryservice.exception.NonExistentMemberIdException;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
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
    public void addFood(String memberId, String name, String price) throws Exception {
        CompanyFoodDto companyFoodDto = new CompanyFoodDto(null, Long.valueOf(memberId), name, new Timestamp(System.currentTimeMillis()), null);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            validateDuplicateFoodName(em, Long.valueOf(memberId), name);
            companyFoodRepository.add(em, companyFoodDto, new BigDecimal(price));
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
            List<CompanyFoodDto> allFood = companyFoodRepository.findAllFood(em,
                Long.valueOf(memberId));
            tx.commit();
            return allFood;
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
            CompanyFoodDto findFood = companyFoodRepository.findById(em, Long.valueOf(id))
                .orElse(null);
            if (findFood == null) {
                throw new NonExistentFoodIdException();
            }
            tx.commit();
            return findFood;
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
}
