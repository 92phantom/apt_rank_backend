package com.apt_rank.springboot.domain.apt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@IdClass(AptSubsSpc_Id.class)
@Table(name="apt_subs_spc")
public class AptSubsSpc {

    @Id
    @Column(length=45, nullable = false)
    private String pblanc_no;

    @Id
    @Column(length=45, nullable = false)
    private String house_secd;

    @Column(length=100, nullable = false)
    private String house_nm;

    @Column(length=45)
    private String in_date;

    @Column(length=45)
    private String rcept_se;

    @Column(length=45)
    private String restde_at;

    @Column(length=45)
    private String in_day;

    @Column(length=45)
    private String rm;

    @Column(length=45)
    private String house_manage_no;

    @Column(length=6)
    private String inqirepd;

    @Column(nullable = false)
    private Date audit_dtm;

}
