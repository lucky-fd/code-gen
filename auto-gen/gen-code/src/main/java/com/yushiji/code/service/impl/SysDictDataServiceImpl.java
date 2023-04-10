package com.yushiji.code.service.impl;


import com.yushiji.code.mapper.SysDictDataMapper;
import com.yushiji.code.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 字典 业务层处理
 *
 * @author lucky_fd
 * @since 2020.6.8
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {
    @Autowired
    private SysDictDataMapper dictDataMapper;



}
