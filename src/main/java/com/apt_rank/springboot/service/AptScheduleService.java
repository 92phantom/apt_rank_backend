package com.apt_rank.springboot.service;

import com.apt_rank.springboot.domain.apt.AptSubsSpc;
import com.apt_rank.springboot.domain.apt.AptSubsSpcRepository;
import com.apt_rank.springboot.web.dto.ScheduleData;
import com.apt_rank.springboot.web.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service

public class AptScheduleService {
    @Autowired
    private AptSubsSpcRepository aptSubsSpcRepository;

    // 청약캘린더 - 상위 항목
    public List<ScheduleDto> findScheduleByInqirePd(String inqirePd, String type){

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

            List<AptSubsSpc> aptSubsSpcs = aptSubsSpcRepository.findScheduleByInqirepdAndRcept(inqirePd, type);

            HashSet<String> dateKey = new HashSet<String>();

            for(int i=0; i<aptSubsSpcs.size(); i++){

                dateKey.add(aptSubsSpcs.get(i).getIn_date());

            }

            List<ScheduleDto> scheduleDtoes = new ArrayList<>();

            Iterator<String> it = dateKey.iterator();
            while(it.hasNext()){

                String key = it.next();

                ScheduleDto scheduleDto = new ScheduleDto();
                List<ScheduleData> scheduleData_list = new ArrayList<>();
                scheduleDto.setDate(key);

                for(int i=0; i<aptSubsSpcs.size(); i++){

                    if(key.equals(aptSubsSpcs.get(i).getIn_date())) {
                        ScheduleData scheduleData = new ScheduleData();

                        scheduleData.setHouse_nm(aptSubsSpcs.get(i).getHouse_nm());
                        scheduleData.setHouse_secd(aptSubsSpcs.get(i).getHouse_secd());
                        scheduleData.setPblanc_no(aptSubsSpcs.get(i).getPblanc_no());
                        scheduleData.setIn_date(aptSubsSpcs.get(i).getIn_date());
                        scheduleData.setRcept_se(aptSubsSpcs.get(i).getRcept_se());
                        scheduleData.setRestde_at(aptSubsSpcs.get(i).getRestde_at());
                        scheduleData.setIn_day(aptSubsSpcs.get(i).getIn_day());
                        scheduleData.setRm(aptSubsSpcs.get(i).getRm());
                        scheduleData.setHouse_manage_no(aptSubsSpcs.get(i).getHouse_manage_no());
                        scheduleData.setInqirepd(aptSubsSpcs.get(i).getInqirepd());

                        scheduleData_list.add(scheduleData);
                    }
                }

                scheduleDto.setData(scheduleData_list);
                scheduleDtoes.add(scheduleDto);
            }

            return scheduleDtoes;
        }
        else {

            List<AptSubsSpc> aptSubsSpcs = aptSubsSpcRepository.findScheduleByInqirepd(inqirePd);

            HashSet<String> dateKey = new HashSet<String>();

            for(int i=0; i<aptSubsSpcs.size(); i++){

                dateKey.add(aptSubsSpcs.get(i).getIn_date());

            }

            List<ScheduleDto> scheduleDtoes = new ArrayList<>();

            Iterator<String> it = dateKey.iterator();
            while(it.hasNext()){

                String key = it.next();

                ScheduleDto scheduleDto = new ScheduleDto();
                List<ScheduleData> scheduleData_list = new ArrayList<>();
                scheduleDto.setDate(key);

                for(int i=0; i<aptSubsSpcs.size(); i++){

                    if(key.equals(aptSubsSpcs.get(i).getIn_date())) {
                        ScheduleData scheduleData = new ScheduleData();

                        scheduleData.setHouse_nm(aptSubsSpcs.get(i).getHouse_nm());
                        scheduleData.setHouse_secd(aptSubsSpcs.get(i).getHouse_secd());
                        scheduleData.setPblanc_no(aptSubsSpcs.get(i).getPblanc_no());
                        scheduleData.setIn_date(aptSubsSpcs.get(i).getIn_date());
                        scheduleData.setRcept_se(aptSubsSpcs.get(i).getRcept_se());
                        scheduleData.setRestde_at(aptSubsSpcs.get(i).getRestde_at());
                        scheduleData.setIn_day(aptSubsSpcs.get(i).getIn_day());
                        scheduleData.setRm(aptSubsSpcs.get(i).getRm());
                        scheduleData.setHouse_manage_no(aptSubsSpcs.get(i).getHouse_manage_no());
                        scheduleData.setInqirepd(aptSubsSpcs.get(i).getInqirepd());

                        scheduleData_list.add(scheduleData);
                    }
                }

                scheduleDto.setData(scheduleData_list);
                scheduleDtoes.add(scheduleDto);
            }


            Collections.sort(scheduleDtoes, new Comparator<ScheduleDto>() {
                @Override
                public int compare(ScheduleDto t1, ScheduleDto t2) {

                    int t1_int = Integer.parseInt(t1.getDate());
                    int t2_int = Integer.parseInt(t2.getDate());


                    if(t1_int > t2_int){
                        return 1;
                    }else if (t1_int < t2_int){
                        return -1;
                    }

                    return 0;
                }
            });

            return scheduleDtoes;

        }
    }

}
