package com.apt_rank.springboot.service;

import com.apt_rank.springboot.domain.apt.AptSubsSpc;
import com.apt_rank.springboot.domain.apt.AptSubsSpcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service

public class AptScheduleService {
    @Autowired
    private AptSubsSpcRepository aptSubsSpcRepository;

    // 청약캘린더 - 상위 항목
    public List<AptSubsSpc> findScheduleByInqirePd(String inqirePd){

        return aptSubsSpcRepository.findScheduleByInqirePd(inqirePd);
    }

}
