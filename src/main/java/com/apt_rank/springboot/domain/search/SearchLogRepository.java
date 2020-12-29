package com.apt_rank.springboot.domain.search;

import com.apt_rank.springboot.domain.search.projection.TopRankInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchLogRepository  extends JpaRepository<SearchLog, String> {

    @Query(value =
            "SELECT * " +
                    "FROM apt_search_log asl1 " +
                    "WHERE asl1.client_ip = ?1 " +
                    "   AND asl1.port = ?2" +
                    "   AND asl1.serial_num = ?3" +
                    "   AND asl1.exclusive_area = ?4" +
                    "   AND date_format(asl1.audit_dtm,'%Y%m%d')  =  date_format(NOW(),'%Y%m%d')", nativeQuery = true)
    List<SearchLog> findByIp_Port(String client_id, String port, String serial_num, int exclusive_area);

    @Query(value =
            "SELECT atpd.apt_name, main_q.exclusive_area, main_q.serial_num, main_q.rank, arcd.province_nm, arcd.city_nm, arcd.dong_nm, MAX(atpd.trans_price) as max_trans_price\n" +
        "FROM (\n" +
        "\tSELECT @ROWNUM \\:= @ROWNUM +1 AS rank , t.*\n" +
        "\tFROM (\n" +
        "\t\tSELECT serial_num, exclusive_area, count(1)\n" +
        "\t\tFROM apt_search_log\n" +
        "\t\tWHERE audit_dtm BETWEEN DATE_ADD(NOW(), INTERVAL -7 DAY) AND NOW() \n" +
        "\t\tGROUP BY serial_num, exclusive_area ORDER BY count(1) desc\n" +
        "\t)t, (SELECT @ROWNUM \\:= 0) tmp \n" +
        ") main_q, apt_trans_price_dtl atpd, apt_region_cd_dtl arcd\n" +
        "WHERE main_q.rank <= ?1\n" +
        "\tAND main_q.serial_num = atpd.serial_num\n" +
        "\tAND main_q.exclusive_area = ROUND(atpd.apt_capacity)\n" +
        "\tAND atpd.addr_pr_cd = arcd.province_cd\n" +
        "\tAND atpd.addr_ct_cd = arcd.city_cd\n" +
        "\tAND atpd.addr_dong_cd = arcd.dong_cd\n" +
        "GROUP BY atpd.serial_num\n" +
        "ORDER BY main_q.rank", nativeQuery = true)
    List<TopRankInterface> findByTopRank(int topRank);

}
