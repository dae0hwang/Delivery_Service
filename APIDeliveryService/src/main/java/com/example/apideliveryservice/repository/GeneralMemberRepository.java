package com.example.apideliveryservice.repository;

import com.example.apideliveryservice.entity.GeneralMemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralMemberRepository extends JpaRepository<GeneralMemberEntity, Long> {

    Optional<GeneralMemberEntity> findByLoginName(String loginName);

}
