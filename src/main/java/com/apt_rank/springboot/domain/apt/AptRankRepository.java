package com.apt_rank.springboot.domain.apt;

import com.apt_rank.springboot.domain.apt.projection.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AptRankRepository extends JpaRepository<AptRankSearch, String> {

    // 전용면적 1) 시리얼 번호 존재
    @Query(value = "SELECT ROUND(apt_capacity) as exclusive_area\n" +
            "FROM apt_trans_price_dtl\n" +
            "WHERE serial_num = ?1\n" +
            "GROUP BY ROUND(apt_capacity)", nativeQuery = true)
    List<ExclusiveInterface> findExclusiveBySerialNum(String serial_num);

    // 전용면적 2) 시리얼 번호 미존재
    @Query(value = "SELECT ROUND(apt_capacity) as exclusive_area\n" +
            "FROM apt_trans_price_dtl\n" +
            "WHERE addr_pr_cd = ?1\n" +
            "\tAND addr_ct_cd = ?2\n" +
            "\tAND addr_dong_cd = ?3\n" +
            "\tAND addr_cd =  ?4\n" +
            "GROUP BY  ROUND(apt_capacity)", nativeQuery = true)
    List<ExclusiveInterface> findExclusiveByAddrCd(String pr_cd, String ct_cd, String dong_cd, String addr_cd);

    // ADDR_CD 추출
    @Query(value = "SELECT addr_pr_cd, addr_ct_cd, addr_dong_cd, addr_cd\n" +
            "FROM apt_trans_price_dtl\n" +
            "WHERE serial_num = ?1\n" +
            "GROUP BY addr_pr_cd, addr_ct_cd, addr_dong_cd, addr_cd", nativeQuery = true)
    AptDetail findAddrCdBySerialNum(String serial_num);

    // 전국에서 내 랭킹
    @Query(value = "SELECT *\n" +
            "FROM apt_trans_rank atr\n" +
            "WHERE (atr.rank = 1 OR\n" +
            "\t\t(atr.addr_pr_cd = ?1 \n" +
            "\t\t\tANd atr.addr_ct_cd = ?2\n" +
            "            AND atr.addr_dong_cd = ?3\n" +
            "            ANd atr.addr_cd = ?4\n" +
            "            AND atr.exclusive_area = ?5))\n" +
            "\tAND atr.op_dt = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -2 DAY), '%Y%m%d')\n" +
            " ORDER BY rank ", nativeQuery = true)
    List<AptDetail> findWideRankByAddrCd(String pr_cd, String ct_cd, String dong_cd, String addr_cd, int exclusive_area);

    // 지역구 내 랭킹
    @Query(value = "SELECT main_q.*\n" +
            "FROM (\n" +
            "\tSELECT  @ROWNUM \\:= @ROWNUM +1 AS local_rank, t.*\n" +
            "\tFROM (\n" +
            "\t\tSELECT *\n" +
            "\t\tFROM apt_trans_rank atr, (select @ROWNUM \\:= 0 ) tmp\n" +
            "\t\tWHERE op_dt = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -2 DAY), '%Y%m%d')\n" +
            "\t\t\tAND atr.addr_pr_cd = ?1 \n" +
            "\t\t\tAND atr.addr_ct_cd = ?2\n" +
            "\t\t\tAND atr.addr_dong_cd = ?3\n" +
            "\t\t) t\n" +
            "\t) main_q\n" +
            "WHERE (main_q.local_rank = \"1\" OR \n" +
            "\t(main_q.addr_cd = ?4 AND main_q.exclusive_area = ?5))"+
            "\tORDER BY local_rank", nativeQuery = true)
    List<AptDetail> findLocalRankByAddrCd(String addr_pr_cd, String ct_cd, String dong_cd, String addr_cd, int exclusive_area);

    // 전국 아파트 개수
    @Query(value = "SELECT count(1) as wide_apt_cnt\n" +
            "FROM apt_trans_rank\n" +
            "WHERE op_dt = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -2 DAY), '%Y%m%d')", nativeQuery = true)
    AptDetail findWideAptCount();

    // 지역 내 아파트 개수
    @Query(value = "SELECT count(1) as local_apt_cnt\n" +
            "FROM apt_trans_rank\n" +
            "WHERE op_dt = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -2 DAY), '%Y%m%d')\n" +
            "\tAND addr_pr_cd = ?1\n" +
            "\tAND addr_ct_cd = ?2\n" +
            "\tAND addr_dong_cd = ?3\n", nativeQuery = true)
    AptDetail findLocalAptCount(String pr_cd, String ct_cd, String dong_cd);

    // MY apt dtl - 상세 정보
    @Query(value = "SELECT arcd.province_nm, arcd.city_nm, arcd.dong_nm, atpd.apt_build_yy, \n" +
            "\tatpd.apt_floor, atpd.trans_yymmdd, atpd.trans_price, atpd.apt_name, MAX(atpd.trans_price) as max_trans_price \n" +
            "FROM apt_trans_price_dtl atpd, apt_region_cd_dtl arcd\n" +
            "where atpd.addr_pr_cd = ?1\n" +
            "\tand atpd.addr_ct_cd = ?2\n" +
            "\tand atpd.addr_dong_cd = ?3\n" +
            "\tand atpd.addr_cd = ?4\n" +
            "\tand ROUND(apt_capacity) = ?5\n" +
            "\tand atpd.addr_pr_cd = arcd.province_cd\n" +
            "\tand atpd.addr_ct_cd = arcd.city_cd\n" +
            "\tand atpd.addr_dong_cd = arcd.dong_cd\n" +
            "order by trans_yymmdd ", nativeQuery = true)
    List<AptDetail> findMyAptDtlByAddrCd(String pr_cd, String ct_cd, String dong_cd, String addr_cd, int exclusive_area);


    @Query(value = "select atpd.apt_name, ROUND(atpd.apt_capacity) as exclusive_area, atpd.serial_num,\n" +
            "\t\tarcd.province_nm, arcd.city_nm, arcd.dong_nm, max(trans_price) max_trans_price, \n" +
            "\t\tatpd.addr_pr_cd,\n" +
            "\t\tatpd.addr_ct_cd,\n" +
            "\t\tatpd.addr_dong_cd,\n" +
            "\t\tatpd.addr_cd,\n" +
            "\t\tmax(atpd.unit_price) as max_unit_price,\n" +
            "        min(atpd.unit_price) as min_unit_price\n" +
            "\tfrom apt_trans_price_dtl atpd  use index(primary, trans_amount_2, analysis_price, analysis_trans_amount), apt_region_cd_dtl arcd\n" +
            "\tWHERE atpd.trans_yymm between ?1 and ?2\n" +
            "\t\tand atpd.addr_pr_cd = arcd.province_cd  \n" +
            "\t\tand atpd.addr_ct_cd = arcd.city_cd  \n" +
            "\t\tand atpd.addr_dong_cd = arcd.dong_cd \n" +
            "\tgroup by atpd.addr_pr_cd, atpd.addr_ct_cd, atpd.addr_dong_cd, atpd.addr_cd", nativeQuery = true)
    List<AptSoaring> findAptSoaring(String start_dt, String end_dt);

    // 가격변화량 (전국)

    @Query(value =
            "SELECT trans_yymm, ROUND(AVG(trans_price), 1) AS trans_price\n" +
                    "FROM apt_trans_price_dtl atpd\n" +
                    "WHERE trans_yymm  >= ?1\n" +
                    "GROUP BY trans_yymm ", nativeQuery = true)
    List<PriceChange> findAllTransAmount(String start_dt);


    @Query(value =
            "SELECT trans_yymm, ROUND(AVG(trans_price), 1) AS trans_price\n" +
                    "FROM apt_trans_price_dtl atpd\n" +
                    "WHERE trans_yymm  >= ?1\n"  +
                    "AND atpd.addr_pr_cd = ?2\n" +
                    "GROUP BY addr_pr_cd, trans_yymm ", nativeQuery = true)
    List<PriceChange> findLocalTransAmount(String start_dt, String pr_cd);

    // 거래량 (전국)
    @Query(value =
            "SELECT trans_yymm, count(1) as trans_amount\n" +
                    "FROM apt_trans_price_dtl\n" +
                    "WHERE trans_yymm  >= ?1\n" +
                    "GROUP BY trans_yymm", nativeQuery = true)
    List<VolumeChange> findAllVolumeChange(String start_dt);

    @Query(value =
            "SELECT trans_yymm, count(1) as trans_amount\n" +
                    "FROM apt_trans_price_dtl\n" +
                    "WHERE trans_yymm  >= ?1\n" +
                    "AND addr_pr_cd = ?2\n" +
                    "GROUP BY addr_pr_cd, trans_yymm", nativeQuery = true)
    List<VolumeChange> findLocalVolumeChange(String start_dt, String pr_cd);

    // raking

    @Query(value =
            "select rank_main.*\n" +
                    "from (\n" +
                    "select main_q.*,  @ROWNUM \\:= @ROWNUM +1 AS rank\n" +
                    "from  (\tselect atpd.apt_name, ROUND(atpd.apt_capacity) as exclusive_area, atpd.serial_num,\n" +
                    "\t\tarcd.province_nm, arcd.city_nm, arcd.dong_nm, max(trans_price) as max_trans_price, \n" +
                    "\t\tatpd.addr_pr_cd,\n" +
                    "\t\tatpd.addr_ct_cd,\n" +
                    "\t\tatpd.addr_dong_cd,\n" +
                    "\t\tatpd.addr_cd,\n" +
                    "\t\tatpd.unit_price\n" +
                    "\tfrom apt_trans_price_dtl atpd, apt_region_cd_dtl arcd\n" +
                    "\tWHERE atpd.addr_pr_cd = ?2 \n" +
                    "\t\tand atpd.trans_yymm >= ?1 \n" +
                    "\t\tand ROUND(atpd.apt_capacity) between ?3 and ?4  \n" +
                    "\t\tand atpd.addr_pr_cd = arcd.province_cd  \n" +
                    "\t\tand atpd.addr_ct_cd = arcd.city_cd  \n" +
                    "\t\tand atpd.addr_dong_cd = arcd.dong_cd \n" +
                    "\tgroup by atpd.addr_pr_cd, atpd.addr_ct_cd, atpd.addr_dong_cd, atpd.addr_cd\n" +
                    "\torder by atpd.unit_price desc\n" +
                    "\n" +
                    "    ) main_q, (SELECT @ROWNUM \\:= 0) tmp\n" +
                    ") rank_main\n" +
                    "where rank_main.rank between ?5 and ?6", nativeQuery = true)

    List<AptVolumeRank> findAptRankByPage(String start_dt, String range, String st_exclusive_area, String end_exclusive_area, int st_page, int end_page);



    @Query(value =
            "select rank_main.*\n" +
                    "from (\n" +
                    "select main_q.*,  @ROWNUM \\:= @ROWNUM +1 AS rank\n" +
                    "from  (\tselect atpd.apt_name, ROUND(atpd.apt_capacity) as exclusive_area, atpd.serial_num,\n" +
                    "\t\tarcd.province_nm, arcd.city_nm, arcd.dong_nm, max(trans_price) as max_trans_price, \n" +
                    "\t\tatpd.addr_pr_cd,\n" +
                    "\t\tatpd.addr_ct_cd,\n" +
                    "\t\tatpd.addr_dong_cd,\n" +
                    "\t\tatpd.addr_cd,\n" +
                    "\t\tatpd.unit_price\n" +
                    "\tfrom apt_trans_price_dtl atpd, apt_region_cd_dtl arcd\n" +
                    "\tWHERE atpd.trans_yymm >= ?1 \n" +
                    "\t\tand ROUND(atpd.apt_capacity) between ?2 and ?3 \n" +
                    "\t\tand atpd.addr_pr_cd = arcd.province_cd  \n" +
                    "\t\tand atpd.addr_ct_cd = arcd.city_cd  \n" +
                    "\t\tand atpd.addr_dong_cd = arcd.dong_cd \n" +
                    "\tgroup by atpd.addr_pr_cd, atpd.addr_ct_cd, atpd.addr_dong_cd, atpd.addr_cd\n" +
                    "\torder by atpd.unit_price desc\n" +
                    "\n" +
                    "    ) main_q, (SELECT @ROWNUM \\:= 0) tmp\n" +
                    ") rank_main\n" +
                    "where rank_main.rank between ?4 and ?5", nativeQuery = true)

    List<AptVolumeRank> findAptRankAllByPage(String start_dt, String st_exclusive_area, String end_exclusive_area, int st_page, int end_page);

}
