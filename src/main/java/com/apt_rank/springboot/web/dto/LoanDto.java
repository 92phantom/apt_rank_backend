package com.apt_rank.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class LoanDto {

    private long total_loan_interest;
    private long total_repayment;
    private List<LoanDetail> loanDetail;

}
