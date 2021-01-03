package com.apt_rank.springboot.domain.apt;


import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Data
@Embeddable
public class AptTransRank_Id implements Serializable {

    private int rank;
    private String op_dt;

}
