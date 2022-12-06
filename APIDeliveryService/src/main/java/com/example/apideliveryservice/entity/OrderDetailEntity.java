package com.example.apideliveryservice.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order_detail_list")
public class OrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;
    @Column(name = "company_id")
    private Long companyId;
    @Column(name = "food_id")
    private Long foodId;
    @Column(name = "food_price")
    private BigDecimal foodPrice;
    @Column(name = "food_amount")
    private Integer foodAmount;
}
