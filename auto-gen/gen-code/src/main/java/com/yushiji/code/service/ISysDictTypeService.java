package com.yushiji.code.service;


import com.yushiji.code.domain.SysDictData;
import com.yushiji.code.domain.SysDictType;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author lucky_fd
 * @since 2020.6.8
 */
public interface ISysDictTypeService {

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    List<SysDictType> selectDictTypeAll();

}
