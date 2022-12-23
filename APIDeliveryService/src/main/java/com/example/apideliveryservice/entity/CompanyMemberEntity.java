package com.example.apideliveryservice.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "company_member")
public class CompanyMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login_name", unique = true, nullable = false)
    private String loginName;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(name = "phone_verification", nullable = false)
    private Boolean phoneVerification;
    @Column(name = "registration_date", nullable = false)
    private Timestamp createdAt;

    public CompanyMemberEntity(String loginName, String password, String name,
        Boolean phoneVerification, Timestamp createdAt) {
        this.loginName = loginName;
        this.password = password;
        this.name = name;
        this.phoneVerification = phoneVerification;
        this.createdAt = createdAt;
    }
}
