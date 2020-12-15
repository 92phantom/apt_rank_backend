package com.apt_rank.springboot.domain.search;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name="apt_search_log")
public class SearchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String  client_ip;

    @Column(nullable = false)
    private String  port;

    @Column(nullable = false)
    private String  serial_num;

    @Column(nullable = false)
    private int     exclusive_area;

    @Column(nullable = false)
    private Date    audit_dtm;

    @Builder
    public SearchLog(
            String client_ip
            , String port
            , String serial_num
            , int exclusive_area
            , Date audit_dtm)
    {
        this.client_ip = client_ip;
        this.port = port;
        this.serial_num = serial_num;
        this.exclusive_area = exclusive_area;
        this.audit_dtm = new Date(System.currentTimeMillis());
    }
}
