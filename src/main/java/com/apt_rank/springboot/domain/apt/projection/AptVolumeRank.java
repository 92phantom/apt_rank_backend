package com.apt_rank.springboot.domain.apt.projection;

public interface AptVolumeRank {

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

    String getMax_trans_price();
    Integer getExclusive_area();

}
