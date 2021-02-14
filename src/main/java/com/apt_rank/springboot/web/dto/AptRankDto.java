package com.apt_rank.springboot.web.dto;

import com.apt_rank.springboot.domain.apt.AptRankSearch;
import com.apt_rank.springboot.domain.apt.AptTransPriceHst;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class AptRankDto {

    private int page;
    private List<AptRankDtlDto> rank_dtl;

}
