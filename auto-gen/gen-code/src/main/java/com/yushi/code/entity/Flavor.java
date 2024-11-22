package com.yushi.code.entity;

import com.yushi.code.east.annotation.TableColumn;
import com.yushi.code.east.annotation.TableId;
import com.yushi.code.east.annotation.TableInfo;

/**
 * @author fdong
 * @since 2024.11.22
 */
@TableInfo(name = "flavor", comment = "口味管理")
public class Flavor {

    /** 主键 */
    @TableId
    @TableColumn(comment = "主键")
    private Long id;

    /** 口味名称 */
    @TableColumn(comment = "口味名称", nullable = false)
    private String name;

    /** 排序 */
    @TableColumn(comment = "排序", defaultValue = "1")
    private Integer sort;

    /** 禁用状态 */
    @TableColumn(comment = "禁用状态", defaultValue = "false")
    private Boolean disabled;

    /** 关联分类id */
    @TableColumn(comment = "关联分类id", nullable = false)
    private Long categoryId;

    /** 口味类型 */
    @TableColumn(comment = "口味类型", nullable = false)
    private String type;

    /** 描述 */
    @TableColumn(comment = "描述")
    private String description;

    /** 创建人ID */
    @TableColumn(comment = "创建人ID")
    private Long createAccountId;

    /** 操作人ID */
    @TableColumn(comment = "操作人ID")
    private Long updateAccountId;
}
