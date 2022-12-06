package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.OrderDetailEntity;
import com.example.apideliveryservice.entity.OrderEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderRepository {

    private final CompanyFoodRepository companyFoodRepository;

    public void addOrder(EntityManager em, Long generalId, List<OrderDetailEntity> orderDetailElementList) {
        //이거 하나이다.
        OrderEntity orderEntity = new OrderEntity(null, generalId,
            new Timestamp(System.currentTimeMillis()));
        em.persist(orderEntity);
        for (OrderDetailEntity orderDetailElement : orderDetailElementList) {
            //음식 가격 파악하기.
            BigDecimal findPrice = companyFoodRepository.findPriceByFoodId(em,
                orderDetailElement.getFoodId());
            OrderDetailEntity orderDetailEntity = new OrderDetailEntity(null, orderEntity,
                orderDetailElement.getCompanyId(), orderDetailElement.getFoodId(), findPrice,
                orderDetailElement.getFoodAmount());
            em.persist(orderDetailEntity);

        }
    }
}
