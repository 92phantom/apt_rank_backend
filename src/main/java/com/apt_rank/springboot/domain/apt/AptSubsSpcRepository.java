package com.apt_rank.springboot.domain.apt;

import com.apt_rank.springboot.domain.apt.projection.ExclusiveInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AptSubsSpcRepository extends JpaRepository<AptSubsSpc, String> {

    @Query(value = "SELECT * FROM apt_subs_spc WHERE inqirepd = ?1", nativeQuery = true)
    List<AptSubsSpc> findScheduleByInqirepd(String inqirepd);

    @Query(value = "SELECT * FROM apt_subs_spc WHERE inqirepd = ?1 AND rcept_se = ?2", nativeQuery = true)
    List<AptSubsSpc> findScheduleByInqirepdAndRcept(String inqirepd, String rcept_se);

}
