package com.apt_rank.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class MyAptDto {

    private String apt_build_yy;
    private String province_nm;
    private String city_nm;
    private String dong_nm;
    private List<TransHstDto> trans_hst;

}
