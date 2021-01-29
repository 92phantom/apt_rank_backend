package com.apt_rank.springboot.service.struct;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Trans_key {

    private String current_dt;
    private String range;

    public Trans_key(String current_dt, String range){
            this.current_dt = current_dt;
            this.range = range;
    }

}
