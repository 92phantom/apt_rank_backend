package com.apt_rank.springboot.service;

import com.apt_rank.springboot.domain.search.SearchLogRepository;
import com.apt_rank.springboot.web.dto.SearchLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SearchLogService {


    private final SearchLogRepository searchLogRepository;

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
