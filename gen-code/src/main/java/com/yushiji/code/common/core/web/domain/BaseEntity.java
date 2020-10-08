package com.yushiji.code.common.core.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yushiji.code.common.core.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity基类
 *
 * @author ruoyi
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    private String searchValue;

    /**
     * 创建者
     */
    @CreateBy
    private String createBy;

    /**
     * 创建时间
     */
    @CreateTime
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    @UpdateBy
    private String updateBy;

    /**
     * 更新时间
     */
    @UpdateTime
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @DelFlag
    private String delFlag;

    /**
     * 归档与取消归档：0为归档，1为取消归档
     */
    @Active
    private Boolean active;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排除的id列表
     */
    private List<String> excludeIds;

    /**
     * 包含id列表
     */
    private List<String> includeIds;


    /**
     * 请求参数
     */
    private Map<String, Object> params = new HashMap<>();



}
