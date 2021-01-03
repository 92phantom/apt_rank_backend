package com.apt_rank.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TransHstDto {

    private String floor;
    private String trans_yymmdd;

}
