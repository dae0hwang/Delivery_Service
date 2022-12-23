package com.example.apideliveryservice.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "general_member")
public class GeneralMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login_name")
    private String loginName;
    private String password;
    private String name;
    @Column(name = "phone_verification")
    private Boolean phoneVerification;
    @Column(name = "registration_date")
    private Timestamp registrationDate;
}