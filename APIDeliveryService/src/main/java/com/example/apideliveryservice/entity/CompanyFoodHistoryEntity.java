package com.example.apideliveryservice.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "company_food_history")
public class CompanyFoodHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "company_food_id", nullable = false)
    private CompanyFoodEntity companyFoodEntity;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Timestamp updateDate;

    public CompanyFoodHistoryEntity(CompanyFoodEntity companyFoodEntity, BigDecimal price,
        Timestamp updateDate) {
        this.companyFoodEntity = companyFoodEntity;
        this.price = price;
        this.updateDate = updateDate;
    }
}
