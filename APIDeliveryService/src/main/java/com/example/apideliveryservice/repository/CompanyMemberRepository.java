package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.CompanyMemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyMemberRepository extends JpaRepository<CompanyMemberEntity, Long> {

    @Query
    Optional<CompanyMemberEntity> findByLoginName(String loginName);
}
