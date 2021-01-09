package com.apt_rank.springboot.domain.apt.projection;

public interface AptDetail {

    // addr_cd 정보
    String getAddr_pr_cd();
    String getAddr_ct_cd();
    String getAddr_dong_cd();
    String getAddr_cd();

    // 전국, 지역 랭킹 개수
    Integer getWide_apt_cnt();
    Integer getLocal_apt_cnt();


    String getApt_floor();
    String getTrans_yymmdd();

    String getApt_name();
    String getSerial_num();

    String getApt_Build_yy();
    String getProvince_nm();
    String getCity_nm();
    String getDong_nm();

    Integer getRank();
    Integer getLocal_rank();

    String getTrans_price();

}
