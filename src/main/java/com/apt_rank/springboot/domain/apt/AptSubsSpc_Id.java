package com.apt_rank.springboot.domain.apt;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Data
@Embeddable
public class AptSubsSpc_Id implements Serializable {

    private String pblanc_no;
    private String house_secd;

}
