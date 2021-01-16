package com.apt_rank.springboot.service;

import com.apt_rank.springboot.web.dto.LoanDetail;
import com.apt_rank.springboot.web.dto.LoanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AptCalculatorService {

    public LoanDto equivalencePrincipal(long principal, double interest, int period){

        long cal_principal = principal;
        long total_repayment = 0;
        long total_loan_interest = 0;

        List<LoanDetail> loanDetailList = new ArrayList<>();

        for (int i=0; i<period; i++){

            int idx = i+1;
            long principal_payment = principal/period;
            long loan_interest = (long) (cal_principal*(interest/12));
            long monthly_payment = principal_payment + loan_interest;

            cal_principal -= principal_payment;

            total_loan_interest += loan_interest;

            LoanDetail loanDetail = new LoanDetail();
            loanDetail.setIdx(idx);
            loanDetail.setLoan_interest(loan_interest);
            loanDetail.setPrincipal_payment(principal_payment);
            loanDetail.setMonthly_payment(monthly_payment);

            loanDetailList.add(loanDetail);
        }

        total_repayment = principal + total_loan_interest;

        LoanDto loanDto = new LoanDto();
        loanDto.setLoanDetail(loanDetailList);
        loanDto.setTotal_loan_interest(total_loan_interest);
        loanDto.setTotal_repayment(total_repayment);

        return loanDto;


    }

    public LoanDto equivalencePrincipalAndInterest(long principal, double interest, int period){

        long cal_principal = principal;
        long total_repayment = 0;
        long total_loan_interest = 0;

        List<LoanDetail> loanDetailList = new ArrayList<>();

        for (int i = 0; i < period; i++) {

            int idx = i+1;
            long loan_interest = (long) (cal_principal*(interest/12));
            double cal_1 = Math.pow((1+interest/12), period);
            double cal_2 = cal_1 -1;

            long monthly_payment = (long) (loan_interest * cal_1 / cal_2);

            long principal_payment = monthly_payment - loan_interest;

            total_loan_interest += loan_interest;

            LoanDetail loanDetail = new LoanDetail();
            loanDetail.setIdx(idx);
            loanDetail.setLoan_interest(loan_interest);
            loanDetail.setPrincipal_payment(principal_payment);
            loanDetail.setMonthly_payment(monthly_payment);

            loanDetailList.add(loanDetail);

        }

        total_repayment = principal + total_loan_interest;

        LoanDto loanDto = new LoanDto();
        loanDto.setLoanDetail(loanDetailList);
        loanDto.setTotal_loan_interest(total_loan_interest);
        loanDto.setTotal_repayment(total_repayment);

        return loanDto;


    }

    public LoanDto bullet(long principal, double interest, int period){

        long total_repayment = 0;
        long total_loan_interest = 0;

        List<LoanDetail> loanDetailList = new ArrayList<>();

        for (int i = 0; i < period; i++) {
            int idx = i+1;

            long loan_interest = (long) (principal*(interest/12));

            long monthly_payment;

            if(idx != period) {
                monthly_payment = loan_interest;
            }
            else {
                monthly_payment = principal + loan_interest;
            }

            long principal_payment = principal;

            total_loan_interest += loan_interest;

            LoanDetail loanDetail = new LoanDetail();
            loanDetail.setIdx(idx);
            loanDetail.setLoan_interest(loan_interest);
            loanDetail.setPrincipal_payment(principal_payment);
            loanDetail.setMonthly_payment(monthly_payment);

            loanDetailList.add(loanDetail);

        }

        total_repayment = principal + total_loan_interest;

        LoanDto loanDto = new LoanDto();
        loanDto.setLoanDetail(loanDetailList);
        loanDto.setTotal_loan_interest(total_loan_interest);
        loanDto.setTotal_repayment(total_repayment);

        return loanDto;

    }


}
