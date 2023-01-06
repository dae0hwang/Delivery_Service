package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.CompanyFoodHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyFoodHistoryRepository extends JpaRepository<CompanyFoodHistoryEntity, Long> {

    //save
    //findById
}
