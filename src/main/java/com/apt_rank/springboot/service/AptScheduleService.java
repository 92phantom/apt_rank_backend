package com.apt_rank.springboot.service;

import com.apt_rank.springboot.domain.apt.AptSubsSpc;
import com.apt_rank.springboot.domain.apt.AptSubsSpcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service

public class AptScheduleService {
    @Autowired
    private AptSubsSpcRepository aptSubsSpcRepository;

    // 청약캘린더 - 상위 항목
    public List<AptSubsSpc> findScheduleByInqirePd(String inqirePd, String type){

        type = type.toLowerCase();

        if(!type.matches("all|ALL")) {

            if(type.equals("special")){
                type = "01";
            }
            else if(type.equals("first")){
                type = "02";
            }
            else if(type.equals("second")){
                type = "03";
            }
            else if(type.equals("office")){
                type = "04";
            }
            else if(type.equals("none")){
                type = "05";
            }
            else if(type.equals("rent")){
                type = "06";
            }

            return aptSubsSpcRepository.findScheduleByInqirepdAndRcept(inqirePd, type);
        }
        else {
            return aptSubsSpcRepository.findScheduleByInqirepd(inqirePd);
        }
    }

}
