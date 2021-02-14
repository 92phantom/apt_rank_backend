package com.apt_rank.springboot.service;

import com.apt_rank.springboot.domain.apt.AptRankRepository;
import com.apt_rank.springboot.domain.apt.projection.AptDetail;
import com.apt_rank.springboot.domain.apt.projection.AptVolumeRank;
import com.apt_rank.springboot.domain.apt.projection.ExclusiveInterface;
import com.apt_rank.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AptSearchService {

    public enum RankType{
     /*
        CALCULATE RANKING
        상위 1-5% : DIA
        상위 6-15% : GOLD
        상위 15-50% : SILVER
        상위 51-85% : BRONZE
        나머지 : FLOWER
     */

        TOP5PER("DIA"),
        TOP10PER("GOLD"),
        TOP20PER("SILVER"),
        TOP30PER("BRONZE"),
        ELSEPER("GRASS");

        final private String name;

        private RankType(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }


    @Autowired
    private AptRankRepository aptRankRepository;

    // 전용면적 1) 시리얼 번호 존재
    public AptExclusiveDto findExclusiveBySerialNum(String serial_num){

        List<ExclusiveInterface> tmp = aptRankRepository.findExclusiveBySerialNum(serial_num);

        AptExclusiveDto aptExclusiveDto = new AptExclusiveDto();

        List<Integer> tmpList = new ArrayList<>();

        for(int i=0; i<tmp.size(); i++){
            tmpList.add(tmp.get(i).getExclusive_area());
        }

        aptExclusiveDto.setExclusive_area(tmpList);

        return aptExclusiveDto;
    }

    // 전용면적 2) 시리얼 번호 미존재
    public AptExclusiveDto findExclusiveByAddrCd(String pr_cd, String ct_cd, String dong_cd, String addr_cd){

        List<ExclusiveInterface> tmp =  aptRankRepository.findExclusiveByAddrCd(pr_cd, ct_cd, dong_cd, addr_cd);
        AptExclusiveDto aptExclusiveDto = new AptExclusiveDto();

        List<Integer> tmpList = new ArrayList<>();

        for(int i=0; i<tmp.size(); i++){
            tmpList.add(tmp.get(i).getExclusive_area());
        }

        aptExclusiveDto.setExclusive_area(tmpList);

        return aptExclusiveDto;

    }

    public AptSearchDto findAptDetailByAddrCd(String pr_cd, String ct_cd, String dong_cd, String addr_cd, int exclusive_area){

        return findAptDetail(pr_cd, ct_cd, dong_cd, addr_cd, exclusive_area);
    }

    // 아파트 상세 정보 검색
    public AptSearchDto findAptDetailBySerialNum(String serial_num, int exclusive_area){

        // ADDR_CD 추출
        AptDetail addr_info = aptRankRepository.findAddrCdBySerialNum(serial_num);

        System.out.println("##########debug");
        System.out.println(addr_info.getAddr_pr_cd());
        System.out.println(addr_info.getAddr_ct_cd());
        System.out.println(addr_info.getAddr_dong_cd());
        System.out.println(addr_info.getAddr_cd());
        System.out.println(exclusive_area);
        System.out.println("##########debug");

        return findAptDetail(addr_info.getAddr_pr_cd(),
                addr_info.getAddr_ct_cd(),
                addr_info.getAddr_dong_cd(),
                addr_info.getAddr_cd(),
                exclusive_area);

    }

    // Search common function
    public AptSearchDto findAptDetail(String pr_cd, String ct_cd, String dong_cd, String addr_cd, int exclusive_area){

        // Step 1. 전국에서 내 랭킹
        System.out.println("Step 1. 전국에서 내 랭킹");
        List<AptDetail> wide_info = aptRankRepository.findWideRankByAddrCd(pr_cd,
                ct_cd,
                dong_cd,
                addr_cd,
                exclusive_area);

        // Step 2. 전국 아파트 개수
        System.out.println("Step 2. 전국 아파트 개수");
        AptDetail wide_cnt = aptRankRepository.findWideAptCount();

        // Step 3. 지역에서 내 랭킹
        System.out.println("Step 3. 지역에서 내 랭킹");
        List<AptDetail> local_info = aptRankRepository.findLocalRankByAddrCd(pr_cd,
                ct_cd,
                dong_cd,
                addr_cd,
                exclusive_area);

        // Step 4. 지역 아파트 개수
        System.out.println("Step 4. 지역 아파트 개수");
        AptDetail local_cnt = aptRankRepository.findLocalAptCount(pr_cd,
                ct_cd,
                dong_cd);

        // Step 5-1. my_apt_dtl
        System.out.println("Step 5-1. my_apt_dtl");
        List<AptDetail> my_apt_dtl = aptRankRepository.findMyAptDtlByAddrCd(pr_cd,
                ct_cd,
                dong_cd,
                addr_cd,
                exclusive_area);

        // Step 5-2. my_apt_dtl -> trans_hst
        System.out.println("Step 5-2. my_apt_dtl -> trans_hst");

        MyAptDto myAptDto = new MyAptDto();
        myAptDto.setApt_build_yy(my_apt_dtl.get(0).getApt_Build_yy());
        myAptDto.setProvince_nm(my_apt_dtl.get(0).getProvince_nm());
        myAptDto.setCity_nm(my_apt_dtl.get(0).getCity_nm());
        myAptDto.setDong_nm(my_apt_dtl.get(0).getDong_nm());
        myAptDto.setApt_name(my_apt_dtl.get(0).getApt_name());
        myAptDto.setMax_trans_price(my_apt_dtl.get(0).getMax_trans_price());


        List<TransHstDto> transHstDto = new ArrayList<TransHstDto>();

        for (int i=0; i<my_apt_dtl.size(); i++){

            TransHstDto transHst_element = new TransHstDto();
            transHst_element.setFloor(my_apt_dtl.get(i).getApt_floor());
            transHst_element.setTrans_yymmdd(my_apt_dtl.get(i).getTrans_yymmdd());
            transHst_element.setTrans_price(my_apt_dtl.get(i).getTrans_price());
            transHstDto.add(transHst_element);
        }

        myAptDto.setTrans_hst(transHstDto);


        AptSearchDto aptSearchDto = new AptSearchDto();

        // 전국 내 랭킹
        aptSearchDto.setWide_top_nm(wide_info.get(0).getApt_name());
        aptSearchDto.setWide_top_serial_num(wide_info.get(0).getSerial_num());
        aptSearchDto.setWide_pr_cd(wide_info.get(0).getAddr_pr_cd());
        aptSearchDto.setWide_ct_cd(wide_info.get(0).getAddr_ct_cd());
        aptSearchDto.setWide_dong_cd(wide_info.get(0).getAddr_dong_cd());
        aptSearchDto.setWide_addr_cd(wide_info.get(0).getAddr_cd());
        aptSearchDto.setWide_unit_price(wide_info.get(0).getUnit_price());

        // 전국 내 Tier 계산
        /* 내가 전국 1위 일 경우*/
        if(wide_info.size() == 1){
            aptSearchDto.setWide_my_rank(wide_info.get(0).getRank());
            aptSearchDto.setWide_my_tier(
                    getTierName(wide_cnt.getWide_apt_cnt(), wide_info.get(0).getRank())
            );
            myAptDto.setUnit_price(wide_info.get(0).getUnit_price());

        }
        else{
            aptSearchDto.setWide_my_rank(wide_info.get(1).getRank());
            aptSearchDto.setWide_my_tier(
                    getTierName(wide_cnt.getWide_apt_cnt(), wide_info.get(1).getRank())
            );
            myAptDto.setUnit_price(wide_info.get(1).getUnit_price());

        }

        // 지역구 내 랭킹
        aptSearchDto.setLocal_top_nm(local_info.get(0).getApt_name());
        aptSearchDto.setLocal_top_serial_num(local_info.get(0).getSerial_num());
        aptSearchDto.setLocal_top_pr_cd(local_info.get(0).getAddr_pr_cd());
        aptSearchDto.setLocal_top_ct_cd(local_info.get(0).getAddr_ct_cd());
        aptSearchDto.setLocal_top_dong_cd(local_info.get(0).getAddr_dong_cd());
        aptSearchDto.setLocal_top_addr_cd(local_info.get(0).getAddr_cd());
        aptSearchDto.setLocal_unit_price(local_info.get(0).getUnit_price());

        // 지역 내 Tier 계산
        /* 내가 지역 1위 일 경우*/
        if(local_info.size() == 1){
            aptSearchDto.setLocal_my_rank(local_info.get(0).getLocal_rank());

            aptSearchDto.setLocal_my_tier(
                    getTierName(local_cnt.getLocal_apt_cnt(), local_info.get(0).getLocal_rank())
            );
        }
        else{
            aptSearchDto.setLocal_my_rank(local_info.get(1).getLocal_rank());

            aptSearchDto.setLocal_my_tier(
                    getTierName(local_cnt.getLocal_apt_cnt(), local_info.get(1).getLocal_rank())
            );
        }

        aptSearchDto.setMy_apt_dtl(myAptDto);

        return aptSearchDto;
    }

    public String getTierName(int total_cnt, int my_rank ){

        int rank_tier = (my_rank * 100) / total_cnt;

        String tier_name = "";

        if(rank_tier <= 5){
            tier_name = RankType.valueOf("TOP5PER").getName();
        }else if(rank_tier > 5 && rank_tier <= 15){
            tier_name = RankType.valueOf("TOP10PER").getName();
        }else if(rank_tier > 15 && rank_tier <= 50){
            tier_name = RankType.valueOf("TOP20PER").getName();
        }else if(rank_tier > 50 && rank_tier <= 85){
            tier_name = RankType.valueOf("TOP30PER").getName();
        }else{
            tier_name = RankType.valueOf("ELSEPER").getName();
        }

        return tier_name;
    }


    public AptRankDto findAptRankByPage(int period, String range, String st_exclusive_area, String end_exclusive_area, int page){


        int st_page = 0;
        int end_page = 0;

        end_page = page * 10;
        st_page = end_page - 9;

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyyMM");

        cal.add(Calendar.MONTH, -period);

        String start_dt = df.format(cal.getTime());



        List<AptVolumeRank> aptVolumeRanks;


        if(range.equals("00")){
            aptVolumeRanks = aptRankRepository.findAptRankAllByPage(start_dt, st_exclusive_area, end_exclusive_area, st_page, end_page);
        }
        else{
            aptVolumeRanks = aptRankRepository.findAptRankByPage(start_dt, range, st_exclusive_area, end_exclusive_area, st_page, end_page);
        }

        List<AptRankDtlDto> aptRankDtlDtos = new ArrayList<>();

        for(int i=0; i<aptVolumeRanks.size(); i++){
            AptRankDtlDto aptRankDtlDto = new AptRankDtlDto();

            aptRankDtlDto.setRank(aptVolumeRanks.get(i).getRank());
            aptRankDtlDto.setAddr_cd(aptVolumeRanks.get(i).getAddr_cd());
            aptRankDtlDto.setApt_name(aptVolumeRanks.get(i).getApt_name());
            aptRankDtlDto.setPr_cd(aptVolumeRanks.get(i).getAddr_pr_cd());
            aptRankDtlDto.setProvince_nm(aptVolumeRanks.get(i).getProvince_nm());
            aptRankDtlDto.setCt_cd(aptVolumeRanks.get(i).getAddr_ct_cd());
            aptRankDtlDto.setCity_nm(aptVolumeRanks.get(i).getCity_nm());
            aptRankDtlDto.setDong_cd(aptVolumeRanks.get(i).getAddr_dong_cd());
            aptRankDtlDto.setDong_nm(aptVolumeRanks.get(i).getDong_nm());
            aptRankDtlDto.setSerial_num(aptVolumeRanks.get(i).getSerial_num());
            aptRankDtlDto.setExclusive_area(aptVolumeRanks.get(i).getExclusive_area());
            aptRankDtlDto.setMax_trans_price(aptVolumeRanks.get(i).getMax_trans_price());
            aptRankDtlDtos.add(aptRankDtlDto);

        }

        AptRankDto aptRankDto = new AptRankDto();
        aptRankDto.setPage(page);
        aptRankDto.setRank_dtl(aptRankDtlDtos);

        return aptRankDto;
    }


}
