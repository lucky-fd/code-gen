package com.yushiji.code.common.core.web.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * vo基类
 *
 * @author zwh
 */
@Data
public class BaseVo implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 请求参数
     */
    private Map<String, Object> params = new HashMap<>();



}
