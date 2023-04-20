package com.yushi.code.mapper;


import com.yushi.code.domain.SysDictData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author lucky_fd
 * @since 2020.6.8
 */
@Mapper
public interface SysDictDataMapper
{
    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataByType(String dictType);

}
