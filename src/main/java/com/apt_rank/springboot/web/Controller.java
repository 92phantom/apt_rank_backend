package com.apt_rank.springboot.web;

import com.apt_rank.springboot.domain.search.AptSearch;
import com.apt_rank.springboot.domain.search.projection.TopRankInterface;
import com.apt_rank.springboot.service.AptSearchService;
import com.apt_rank.springboot.service.ElasticsearchService;
import com.apt_rank.springboot.service.SearchLogService;
import com.apt_rank.springboot.web.dto.SearchLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class Controller {

    private final AptSearchService aptSearchService;
    private final SearchLogService searchLogService;
    private final ElasticsearchService elasticsearchService;

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

        apt_name = apt_name.replaceAll(" ","");

        return elasticsearchService.matchAll(apt_name, related_rank);
    }

    /*
        2. 검색
            2-2. 전용 면적 조회
     */
    @RequestMapping("/search/exclusive")
    public String searchAptExclusive(
            @RequestParam(value = "serial_num", required = false) String serial_num,
            @RequestParam(value = "pr_cd",      required = false) String pr_cd,
            @RequestParam(value = "ct_cd",      required = false) String ct_cd,
            @RequestParam(value = "dong_cd",    required = false) String dong_cd,
            @RequestParam(value = "addr_cd",    required = false) String addr_cd) {

        // case 1 (시리얼번호 존재)
        if(serial_num != null){
            return "hihi";
        }

        // case 2 (시리얼번호 미존재)
        else if (   serial_num == null &&
                    pr_cd   != null &&
                    ct_cd   != null &&
                    dong_cd != null &&
                    addr_cd != null ){

            return "bye";
        }
        else{
            return "error";
        }

    }

    /*
        2. 검색
            2-3. 상세 정보 검색 : exclusive_area 는 빈 값이면 무조건 오류 처리
    */
    @RequestMapping("/search/detail")
    public String searchAptDetail(
            @RequestParam(value = "serial_num", required = false)   String serial_num,
            @RequestParam(value = "pr_cd",      required = false)   String pr_cd,
            @RequestParam(value = "ct_cd",      required = false)   String ct_cd,
            @RequestParam(value = "dong_cd",    required = false)   String dong_cd,
            @RequestParam(value = "addr_cd",    required = false)   String addr_cd,
            @RequestParam(value = "exclusive_area", required = true) int exclusive_area) {

        if(exclusive_area != 0){
            return "exclusive_area cannot be null";
        }

        // case 1 (시리얼번호 존재)
        if(serial_num != null   &&
                    exclusive_area != 0){
            return "hihi";
        }

        // case 2 (시리얼번호 미존재)
        else if (   serial_num == null &&
                    pr_cd   != null &&
                    ct_cd   != null &&
                    dong_cd != null &&
                    addr_cd != null &&
                    exclusive_area != 0){

            return "bye";
        }
        else {
            return "unknown error";
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

}
