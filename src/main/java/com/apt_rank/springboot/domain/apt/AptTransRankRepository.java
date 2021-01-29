package com.apt_rank.springboot.domain.apt;

import com.apt_rank.springboot.domain.apt.projection.TransAmountRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AptTransRankRepository  extends JpaRepository<AptTransPriceHst, String> {

    @Query(value ="SELECT atpd.apt_name, atpd.addr_pr_cd, atpd.addr_ct_cd, atpd.addr_dong_cd, atpd.addr_cd, atpd.serial_num, count(1) as trans_amount \n" +
            "\t, arcd.province_nm, arcd.city_nm, arcd.dong_nm, MAX(atpd.trans_price) as max_trans_price\n" +
            "FROM apt_trans_price_dtl atpd use index(primary, trans_amount_2), apt_region_cd_dtl arcd use index(primary)\n" +
            "WHERE atpd.trans_yymm  >= ?1\n" +
            "\tand atpd.addr_pr_cd = arcd.province_cd\n" +
            "    and atpd.addr_ct_cd = arcd.city_cd\n" +
            "    and atpd.addr_dong_cd = arcd.dong_cd \n" +
            "GROUP BY atpd.addr_pr_cd, atpd.addr_ct_cd, atpd.addr_dong_cd, atpd.addr_cd", nativeQuery = true)
    List<TransAmountRank> findAllTransAmountRank(String start_dt);

    @Query(value ="SELECT atpd.apt_name, atpd.addr_pr_cd, atpd.addr_ct_cd, atpd.addr_dong_cd, atpd.addr_cd, atpd.serial_num, count(atpd.serial_num) as trans_amount \n" +
            "\t, arcd.province_nm, arcd.city_nm, arcd.dong_nm, MAX(atpd.trans_price) as max_trans_price\n" +
            "FROM apt_trans_price_dtl atpd use index(primary, trans_amount_2), apt_region_cd_dtl arcd use index(primary)\n" +
            "WHERE atpd.trans_yymm  >= ?1 \n" +
            "\tand atpd.addr_pr_cd = arcd.province_cd\n" +
            "    and atpd.addr_ct_cd = arcd.city_cd\n" +
            "    and atpd.addr_dong_cd = arcd.dong_cd \n" +
            "    and atpd.addr_pr_cd = ?2\n"+
            "GROUP BY atpd.addr_pr_cd, atpd.addr_ct_cd, atpd.addr_dong_cd, atpd.addr_cd", nativeQuery = true)
    List<TransAmountRank> findLocalTransAmountRank(String start_dt, String pr_cd);


}
