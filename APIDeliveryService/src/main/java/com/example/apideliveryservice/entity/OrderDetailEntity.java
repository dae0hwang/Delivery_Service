package com.example.apideliveryservice.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@ToString(exclude = "orderEntity")
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="order_detail")
public class OrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;
    @ManyToOne
    @JoinColumn(name = "company_member_id")
    private CompanyMemberEntity companyMemberEntity;
    @ManyToOne
    @JoinColumn(name = "company_food_id")
    private CompanyFoodEntity companyFoodEntity;
    @Column(name = "food_price")
    private BigDecimal foodPrice;
    @Column(name = "food_amount")
    private Integer foodAmount;

    public OrderDetailEntity(OrderEntity orderEntity, CompanyMemberEntity companyMemberEntity,
        CompanyFoodEntity companyFoodEntity, BigDecimal foodPrice, Integer foodAmount) {
        this.orderEntity = orderEntity;
        this.companyMemberEntity = companyMemberEntity;
        this.companyFoodEntity = companyFoodEntity;
        this.foodPrice = foodPrice;
        this.foodAmount = foodAmount;
    }
}
