package com.apt_rank.springboot.web.dto;

import com.apt_rank.springboot.domain.apt.AptTransPriceHst;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class AptSearchDto {

    private String wide_top_nm;
    private String wide_top_serial_num;
    private int wide_my_rank;
    private String wide_my_tier;
    private String wide_pr_cd;
    private String wide_ct_cd;
    private String wide_dong_cd;
    private String wide_addr_cd;

    private String local_top_nm;
    private String local_top_serial_num;
    private String local_top_pr_cd;
    private String local_top_ct_cd;
    private String local_top_dong_cd;
    private String local_top_addr_cd;
    private int local_my_rank;
    private String local_my_tier;

    private List<MyAptDto> my_apt_dtl;

}
