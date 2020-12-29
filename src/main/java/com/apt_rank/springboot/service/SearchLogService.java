package com.apt_rank.springboot.service;

import com.apt_rank.springboot.domain.search.SearchLog;
import com.apt_rank.springboot.domain.search.SearchLogRepository;
import com.apt_rank.springboot.domain.search.projection.TopRankInterface;
import com.apt_rank.springboot.web.dto.SearchLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchLogService {


    private final SearchLogRepository searchLogRepository;

    public Object searchTopRank(int topRank){

        // top rank 10위 외에는 조회 불가
        // 조회 기준 : 7일 누적
        if(topRank == 10) {

            return searchLogRepository.findByTopRank(topRank);
        }
        else{
            return null;
        }

    }

    public boolean saveSearchLog(SearchLogDto param){
        List<SearchLog> searchlog ;
        /*  검색 저장 이력이 없는 대상 */

        try {
            searchlog = searchLogRepository
                    .findByIp_Port(param.getClient_ip(), param.getPort(), param.getSerial_num(), param.getExclusive_area());

            if(searchlog.size() == 0){
                searchLogRepository.save(param.toEntity());
                return true;
            } else{
                return false;
            }

        } catch(Exception e){
            e.printStackTrace();
            return false;
        }


    }

}
