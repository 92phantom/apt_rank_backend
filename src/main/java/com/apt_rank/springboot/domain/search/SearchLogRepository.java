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
            "    SELECT\n" +
                    "        atpd.apt_name,\n" +
                    "        main_q.exclusive_area,\n" +
                    "        main_q.pr_cd,\n" +
                    "        main_q.ct_cd,\n" +
                    "        main_q.dong_cd,\n" +
                    "        main_q.addr_cd,\n" +
                    "        main_q.rank,\n" +
                    "        arcd.province_nm,\n" +
                    "        arcd.city_nm,\n" +
                    "        arcd.dong_nm,\n" +
                    "        MAX(atpd.trans_price) as max_trans_price \n" +
                    "    FROM\n" +
                    "        (  SELECT\n" +
                    "            @ROWNUM \\:= @ROWNUM +1 AS rank ,\n" +
                    "            t.*  \n" +
                    "        FROM\n" +
                    "            (   SELECT\n" +
                    "                exclusive_area,\n" +
                    "                pr_cd,\n" +
                    "                ct_cd,\n" +
                    "                dong_cd,\n" +
                    "                addr_cd,\n" +
                    "                count(1)   \n" +
                    "            FROM\n" +
                    "                apt_search_log   \n" +
                    "            WHERE\n" +
                    "                audit_dtm BETWEEN DATE_ADD(NOW(), INTERVAL -7 DAY) AND NOW()    \n" +
                    "            GROUP BY\n" +
                    "                serial_num,\n" +
                    "                exclusive_area \n" +
                    "            ORDER BY\n" +
                    "                count(1) desc  )t,\n" +
                    "            (SELECT\n" +
                    "                @ROWNUM \\:= 0) tmp  ) main_q,\n" +
                    "            apt_trans_price_dtl atpd,\n" +
                    "            apt_region_cd_dtl arcd \n" +
                    "        WHERE\n" +
                    "            main_q.rank <= ?1\n" +
                    "            AND main_q.pr_cd = atpd.addr_pr_cd\n" +
                    "            AND main_q.ct_cd = atpd.addr_ct_cd\n" +
                    "            AND main_q.dong_cd = atpd.addr_dong_cd\n" +
                    "            AND main_q.addr_cd = atpd.addr_cd\n" +
                    "            AND main_q.exclusive_area = ROUND(atpd.apt_capacity)  \n" +
                    "            AND atpd.addr_pr_cd = arcd.province_cd  \n" +
                    "            AND atpd.addr_ct_cd = arcd.city_cd  \n" +
                    "            AND atpd.addr_dong_cd = arcd.dong_cd \n" +
                    "        GROUP BY\n" +
                    "            main_q.pr_cd,\n" +
                    "            main_q.ct_cd,\n" +
                    "            main_q.dong_cd,\n" +
                    "            main_q.addr_cd,\n" +
                    "            ROUND(atpd.apt_capacity)  \n" +
                    "        ORDER BY\n" +
                    "            main_q.rank", nativeQuery = true)
    List<TopRankInterface> findByTopRank(int topRank);

}
