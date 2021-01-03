package com.apt_rank.springboot.service;

import com.apt_rank.springboot.domain.apt.AptRankRepository;
import com.apt_rank.springboot.domain.apt.projection.AptDetail;
import com.apt_rank.springboot.domain.apt.projection.ExclusiveInterface;
import com.apt_rank.springboot.web.dto.AptSearchDto;
import com.apt_rank.springboot.web.dto.MyAptDto;
import com.apt_rank.springboot.web.dto.TransHstDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AptSearchService {

    private AptRankRepository aptRankRepository;

    // 전용면적 1) 시리얼 번호 존재
    public List<ExclusiveInterface> findExclusiveBySerialNum(String serial_num){

        return aptRankRepository.findExclusiveBySerialNum(serial_num);
    }

    // 전용면적 2) 시리얼 번호 미존재
    public List<ExclusiveInterface> findExclusiveByAddrCd(String pr_cd, String ct_cd, String dong_cd, String addr_cd){

        return aptRankRepository.findExclusiveByAddrCd(pr_cd, ct_cd, dong_cd, addr_cd);
    }

    //
    public AptSearchDto findAptDetailByAddrCd(String pr_cd, String ct_cd, String dong_cd, String addr_cd, int exclusive_area){

        return findAptDetail(pr_cd, ct_cd, dong_cd, addr_cd, exclusive_area);
    }

    // 아파트 상세 정보 검색
    public AptSearchDto findAptDetailBySerialNum(String serial_num, int exclusive_area){

        // ADDR_CD 추출
        AptDetail addr_info = aptRankRepository.findAddrCdBySerialNum(serial_num);

        return findAptDetail(addr_info.getAddr_pr_cd(),
                addr_info.getAddr_ct_cd(),
                addr_info.getAddr_dong_cd(),
                addr_info.getAddr_addr_cd(),
                exclusive_area);

    }

    // Search common function
    public AptSearchDto findAptDetail(String pr_cd, String ct_cd, String dong_cd, String addr_cd, int exclusive_area){

        // Step 1. 전국에서 내 랭킹
        List<AptDetail> wide_info = aptRankRepository.findWideRankByAddrCd(pr_cd,
                ct_cd,
                dong_cd,
                addr_cd,
                exclusive_area);

        // Step 2. 전국 아파트 개수
        AptDetail wide_cnt = aptRankRepository.findWideAptCount();

        // Step 3. 지역에서 내 랭킹
        List<AptDetail> local_info = aptRankRepository.findLocalRankByAddrCd(pr_cd,
                ct_cd,
                dong_cd,
                addr_cd,
                exclusive_area);

        // Step 4. 지역 아파트 개수
        AptDetail local_cnt = aptRankRepository.findLocalAptCount(pr_cd,
                ct_cd,
                dong_cd);

        // Step 5-1. my_apt_dtl
        List<AptDetail> my_apt_dtl = aptRankRepository.findMyAptDtlByAddrCd(pr_cd,
                ct_cd,
                dong_cd,
                addr_cd,
                exclusive_area);

        // Step 5-2. my_apt_dtl -> trans_hst
        List<MyAptDto> myAptDtoList = null;
        MyAptDto myAptDto = new MyAptDto();
        myAptDto.setApt_build_yy(my_apt_dtl.get(0).getApt_Build_yy());
        myAptDto.setProvince_nm(my_apt_dtl.get(0).getProvince_nm());
        myAptDto.setCity_nm(my_apt_dtl.get(0).getCity_nm());
        myAptDto.setDong_nm(my_apt_dtl.get(0).getDong_nm());

        List<TransHstDto> transHstDto = null;

        for (int i=0; i<my_apt_dtl.size(); i++){

            TransHstDto transHst_element = new TransHstDto();
            transHst_element.setFloor(my_apt_dtl.get(i).getFloor());
            transHst_element.setTrans_yymmdd(my_apt_dtl.get(i).getTrans_yymmdd());

            transHstDto.add(transHst_element);
        }

        myAptDto.setTrans_hst(transHstDto);
        myAptDtoList.add(myAptDto);


        AptSearchDto aptSearchDto = new AptSearchDto();

        aptSearchDto.setWide_top_nm(wide_info.get(0).getWide_top_nm());
        aptSearchDto.setWide_top_serial_num(wide_info.get(0).getWide_top_serial_num());

        // 전국 내 랭킹
        if(wide_info.size() == 1){
            aptSearchDto.setWide_my_rank(wide_info.get(0).getRank());
            // 전국 내 Tier 계산
            aptSearchDto.setWide_my_tier((wide_info.get(0).getRank()/wide_cnt.getWide_apt_cnt()) * 100+"");

        }
        else{
            aptSearchDto.setWide_my_rank(wide_info.get(1).getRank());
            // 전국 내 Tier 계산
            aptSearchDto.setWide_my_tier((wide_info.get(1).getRank()/wide_cnt.getWide_apt_cnt()) * 100+"");
        }

        // 지역구 내 랭킹
        if(local_info.size() == 1){
            aptSearchDto.setLocal_my_rank(local_info.get(0).getRank());
            // 전국 내 Tier 계산
            aptSearchDto.setWide_my_tier((local_info.get(0).getRank()/local_cnt.getLocal_apt_cnt()) * 100+"");
        }
        else{
            aptSearchDto.setLocal_my_rank(local_info.get(1).getRank());
            // 전국 내 Tier 계산
            aptSearchDto.setWide_my_tier((local_info.get(1).getRank()/local_cnt.getLocal_apt_cnt()) * 100+"");
        }

        // 지역 내 Tier 계산

        aptSearchDto.setLocal_top_nm(local_info.get(0).getLocal_top_nm());
        aptSearchDto.setLocal_top_serial_num(local_info.get(0).getLocal_top_serial_num());

        aptSearchDto.setMy_apt_dtl(myAptDtoList);

        return aptSearchDto;
    }

}
