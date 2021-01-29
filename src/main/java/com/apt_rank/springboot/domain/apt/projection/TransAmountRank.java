package com.apt_rank.springboot.domain.apt.projection;

public interface TransAmountRank {

    String getApt_name();
    String getAddr_pr_cd();
    String getAddr_ct_cd();
    String getAddr_dong_cd();
    String getAddr_cd();
    String getSerial_num();
    Integer getTrans_amount();

    String getProvince_nm();
    String getCity_nm();
    String getDong_nm();

    String getMax_trans_price();

}
