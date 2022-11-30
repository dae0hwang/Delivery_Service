package com.example.apideliveryservice.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="purchase_list")
public class PurchaseListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "general_id")
    private Long generalId;
    @Column(name = "company_id")
    private Long companyId;
    @Column(name = "food_id")
    private Long foodId;
    @Column(name = "food_price")
    private BigDecimal foodPrice;
    @Column(name = "registration_date")
    private Timestamp createdAt;
}
