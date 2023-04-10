package com.yushiji.code.mapper;


import com.yushiji.code.domain.SysDictType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author lucky_fd
 * @since 2020.6.8
 */
@Mapper
public interface SysDictTypeMapper
{
    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    public List<SysDictType> selectDictTypeAll();

}
