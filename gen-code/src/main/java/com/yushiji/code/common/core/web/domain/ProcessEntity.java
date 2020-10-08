package com.yushiji.code.common.core.web.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProcessEntity extends BaseEntity {
    /**
     * 流程id
     */
    private String processId;
    /**
     * 部署id
     */
    private String deployId;

    /**
     * 待审的流程id
     */
    private List<String> todoList;
}
