package com.yushi.code.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
/*
    sys_register_agreement_status
 */
public enum EmAuditAgreementStatus {
    AGREE_ED("1", "已同意"),

    REFUSE_ED("2", "领导待审批"),

    AUDIT_LEADER("0", "已拒绝"),
    AUDIT_WAIT("3", "待审批");


    private final String code;
    private final String value;

    public static String getValueByCode(String code) {
        for (EmAuditAgreementStatus type : values()) {
            if (type.getCode().equals(code)) {
                return type.getValue();
            }
        }
        return null;
    }
}
