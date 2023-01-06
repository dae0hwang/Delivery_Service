package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.GeneralMemberEntity;
import com.example.apideliveryservice.entity.OrderEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findAllByGeneralMemberEntity(GeneralMemberEntity generalMember);
}
