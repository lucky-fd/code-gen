package com.yushiji.code.common.core.web.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class SelectVo {
    private String label;

    private String key;

    private String value;

    private boolean active;

    private String avatar;

    //禁用
    private Boolean disabled;

    private Object group;

    private List<Object> options;
}
