package com.apt_rank.springboot.domain.apt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@IdClass(AptTransPriceHst_Id.class)
@Table(name="apt_trans_price_dtl")
public class AptTransPriceHst {

    @Id
    @Column(length=30, nullable = true)
    private String serial_num;

    @Id
    @Column(length = 20, nullable = false)
    private int trans_price;

    @Id
    @Column(nullable = false)
    private Date audit_dtm;

    @Id
    @Column(length=30, nullable = false)
    private String trans_yymmdd;

    @Column(length=30, nullable = false)
    private String apt_name;

    @Column(length=30)
    private String apt_floor;

    @Column(length=30)
    private String apt_capacity;

    @Column(length=30)
    private String apt_build_yy;

    @Column(length=30)
    private String trans_yymm;

    @Column(length=30)
    private String addr_cd;

    @Column(length=30)
    private String addr_pr_cd;

    @Column(length=30)
    private String addr_ct_cd;

    @Column(length=30)
    private String addr_dong_cd;

    @Column(length=30)
    private String addr_dong_nm;

    @Column(length=30)
    private String audit_id;

    @Column(length=30)
    private String trans_dd;

    @Column(length=20)
    private int unit_price;

}
