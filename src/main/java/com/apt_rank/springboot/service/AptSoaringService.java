package com.apt_rank.springboot.service;

import com.apt_rank.springboot.domain.apt.AptRankRepository;
import com.apt_rank.springboot.domain.apt.projection.AptSoaring;
import com.apt_rank.springboot.web.dto.AptSoaringDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AptSoaringService {

    @Autowired
    private AptRankRepository aptRankRepository;

    public List<AptSoaringDto> findAptSoaring(){

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyyMM");

        String end_dt = df.format(cal.getTime());

        cal.add(Calendar.MONTH, -6);

        String start_dt = df.format(cal.getTime());


        List<AptSoaring> aptSoarings = aptRankRepository.findAptSoaring(start_dt, end_dt);

        Collections.sort(aptSoarings, new Comparator<AptSoaring>() {
            @Override
            public int compare(AptSoaring t1, AptSoaring t2) {

                int t1_gap = t1.getMax_unit_price() - t1.getMin_unit_price();
                int t2_gap = t2.getMax_unit_price() - t2.getMin_unit_price();

                if(t1_gap > t2_gap){
                    return -1;
                }
                else if (t1_gap < t2_gap){
                    return 1;
                }
                return 0;

            }
        });

        List<AptSoaringDto> aptSoaringDtos = new ArrayList<>();

        for (int i=0; i<5; i++){

            AptSoaringDto aptSoaringDto = new AptSoaringDto();

            aptSoaringDto.setApt_name(aptSoarings.get(i).getApt_name());
            aptSoaringDto.setAddr_cd(aptSoarings.get(i).getAddr_cd());
            aptSoaringDto.setAddr_ct_cd(aptSoarings.get(i).getAddr_ct_cd());
            aptSoaringDto.setAddr_dong_cd(aptSoarings.get(i).getAddr_dong_cd());
            aptSoaringDto.setAddr_pr_cd(aptSoarings.get(i).getAddr_pr_cd());
            aptSoaringDto.setDong_nm(aptSoarings.get(i).getDong_nm());
            aptSoaringDto.setCity_nm(aptSoarings.get(i).getCity_nm());
            aptSoaringDto.setProvince_nm(aptSoarings.get(i).getProvince_nm());
            aptSoaringDto.setExclusive_area(aptSoarings.get(i).getExclusive_area());
            aptSoaringDto.setMax_trans_price(aptSoarings.get(i).getMax_trans_price());
            aptSoaringDto.setRank(i+1);
            aptSoaringDtos.add(aptSoaringDto);
        }

        return aptSoaringDtos;
    }

}
