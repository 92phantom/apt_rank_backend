package com.apt_rank.springboot.domain.apt;

import com.apt_rank.springboot.domain.apt.projection.ExclusiveInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AptSubsSpcRepository extends JpaRepository<AptSubsSpc, String> {

    List<AptSubsSpc> findScheduleByInqirePd(String inqirePd);

}
