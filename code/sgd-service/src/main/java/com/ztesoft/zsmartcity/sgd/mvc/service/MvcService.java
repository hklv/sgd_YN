package com.ztesoft.zsmartcity.sgd.mvc.service;

import com.ztesoft.zsmartcity.sgd.mvc.domain.Demo;
import com.ztesoft.zsmartcity.sgd.mvc.repository.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/6/30 下午12:28
 */
@Service
public class MvcService {
    @Autowired
    private DemoRepository demoRepository;

    public Demo select(Long id) {
        return demoRepository.load(id);
    }

    @Transactional
    public int insert(Demo t) {
        return demoRepository.save(t);
    }

    @Transactional
    public int insertList(List<Demo> demos) {
        return demoRepository.batchSave(demos);
    }

    @Transactional
    public int update(Demo entity) {
        return demoRepository.update(entity);
    }

    @Transactional
    public int delete(Long id) {
        return demoRepository.delete(id);
    }

    public Page<Demo> list(Pageable pageable) {
        return demoRepository.list(pageable);
    }
}
