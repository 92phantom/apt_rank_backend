package com.apt_rank.springboot.service;

import com.apt_rank.springboot.domain.search.SearchLogRepository;
import com.apt_rank.springboot.web.dto.SearchLogDto;
import com.apt_rank.springboot.web.dto.TopRankDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchLogService {


    private final SearchLogRepository searchLogRepository;

    public List<TopRankDto> searchTopRank(int topRank){

        if(topRank == 10) {
            return searchLogRepository.findByTopRank(topRank);
        }
        else{
            return (List<TopRankDto>) new TopRankDto();
        }

    }

    public void saveSearchLog(SearchLogDto param){
        SearchLogDto searchlog = new SearchLogDto(searchLogRepository
                        .findByIp_Port(param.getClient_ip(), param.getPort()));

        /*
         *   검색 저장 이력이 없는 대상
         */

        if(searchlog == null){
            searchLogRepository.save(param.toEntity());
        }
        else{
            // 7시간에 한번
            System.out.println("@@@"+searchlog.getAudit_dtm());
        }
    }

}
