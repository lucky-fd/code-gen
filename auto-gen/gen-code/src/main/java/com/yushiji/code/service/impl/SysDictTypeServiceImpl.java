package com.yushiji.code.service.impl;


import com.yushiji.code.domain.SysDictData;
import com.yushiji.code.domain.SysDictType;

import com.yushiji.code.mapper.SysDictDataMapper;
import com.yushiji.code.mapper.SysDictTypeMapper;
import com.yushiji.code.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author lucky_fd
 * @since 2020.6.8
 */
@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService {
    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {

        return dictDataMapper.selectDictDataByType(dictType);
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeAll() {
        return dictTypeMapper.selectDictTypeAll();
    }


}
