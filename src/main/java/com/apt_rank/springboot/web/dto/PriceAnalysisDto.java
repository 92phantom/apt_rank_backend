package com.apt_rank.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PriceAnalysisDto {

    private String range;
    private List<PriceChange> price_change;

}
