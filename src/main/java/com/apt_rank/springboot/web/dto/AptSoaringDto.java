package com.apt_rank.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AptSoaringDto {

    private String addr_pr_cd;
    private String addr_ct_cd;
    private String addr_dong_cd;
    private String addr_cd;
    private String province_nm;
    private String city_nm;
    private String dong_nm;
    private String apt_name;
    private String serial_num;
    private int rank;
    private int exclusive_area;
    private int variation;
    private String max_trans_price;

}
