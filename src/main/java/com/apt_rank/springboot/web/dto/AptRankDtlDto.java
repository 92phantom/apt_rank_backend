package com.apt_rank.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AptRankDtlDto {

    private String apt_name;
    private int exclusive_area;
    private String serial_num;
    private int rank;
    private String province_nm;
    private String city_nm;
    private String dong_nm;

    private String pr_cd;
    private String ct_cd;
    private String dong_cd;
    private String addr_cd;

    private String max_trans_price;

}
