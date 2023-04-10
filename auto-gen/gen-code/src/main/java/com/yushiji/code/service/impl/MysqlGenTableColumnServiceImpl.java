package com.yushiji.code.service.impl;

import java.util.List;

import com.yushiji.code.common.core.text.Convert;
import com.yushiji.code.service.IGenTableColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yushiji.code.domain.GenTableColumn;
import com.yushiji.code.mapper.mysql.MysqlGenTableColumnMapper;

/**
 * 业务字段 服务层实现
 *
 * @author lucky_fd
 * @since 2020.6.8
 */
public class MysqlGenTableColumnServiceImpl implements IGenTableColumnService
{
	@Autowired
	private MysqlGenTableColumnMapper genTableColumnMapper;

	/**
     * 查询业务字段列表
     * 
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
	@Override
	public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId)
	{
	    return genTableColumnMapper.selectGenTableColumnListByTableId(tableId);
	}
	
    /**
     * 新增业务字段
     * 
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
	@Override
	public int insertGenTableColumn(GenTableColumn genTableColumn)
	{
	    return genTableColumnMapper.insertGenTableColumn(genTableColumn);
	}
	
	/**
     * 修改业务字段
     * 
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
	@Override
	public int updateGenTableColumn(GenTableColumn genTableColumn)
	{
	    return genTableColumnMapper.updateGenTableColumn(genTableColumn);
	}

	/**
     * 删除业务字段对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteGenTableColumnByIds(String ids)
	{
		return genTableColumnMapper.deleteGenTableColumnByIds(Convert.toLongArray(ids));
	}
}