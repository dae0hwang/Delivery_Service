package com.example.apideliveryservice.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "company_member")
public class CompanyMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login_name")
    private String loginName;
    private String password;
    private String name;
    @Column(name = "phone_verification")
    private Boolean phoneVerification;
    @CreatedDate
    private Timestamp registrationDate;

    public CompanyMemberEntity(String loginName, String password, String name,
        Boolean phoneVerification) {
        this.loginName = loginName;
        this.password = password;
        this.name = name;
        this.phoneVerification = phoneVerification;
    }
}
