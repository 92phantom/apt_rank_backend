package com.apt_rank.springboot.domain.search;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

}
