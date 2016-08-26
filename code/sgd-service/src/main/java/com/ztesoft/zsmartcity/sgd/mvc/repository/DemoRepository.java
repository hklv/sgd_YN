package com.ztesoft.zsmartcity.sgd.mvc.repository;

import com.ztesoft.zsmartcity.sgd.mvc.domain.Demo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/6/30 下午4:48
 */
@Repository
public interface DemoRepository {
    Demo load(@Param("id") Long pkId);

    int save(Demo demo);

    int batchSave(List<Demo> demos);

    int update(Demo demo);

    int delete(@Param("id") Long pkId);

    Page<Demo> list(Pageable pageable);
}
