package com.apt_rank.springboot.service;

import com.apt_rank.springboot.domain.apt.AptRankRepository;
import com.apt_rank.springboot.domain.apt.projection.AptSoaring;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AptSoaringService {

    @Autowired
    private AptRankRepository aptRankRepository;

    public List<AptSoaring> findAptSoaring(){
        return aptRankRepository.findAptSoaring();
    }

}
