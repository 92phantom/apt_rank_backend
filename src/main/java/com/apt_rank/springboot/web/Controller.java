package com.apt_rank.springboot.web;

import com.apt_rank.springboot.domain.apt.AptSubsSpc;
import com.apt_rank.springboot.domain.apt.projection.AptSoaring;
import com.apt_rank.springboot.domain.search.AptSearch;
import com.apt_rank.springboot.domain.search.projection.TopRankInterface;
import com.apt_rank.springboot.service.*;
import com.apt_rank.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class Controller {

    private final AptSearchService aptSearchService;
    private final AptAnalysisService aptAnalysisService;
    private final SearchLogService searchLogService;
    private final ElasticsearchService elasticsearchService;
    private final AptScheduleService aptScheduleService;
    private final AptCalculatorService aptCalculatorService;
    private final AptSoaringService aptSoaringService;
    /*  1. 인기 검색어
            1-1. 인기 검색 상위 10개 아파트 리스트 검색
            기준 기간 - 14일 누적 검색
     */
    @RequestMapping("/popular")
    public List<TopRankInterface> index(@RequestParam("top") int top){

        return (List<TopRankInterface>) searchLogService.searchTopRank(top);
    }

    /*  2. 검색
          2-1. 검색창에서 사용자가 입력했을 때 관련 검색어 노출
     */
    @RequestMapping("/search")
    public Mono<List<AptSearch>> searchAptList(@RequestParam(value = "apt_name") String apt_name,
                                               @RequestParam(value = "related") int related_rank) {

        System.out.println("###"+ apt_name);

        apt_name = apt_name.replaceAll(" ","");

        return elasticsearchService.matchAll(apt_name, related_rank);
    }

    /*
        2. 검색
            2-2. 전용 면적 조회
     */
    @RequestMapping("/search/exclusive")
    public AptExclusiveDto searchAptExclusive(
            @RequestParam(value = "serial_num", required = false) String serial_num,
            @RequestParam(value = "pr_cd",      required = false) String pr_cd,
            @RequestParam(value = "ct_cd",      required = false) String ct_cd,
            @RequestParam(value = "dong_cd",    required = false) String dong_cd,
            @RequestParam(value = "addr_cd",    required = false) String addr_cd) {

        // case 1 (시리얼번호 존재)
        if(serial_num != null){
            return aptSearchService.findExclusiveBySerialNum(serial_num);
        }

        // case 2 (시리얼번호 미존재)
        else if (   serial_num == null &&
                    pr_cd   != null &&
                    ct_cd   != null &&
                    dong_cd != null &&
                    addr_cd != null ){
            return aptSearchService.findExclusiveByAddrCd(pr_cd, ct_cd, dong_cd, addr_cd);
        }

        return null;

    }

    /*
        2. 검색
            2-3. 상세 정보 검색 : exclusive_area 는 빈 값이면 무조건 오류 처리
    */
    @RequestMapping("/search/detail")
    public AptSearchDto searchAptDetail(
            @RequestParam(value = "serial_num", required = false)   String serial_num,
            @RequestParam(value = "pr_cd",      required = false)   String pr_cd,
            @RequestParam(value = "ct_cd",      required = false)   String ct_cd,
            @RequestParam(value = "dong_cd",    required = false)   String dong_cd,
            @RequestParam(value = "addr_cd",    required = false)   String addr_cd,
            @RequestParam(value = "exclusive_area", required = true) int exclusive_area) {

        if(exclusive_area == 0){
            return null;
        }

        // case 1 (시리얼번호 존재)
        if(serial_num != null   &&
                    exclusive_area != 0){

            System.out.println("here we go");
            return aptSearchService.findAptDetailBySerialNum(serial_num, exclusive_area);
        }

        // case 2 (시리얼번호 미존재)
        else if (   serial_num == null &&
                    pr_cd   != null &&
                    ct_cd   != null &&
                    dong_cd != null &&
                    addr_cd != null &&
                    exclusive_area != 0){

            return aptSearchService.findAptDetailByAddrCd(pr_cd, ct_cd, dong_cd, addr_cd, exclusive_area);

        }
        else {
            return null;
        }
    }

    /*
        3. 검색 로그 쌓기 : 7일 간 누적순위 log
     */
    @RequestMapping(value = "/search/log"
            , method=RequestMethod.POST)
    public ResponseEntity<?> searchLog(@ModelAttribute final SearchLogDto requestParam,
                            HttpServletRequest request) {

        boolean save_flag = searchLogService.saveSearchLog(requestParam);

        if(save_flag){
            return new ResponseEntity(HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.ALREADY_REPORTED);
        }

    }

    /*
        4. 지역별 분석
            4-1. 지역구 아파트 가격 변화
     */
    @RequestMapping(value = "/analysis")
    public PriceAnalysisDto analysisArea(@RequestParam(value = "range" , required = true)   String range,
                                         @RequestParam(value = "year"  , required = true)   String year){

        return aptAnalysisService.localChange(range, year);

    }


    /*
        4. 지역별 분석
            4-2. 현재 거래량 많은 아파트
     */

    @RequestMapping(value = "/analysis/volume")
    public List<TransRankDto> analysisPrice(@RequestParam(value = "range" , required = true)   String range,
                                      @RequestParam(value = "related"  , required = true)   String related){

        return aptAnalysisService.volumeRank(range, related);

    }

    /*
        7. 랭킹
     */
//    @RequestMapping(value = "rank")
//    public

    /*
        5. 청약캘린더
     */

    @RequestMapping(value = "/schedule")
    public List<AptSubsSpc> scheduleCalendar(@RequestParam(value = "date") String date,
                @RequestParam(value = "type") String type){
        // 일자별로 조회될수 있도록 조건 추가

        if(date.length() == 6){
            return aptScheduleService.findScheduleByInqirePd(date, type);
        }

        else{
            return null;
        }
    }

    /*
        6. 대출 계산기
     */
    @RequestMapping(value = "/calculator")
    public LoanDto loanCalculator(@RequestParam(value = "type", required = true) String type,
                                  @RequestParam(value = "principal", required = true) long principal,
                                  @RequestParam(value = "interest", required = true) double interest,
                                  @RequestParam(value = "period", required = true) int period
                                  ){
        /*
            대출 Type
            01 - 원금 균등상환
            02 - 원리금 균등상환
            03 - 만기일시상환
         */

        interest = interest * 0.01;

        if(type.equals("01")){

            return aptCalculatorService.equivalencePrincipal(principal, interest, period);
        }
        else if(type.equals("02")) {

            return aptCalculatorService.equivalencePrincipalAndInterest(principal, interest, period);
        }
        else if(type.equals("03")){

            return aptCalculatorService.bullet(principal, interest, period);

        }

        return new LoanDto();

    }
    /*
        8. 검색 이후 급상승 순위
     */
    @RequestMapping(value = "/search/soaring")
    public List<AptSoaring> soaringRank(){
        return aptSoaringService.findAptSoaring();
    }

}
