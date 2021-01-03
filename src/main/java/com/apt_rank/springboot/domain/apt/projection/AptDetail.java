package com.apt_rank.springboot.domain.apt.projection;

public interface AptDetail {

    // addr_cd 정보
    String getAddr_pr_cd();
    String getAddr_ct_cd();
    String getAddr_dong_cd();
    String getAddr_addr_cd();

    // 전국, 지역 랭킹 개수
    Integer getWide_apt_cnt();
    Integer getLocal_apt_cnt();

    // Detail
    String getWide_top_nm();
    String getWide_top_serial_num();

    String getFloor();
    String getTrans_yymmdd();

    String getApt_Build_yy();
    String getProvince_nm();
    String getCity_nm();
    String getDong_nm();

    String getLocal_my_tier();
    Integer getLocal_my_rank();

    String getLocal_top_serial_num();
    String getLocal_top_nm();

    String getWide_my_tier();
    Integer getWide_my_rank();

    Integer getRank();

}
