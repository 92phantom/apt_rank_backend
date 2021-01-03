package com.apt_rank.springboot.domain.apt;

import com.apt_rank.springboot.domain.apt.projection.AptDetail;
import com.apt_rank.springboot.domain.apt.projection.ExclusiveInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AptRankRepository extends JpaRepository<AptRankSearch, String> {

    // 전용면적 1) 시리얼 번호 존재
    @Query(value = "SELECT ROUND(apt_capacity) as exclusive_area\n" +
            "FROM apt_trans_price_dtl\n" +
            "WHERE serial_num = ?1\n" +
            "GROUP BY apt_capacity", nativeQuery = true)
    List<ExclusiveInterface> findExclusiveBySerialNum(String serial_num);

    // 전용면적 2) 시리얼 번호 미존재
    @Query(value = "SELECT ROUND(apt_capacity) as exclusive_area\n" +
            "FROM apt_trans_price_dtl\n" +
            "WHERE addr_pr_cd = ?1\n" +
            "\tAND addr_ct_cd = ?2\n" +
            "\tAND addr_dong_cd = ?3\n" +
            "\tAND addr_cd =  ?4\n" +
            "GROUP BY apt_capacity", nativeQuery = true)
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
            "\tAND atr.op_dt = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -1 DAY), '%Y%m%d')\n", nativeQuery = true)
    List<AptDetail> findWideRankByAddrCd(String pr_cd, String ct_cd, String dong_cd, String addr_cd, int exclusive_area);

    // 지역구 내 랭킹
    @Query(value = "SELECT main_q.*\n" +
            "FROM (\n" +
            "\tSELECT  @ROWNUM := @ROWNUM +1 AS local_rank, t.*\n" +
            "\tFROM (\n" +
            "\t\tSELECT *\n" +
            "\t\tFROM apt_trans_rank atr, (select @ROWNUM := 0 ) tmp\n" +
            "\t\tWHERE op_dt = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -1 DAY), '%Y%m%d')\n" +
            "\t\t\tAND atr.addr_pr_cd = ?1 \n" +
            "\t\t\tAND atr.addr_ct_cd = ?2\n" +
            "\t\t\tAND atr.addr_dong_cd = ?3\n" +
            "\t\tORDER BY atr.unit_price desc\n" +
            "\t\t) t\n" +
            "\t) main_q\n" +
            "WHERE (main_q.local_rank = 1 OR \n" +
            "\t(main_q.addr_cd = ?4 AND main_q.exclusive_area = ?5))", nativeQuery = true)
    List<AptDetail> findLocalRankByAddrCd(String pr_cd, String ct_cd, String dong_cd, String addr_cd, int exclusive_area);

    // 전국 아파트 개수
    @Query(value = "SELECT count(1) as wide_apt_cnt\n" +
            "FROM apt_trans_rank\n" +
            "WHERE op_dt = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -1 DAY), '%Y%m%d')", nativeQuery = true)
    AptDetail findWideAptCount();

    // 지역 내 아파트 개수
    @Query(value = "SELECT count(1) as local_apt_cnt\n" +
            "FROM apt_trans_rank\n" +
            "WHERE op_dt = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -1 DAY), '%Y%m%d')\n" +
            "\tAND addr_pr_cd = ?1\n" +
            "\tAND addr_ct_cd = ?2\n" +
            "\tAND addr_dong_cd = ?3\n", nativeQuery = true)
    AptDetail findLocalAptCount(String pr_cd, String ct_cd, String dong_cd);

    // MY apt dtl - 상세 정보
    @Query(value = "SELECT arcd.province_nm, arcd.city_nm, arcd.dong_nm, atpd.apt_build_yy, \n" +
            "\tatpd.apt_floor, atpd.trans_yymmdd, atpd.trans_price\n" +
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

}
