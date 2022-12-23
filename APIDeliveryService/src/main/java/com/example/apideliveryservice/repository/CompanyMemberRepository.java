package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.CompanyMemberEntity;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

public interface CompanyMemberRepository extends JpaRepository<CompanyMemberEntity, Long> {

}
