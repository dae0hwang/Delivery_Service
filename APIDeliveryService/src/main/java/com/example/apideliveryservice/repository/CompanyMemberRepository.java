package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.CompanyMemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyMemberRepository extends JpaRepository<CompanyMemberEntity, Long> {

    Optional<CompanyMemberEntity> findByLoginName(String loginName);
}
