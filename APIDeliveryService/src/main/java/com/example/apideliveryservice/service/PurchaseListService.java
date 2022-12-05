package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.FoodPriceSumDto;
import com.example.apideliveryservice.entity.PurchaseListEntity;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import com.example.apideliveryservice.repository.PurchaseListRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
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
public class PurchaseListService {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    private final PurchaseListRepository repository;
    private final CompanyFoodRepository companyFoodRepository;

    public void addList(Long generalId, Long companyId, Long foodId)
        throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            BigDecimal priceByNameAndMemberId = companyFoodRepository.findPriceByFoodId(em, foodId);
            PurchaseListEntity purchaseListEntity = new PurchaseListEntity(null, generalId,
                companyId, foodId, priceByNameAndMemberId,
                new Timestamp(System.currentTimeMillis()));
            repository.create(em, purchaseListEntity);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }


    public List<FoodPriceSumDto> companyAllOfDay() throws SQLException {
        try (Connection connection = repository.connectHikariCp()){
            List<FoodPriceSumDto> list = repository.companyAllOfDay(connection);
            return list;
        }
    }


    //    public static void main(String[] args) {
//        PurchaseListRepository repository= new PurchaseListRepository();
//        CompanyFoodRepository companyFoodRepository = new CompanyFoodRepository();
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-mysql");
//        EntityManager em = emf.createEntityManager();
//        EntityTransaction tx = em.getTransaction();
//
//        BigDecimal price = new BigDecimal("6000");
//        try {
//            tx.begin();
//            BigDecimal priceByNameAndMemberId = companyFoodRepository.findPriceByNameAndMemberId(em,
//                26l, "참치김밥");
//            PurchaseListEntity purchaseListEntity = new PurchaseListEntity(null, 77l,
//                26l, 11l, priceByNameAndMemberId,
//                new Timestamp(System.currentTimeMillis()));
//            repository.create(em, purchaseListEntity);
//            tx.commit();
//        } catch (Exception e) {
//            log.error("ex", e);
//            tx.rollback();
//        } finally {
//            em.close();
//            emf.close();
//        }
//    }
}
