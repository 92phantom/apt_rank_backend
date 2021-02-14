package com.apt_rank.springboot.web.dto;


import com.apt_rank.springboot.domain.search.SearchLog;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class SearchLogDto implements Serializable {

    private String  client_ip;
    private String  port;
    private String  serial_num;
    private String  pr_cd;
    private String  ct_cd;
    private String  dong_cd;
    private String  addr_cd;

    private int     exclusive_area;
    private Date    audit_dtm;

    @Builder
    public SearchLogDto(SearchLog entity){

        this.client_ip      = entity.getClient_ip();
        this.port           = entity.getPort();
        this.serial_num     = entity.getSerial_num();
        this.exclusive_area = entity.getExclusive_area();
        this.audit_dtm      = entity.getAudit_dtm();
        this.pr_cd = entity.getPr_cd();
        this.ct_cd = entity.getCt_cd();
        this.dong_cd = entity.getDong_cd();
        this.addr_cd = entity.getAddr_cd();
    }

    public SearchLog toEntity(){

        return SearchLog.builder()
                .client_ip(client_ip)
                .port(port)
                .serial_num(serial_num)
                .exclusive_area(exclusive_area)
                .pr_cd(pr_cd)
                .ct_cd(ct_cd)
                .dong_cd(dong_cd)
                .addr_cd(addr_cd)
                .audit_dtm(new Date(System.currentTimeMillis()))
                .build();

    }

}
