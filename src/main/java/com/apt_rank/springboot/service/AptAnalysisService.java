package com.apt_rank.springboot.service;

import com.apt_rank.springboot.domain.apt.AptRankRepository;
import com.apt_rank.springboot.domain.apt.AptTransRankRepository;
import com.apt_rank.springboot.domain.apt.projection.*;
import com.apt_rank.springboot.service.struct.Trans_key;
import com.apt_rank.springboot.web.dto.PriceAnalysisDto;
import com.apt_rank.springboot.web.dto.PriceChangeDto;
import com.apt_rank.springboot.web.dto.TransRankDto;
import com.apt_rank.springboot.web.dto.VolumeChangeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class AptAnalysisService {

    @Autowired
    private AptRankRepository aptRankRepository;

    @Autowired
    private AptTransRankRepository aptTransRankRepository;

    ConcurrentHashMap<Trans_key, List<TransRankDto>> curTransRank = new ConcurrentHashMap<>();


    public PriceAnalysisDto localChange(String range, String year) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyyMM");

        if (year.equals("5")) {
            cal.add(Calendar.MONTH, -61);
        } else if (year.equals("3")) {
            cal.add(Calendar.MONTH, -37);
        } else if (year.equals("1")) {
            cal.add(Calendar.MONTH, -13);
        }

        String start_dt = df.format(cal.getTime());

        List<PriceChange> priceChanges;
        List<VolumeChange> volumeChanges;

        if (range.equals("00")) {
            priceChanges = aptRankRepository.findAllTransAmount(start_dt);
            volumeChanges = aptRankRepository.findAllVolumeChange(start_dt);

        } else {
            priceChanges = aptRankRepository.findLocalTransAmount(start_dt, range);
            volumeChanges = aptRankRepository.findLocalVolumeChange(start_dt, range);
        }

        PriceAnalysisDto priceAnalysisDto = new PriceAnalysisDto();
        List<PriceChangeDto> priceChangeDtos = new ArrayList<>();
        List<VolumeChangeDto> volumeChangeDtos = new ArrayList<>();

        for (int i = 1; i < priceChanges.size(); i++) {

            PriceChangeDto priceChangeDto = new PriceChangeDto();

            priceChangeDto.setTrans_yymm(priceChanges.get(i).getTrans_yymm());

            double cal_trans_amount =
                    Double.parseDouble(priceChanges.get(i).getTrans_price()) -
                            Double.parseDouble(priceChanges.get(i - 1).getTrans_price());
            priceChangeDto.setTrans_price(cal_trans_amount + "");
            priceChangeDtos.add(priceChangeDto);

        }

        for (int i = 1; i < volumeChanges.size(); i++) {

            VolumeChangeDto volumeChangeDto = new VolumeChangeDto();
            volumeChangeDto.setTrans_yymm(volumeChanges.get(i).getTrans_yymm());

            double cal_volume_amount =
                    Double.parseDouble(volumeChanges.get(i).getTrans_amount()) -
                            Double.parseDouble(volumeChanges.get(i - 1).getTrans_amount());
            volumeChangeDto.setTrans_amount(cal_volume_amount + "");
            volumeChangeDtos.add(volumeChangeDto);
        }

        priceAnalysisDto.setPrice_change(priceChangeDtos);
        priceAnalysisDto.setVolume_change(volumeChangeDtos);
        priceAnalysisDto.setRange(range);

        return priceAnalysisDto;
    }

    public List<TransRankDto> volumeRank(String range, String related) {

        int related_parse = Integer.parseInt(related);

        if (related_parse > 5) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        // Current Date
        DateFormat cur_df = new SimpleDateFormat("yyyyMMdd");
        String current_dt = cur_df.format(cal.getTime());

        // Calculate Date
        DateFormat df = new SimpleDateFormat("yyyyMM");
        cal.add(Calendar.MONTH, -6);
        String start_dt = df.format(cal.getTime());

        Trans_key trans_key = new Trans_key(current_dt, range);

        Iterator<Trans_key> it = curTransRank.keySet().iterator();

        boolean date_flag = false;

        while (it.hasNext()) {

            Trans_key t_k = it.next();
            if (t_k.getCurrent_dt().equals(current_dt)) {
                date_flag = true;
                break;
            }

        }

        if (!date_flag) {
            System.err.println("date refresh");
            curTransRank = new ConcurrentHashMap<>();
        }

        it = curTransRank.keySet().iterator();
        while (it.hasNext()) {

            Trans_key t_k = it.next();
            if (t_k.getCurrent_dt().equals(current_dt) && t_k.getRange().equals(range)) {
                System.err.println("자료 있어요");
//                return curTransRank.get(t_k);

            }
        }

        System.err.println("자료 없어요");

        // Query
        List<TransAmountRank> transAmountRanks = new ArrayList<>();

        if (range.equals("00")) {
            transAmountRanks = aptTransRankRepository.findAllTransAmountRank(start_dt);

        } else {
            transAmountRanks = aptTransRankRepository.findLocalTransAmountRank(start_dt, range);
        }

        Collections.sort(transAmountRanks, new Comparator<TransAmountRank>() {
            @Override
            public int compare(TransAmountRank t1, TransAmountRank t2) {

                if (t1.getTrans_amount() > t2.getTrans_amount()) {
                    return -1;
                }
                else if (t1.getTrans_amount() < t2.getTrans_amount()) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        });

        // Set return value

        List<TransRankDto> transRankDto_list = new ArrayList<>();

        for (int i = 0; i < related_parse; i++) {

            // exclusive
            List<ExclusiveInterface> exclusiveList = aptRankRepository.findExclusiveByAddrCd(
                    transAmountRanks.get(i).getAddr_pr_cd(),
                    transAmountRanks.get(i).getAddr_ct_cd(),
                    transAmountRanks.get(i).getAddr_dong_cd(),
                    transAmountRanks.get(i).getAddr_cd()
            );

            TransRankDto transRankDto = new TransRankDto();

            transRankDto.setApt_name(transAmountRanks.get(i).getApt_name());
            transRankDto.setSerial_num(transAmountRanks.get(i).getSerial_num());
            transRankDto.setRank(i + 1);
            transRankDto.setAddr_cd(transAmountRanks.get(i).getAddr_cd());
            transRankDto.setPr_cd(transAmountRanks.get(i).getAddr_pr_cd());
            transRankDto.setCt_cd(transAmountRanks.get(i).getAddr_ct_cd());
            transRankDto.setDong_cd(transAmountRanks.get(i).getAddr_dong_cd());

            transRankDto.setCity_nm(transAmountRanks.get(i).getCity_nm());
            transRankDto.setProvince_nm(transAmountRanks.get(i).getProvince_nm());
            transRankDto.setDong_nm(transAmountRanks.get(i).getDong_nm());
            transRankDto.setMax_trans_price(transAmountRanks.get(i).getMax_trans_price());

            List<Integer> ex_int_list = new ArrayList<>();

            for (int j = 0; j < exclusiveList.size(); j++) {

                ex_int_list.add(exclusiveList.get(j).getExclusive_area());

            }

            transRankDto.setExclusive_area(ex_int_list);

            transRankDto_list.add(transRankDto);

        }

        curTransRank.put(new Trans_key(current_dt, range), transRankDto_list);

        return transRankDto_list;


    }


}
