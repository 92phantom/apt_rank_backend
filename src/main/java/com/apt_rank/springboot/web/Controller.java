package com.apt_rank.springboot.web;

import com.apt_rank.springboot.domain.apt.AptRankSearch;
import com.apt_rank.springboot.domain.apt.AptTransPriceHst;
import com.apt_rank.springboot.service.AptSearchService;
import com.apt_rank.springboot.web.dto.AptRankDto;
import com.apt_rank.springboot.web.dto.AptSearchDto;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
//@RequestMapping("/apart")
public class Controller {

    private final AptSearchService aptSearchService;

    @RequestMapping("/popular")
    public List<JSONObject> index(@RequestParam("top") int top){
        List<JSONObject> out = new ArrayList<>();
        for(int i=1; i<=top; i++){

            JSONObject json = new JSONObject();
            json.put("apt_name", "시그니엘");
            json.put("exclusive_area", "12");
            json.put("serial_num", "11305-4704");
            json.put("rank", i);
            json.put("province_nm", "서울특별시");
            json.put("city_nm", "강북구");
            json.put("dong_nm", "수유동");
            json.put("max_trans_price", "10600");
            out.add(json);

        }
        return out;
    }

    /*
        2. 검색
            2-1. 검색창에서 사용자가 입력했을 때 관련 검색어 노출
     */
    @RequestMapping("/search")
    public String searchAptList(@RequestParam(value = "apt_name") String apt_name,
                                @RequestParam(value = "related") int related_rank) {


        return "hihi";
    }

    /*
        2. 검색
            2-2. 전용 면적 조회
            2-3. 상세 정보 검색
     */
    @RequestMapping("/search/detail")
    public String searchAptDetail(
            @RequestParam(value = "serial_num", required = true) String apt_name,
            @RequestParam(value = "exclusive_area", required = false) int exclusive_area
            ) {

        if(exclusive_area != 0){
            return "bye";
        }

        return "hihi";
    }

    /*
        3. 검색 로그 쌓기 : 7일 간 누적순위 log
     */
    @RequestMapping(value = "/search/log"
            , method=RequestMethod.POST)
    public String searchLog(@ModelAttribute final String requestParam,
                            HttpServletRequest request) {



        return "hihi";
    }





//    @GetMapping("/apt")
//    @RequestMapping
//    public List<JSONObject> handleAptName(@RequestParam("apart_name") String apt_name, Model map){
//        map.addAttribute("msg", "employees request by dept: " + apt_name);
//
//        List<JSONObject> out = new ArrayList<>();
//
//        for(int i=1; i<3; i++){
//            JSONObject json = new JSONObject();
//            json.put("id", 1);
//            json.put("name", "롯데 시그니엘 "+i+"차");
//            json.put("address1", "서울시");
//            json.put("address2", "강동구");
//            json.put("address3", "천호동");
//
//            out.add(json);
//        }
//
//
//        return out;
//    }

    @RequestMapping
    public List<AptSearchDto> handleAptList_Search(@RequestParam("apart_name") String apt_name){

        return aptSearchService.findByAptName(apt_name);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public AptRankDto handleAptName_dup(@RequestParam("apart_name") String apt_name,
                                        @RequestParam("rc") String region_cd,
                                        @RequestParam("dc") String dong_cd){

        // 검색된 아파트 정보는 ranking 테이블에 쌓는 로직 필요
        // 저장해뒀다가 한번에 처리하는 방식으로

        return aptSearchService.findRankByApt_Name(apt_name, region_cd, dong_cd);
    }




}
