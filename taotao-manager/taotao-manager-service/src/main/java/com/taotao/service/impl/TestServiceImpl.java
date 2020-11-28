package com.taotao.service.impl;

import com.taotao.mapper.TestMapper;
import com.taotao.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wwt
 * @date 2019/9/17 11:30
 */

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper mapper;

    @Override
    public String queryNow() {
        return mapper.queryNow();
    }
}
