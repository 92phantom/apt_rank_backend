package com.apt_rank.springboot.domain.apt.projection;

public interface AptSoaring {

    // addr_cd 정보
    String getAddr_pr_cd();
    String getAddr_ct_cd();
    String getAddr_dong_cd();
    String getAddr_cd();

    String getApt_name();
    String getSerial_num();

    String getProvince_nm();
    String getCity_nm();
    String getDong_nm();

    Integer getRank();
    Integer getVariation();

    String getMax_trans_price();
    Integer getExclusive_area();

    Integer getMax_unit_price();
    Integer getMin_unit_price();


}
