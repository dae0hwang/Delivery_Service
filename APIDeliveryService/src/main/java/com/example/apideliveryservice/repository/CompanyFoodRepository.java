package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyFoodRepository extends JpaRepository<CompanyFoodEntity, Long> {

    //save
    //findById
    //update setPrice

    @Query
    Optional<CompanyFoodEntity> findByNameAndCompanyMemberEntity(String name,
        CompanyMemberEntity companyMemberEntity);

    @Query
    List<CompanyFoodEntity> findAllByCompanyMemberEntity(CompanyMemberEntity companyMemberEntity);


}
