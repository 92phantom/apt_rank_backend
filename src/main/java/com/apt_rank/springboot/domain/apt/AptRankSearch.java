package com.apt_rank.springboot.domain.apt;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name="apt_trans_rank")
@IdClass(AptTransRank_Id.class)
public class AptRankSearch {

    @Id
    @Column(nullable = false, name = "rank")
    private int rank;

    @Id
    @Column(nullable = false, name ="op_dt")
    private Date op_dt;

    @Column(nullable = true, length = 30, name ="serial_num")
    private String serial_num;

    @Column(nullable = false, length = 45, name ="apt_name")
    private String apt_name;

    @Column(nullable = true, length = 45, name ="apt_floor")
    private String apt_floor;

    @Column(nullable = true, length = 45, name ="exclusive_area")
    private String exclusive_area;

    @Column(nullable = false, length = 45, name ="trans_yymm")
    private String trans_yymm;

    @Column(nullable = false, length = 45, name ="trans_yymmdd")
    private String trans_yymmdd;

    @Column(nullable = false, length = 45, name ="trans_price")
    private String trans_price;

    @Column(nullable = false, length = 45, name ="addr_cd")
    private String addr_cd;

    @Column(nullable = false, length = 45, name ="addr_pr_cd")
    private String addr_pr_cd;

    @Column(nullable = false, length = 45, name ="addr_ct_cd")
    private String addr_ct_cd;

    @Column(nullable = false, length = 45, name ="addr_dong_cd")
    private String addr_dong_cd;

    @Column(nullable = false, length = 45, name ="unit_price")
    private String unit_price;

    @Column(nullable = false, name ="audit_dtm")
    private Date audit_dtm;


}
