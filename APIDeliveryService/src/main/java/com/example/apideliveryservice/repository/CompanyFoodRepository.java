package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.CompanyFoodEntity;
import com.example.apideliveryservice.entity.CompanyMemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyFoodRepository extends JpaRepository<CompanyFoodEntity, Long> {

    //save
    //findById
    //update setPrice

    Optional<CompanyFoodEntity> findByNameAndCompanyMemberEntity(String name,
        CompanyMemberEntity companyMemberEntity);

    List<CompanyFoodEntity> findAllByCompanyMemberEntity(CompanyMemberEntity companyMemberEntity);


}
