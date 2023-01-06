package com.example.apideliveryservice.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@ToString(exclude = "orderDetailEntityList")
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "general_member_id")
    private GeneralMemberEntity generalMemberEntity;
    @CreatedDate
    @Column(name = "registration_date")
    private Timestamp registrationDate;
    @OneToMany(mappedBy = "orderEntity")
    private List<OrderDetailEntity> orderDetailEntityList = new ArrayList<>();

    public OrderEntity(GeneralMemberEntity generalMemberEntity) {
        this.generalMemberEntity = generalMemberEntity;
    }
}
