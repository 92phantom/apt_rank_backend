package com.apt_rank.springboot.domain.search;

import lombok.Data;

@Data
public class AptSearch {

    private String apt_name;
    private String province_nm;
    private String city_nm;
    private String dong_nm;
    private String serial_num;
    private String ct_cd;
    private String dong_cd;
    private String pr_cd;
    private String addr_cd;
}
