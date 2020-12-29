package com.apt_rank.springboot.domain.apt;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Data
@Embeddable
public class AptTransPriceHst_Id implements Serializable {

    private String serial_num;
    private String trans_price;
    private Date audit_dtm;
    private String trans_yymmdd;

}
