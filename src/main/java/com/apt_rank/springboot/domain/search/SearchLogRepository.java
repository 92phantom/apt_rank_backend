package com.apt_rank.springboot.domain.search;

import com.apt_rank.springboot.web.dto.TopRankDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchLogRepository  extends JpaRepository<SearchLog, String> {

    @Query(value =
            "SELECT * " +
                    "FROM apt_search_log asl1 " +
                    "WHERE asl1.client_ip = ?1 " +
                    "   AND asl1.port = ?2" +
                    "   AND asl1.id = " +
                    "       (SELECT MAX(asl2.id)" +
                    "       FROM    apt_search_log asl2" +
                    "       WHERE   asl1.client_ip  = asl2.client_ip" +
                    "       AND     asl1.port       = asl2.port)", nativeQuery = true)
    SearchLog findByIp_Port(String client_id, String port);

    @Query(value =
    "SELECT atpd.apt_name, main_q.exclusive_area, main_q.serial_num, main_q.rank, arcd.province_nm, arcd.city_nm, arcd.dong_nm, MAX(atpd.trans_price) as max_trans_price\n" +
            "FROM (\n" +
            "SELECT serial_num, exclusive_area, count(1),@ROWNUM := @ROWNUM +1 AS rank \n" +
            "FROM apt_search_log, (SELECT @ROWNUM := 0) tmp \n" +
            "WHERE audit_dtm BETWEEN DATE_ADD(NOW(), INTERVAL -7 DAY) AND NOW() \n" +
            "GROUP BY serial_num, exclusive_area ORDER BY 3 desc\n" +
            ") main_q, apt_trans_price_dtl atpd, apt_region_cd_dtl arcd\n" +
            "WHERE main_q.rank <= 10\n" +
            "\tAND main_q.serial_num = atpd.serial_num\n" +
            "\tAND main_q.exclusive_area = ROUND(atpd.apt_capacity)\n" +
            "\tAND atpd.addr_pr_cd = arcd.province_cd\n" +
            "\tAND atpd.addr_ct_cd = arcd.city_cd\n" +
            "\tAND atpd.addr_dong_cd = arcd.dong_cd\n" +
            "GROUP BY atpd.serial_num\n", nativeQuery = true)
    List<TopRankDto> findByTopRank(int queryRank);

}
