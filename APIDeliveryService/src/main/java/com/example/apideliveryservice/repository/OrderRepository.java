package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.dto.GeneralMemberOrderDto;
import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.OrderDetailEntity;
import com.example.apideliveryservice.entity.OrderEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderRepository {

    private final CompanyFoodRepository companyFoodRepository;

    public void addOrder(EntityManager em, Long generalId, List<OrderDetailEntity> orderDetailElementList) {
        OrderEntity orderEntity = new OrderEntity(null, generalId,
            new Timestamp(System.currentTimeMillis()));
        em.persist(orderEntity);
        for (OrderDetailEntity orderDetailElement : orderDetailElementList) {
            BigDecimal findPrice = companyFoodRepository.findPriceByFoodId(em,
                orderDetailElement.getFoodId());
            OrderDetailEntity orderDetailEntity = new OrderDetailEntity(null, orderEntity,
                orderDetailElement.getCompanyId(), orderDetailElement.getFoodId(), findPrice,
                orderDetailElement.getFoodAmount());
            em.persist(orderDetailEntity);

        }
    }

    public List<GeneralMemberOrderDto> findOrderListByGeneralId(EntityManager em, Long generalId) {
//        String jpql =
//            "select new com.example.apideliveryservice.dto.GeneralMemberOrderDto(o"
//                + ".registrationDate, o.id, o.generalId, od.foodId, cp.name, od.foodPrice, od"
//                + ".foodAmount, od.companyId) "
//                + "from OrderEntity as o \n"
//                + "join OrderDetailEntity as od on o=od.orderEntity\n"
//                + "join CompanyFoodEntity as cp on od.foodId=cp.id\n"
//                + "where o.generalId=:generalId\n" + "order by o.registrationDate desc";
//
//        List<GeneralMemberOrderDto> findList = em.createQuery(jpql, GeneralMemberOrderDto.class)
//            .setParameter("generalId", generalId).getResultList();
//        return findList;

        CriteriaBuilder criteriaBuilder  = em.getCriteriaBuilder();
        CriteriaQuery<GeneralMemberOrderDto> criteriaQuery = criteriaBuilder.createQuery(
            GeneralMemberOrderDto.class);

        Root<OrderDetailEntity> od = criteriaQuery.from(OrderDetailEntity.class);
        Join<OrderDetailEntity, OrderEntity> o = od.join("orderEntity",
            JoinType.INNER);
        Join<OrderDetailEntity, CompanyFoodEntity> cp = od.join("foodId", JoinType.INNER);

        criteriaQuery.select(criteriaBuilder.construct(GeneralMemberOrderDto.class, o.get("registrationDate"), o.get("id"), o.get("generalId"), od.get("foodId"), cp.get("name"), od.get("foodPrice"),
                od.get("foodAmount"), od.get("companyId")))
        .multiselect(od, o, cp)
            .where(criteriaBuilder.equal(od.get("generaId"), generalId));

        List<GeneralMemberOrderDto> resultList = em.createQuery(criteriaQuery).getResultList();
        return resultList;
    }

    public List<GeneralMemberOrderDto> findOrderListByGeneralId2(EntityManager em, Long generalId) {
        CriteriaBuilder criteriaBuilder  = em.getCriteriaBuilder();
        CriteriaQuery<GeneralMemberOrderDto> criteriaQuery = criteriaBuilder.createQuery(
            GeneralMemberOrderDto.class);
        
        Root<OrderDetailEntity> root = criteriaQuery.from(OrderDetailEntity.class);
        Join<OrderDetailEntity, OrderEntity> joinOrderEntity = root.join("orderEntity",
            JoinType.INNER);
        Join<OrderDetailEntity, CompanyFoodEntity> joinFoodId = root.join("foodId", JoinType.INNER);

        criteriaQuery.multiselect(root, joinOrderEntity, joinFoodId)
            .where(criteriaBuilder.equal(root.get("generaId"), generalId));

        List<GeneralMemberOrderDto> resultList = em.createQuery(criteriaQuery).getResultList();
        return resultList;
    }
}
