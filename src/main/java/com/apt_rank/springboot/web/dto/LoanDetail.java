package com.apt_rank.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoanDetail {

    private int idx;
    private long principal_payment;
    private long loan_interest;
    private long monthly_payment;
}
