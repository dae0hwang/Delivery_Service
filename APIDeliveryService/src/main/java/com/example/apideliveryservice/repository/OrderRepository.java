package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.GeneralMemberOrderDto;
import com.example.apideliveryservice.entity.GeneralMemberEntity;
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

    public void addOrder(EntityManager em, Long generalMemberId, List<OrderDetailEntity> orderDetailElementList) {
        GeneralMemberEntity findGeneralMember = em.find(GeneralMemberEntity.class, generalMemberId);
        OrderEntity orderEntity = new OrderEntity(null, findGeneralMember,
            new Timestamp(System.currentTimeMillis()));
        em.persist(orderEntity);
        for (OrderDetailEntity orderDetailElement : orderDetailElementList) {
            BigDecimal findPrice = companyFoodRepository.findPriceByFoodId(em,
                orderDetailElement.getCompanyFoodEntity().getId());
            OrderDetailEntity orderDetailEntity = new OrderDetailEntity(null, orderEntity,
                orderDetailElement.getCompanyMemberEntity(),
                orderDetailElement.getCompanyFoodEntity(), findPrice,
                orderDetailElement.getFoodAmount());
            em.persist(orderDetailEntity);
        }
    }

    public List<GeneralMemberOrderDto> findOrderListByGeneralId(EntityManager em, Long generalId) {
        GeneralMemberEntity findGeneralMember = em.find(GeneralMemberEntity.class, generalId);
        String jpql = "select new com.example.apideliveryservice.dto.GeneralMemberOrderDto(o.registrationDate, o.id, o.generalMemberEntity.id, od.companyFoodEntity.id, cp.name, od.foodPrice, od.foodAmount, od.companyFoodEntity.id) "
            + "from OrderEntity as o \n"
            + "join OrderDetailEntity as od on o = od.orderEntity\n"
            + "join CompanyFoodEntity as cp on od.companyFoodEntity = cp\n"
            + "where o.generalMemberEntity=:generalMemberEntity\n"
            + "order by o.registrationDate desc";
        List<GeneralMemberOrderDto> findList = em.createQuery(jpql, GeneralMemberOrderDto.class)
            .setParameter("generalMemberEntity", findGeneralMember).getResultList();
        return findList;
    }
}
