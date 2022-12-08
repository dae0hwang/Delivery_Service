package com.example.apideliveryservice.service;

import com.example.apideliveryservice.dto.GeneralMemberOrderDto;
import com.example.apideliveryservice.entity.OrderDetailEntity;
import com.example.apideliveryservice.repository.CompanyFoodRepository;
import com.example.apideliveryservice.repository.OrderRepository;
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
public class OrderService {

    @Value("${persistenceName:@null}")
    private String persistenceName;
    private final OrderRepository orderRepository;
    private final CompanyFoodRepository companyFoodRepository;

    public void addOrder(Long generalId, List<OrderDetailEntity> orderDetailElementList)
        throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            orderRepository.addOrder(em, generalId, orderDetailElementList);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }

    public List<GeneralMemberOrderDto> findOrderListByGeneralId(Long generalId) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<GeneralMemberOrderDto> findOrderList =
                orderRepository.findOrderListByGeneralId(
                em, generalId);
            tx.commit();
            return findOrderList;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }
}
