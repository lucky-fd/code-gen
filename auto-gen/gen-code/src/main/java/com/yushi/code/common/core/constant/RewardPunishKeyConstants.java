package com.yushi.code.common.core.constant;

/**
 * cierp
 * 奖罚key常量
 *
 * @author lucky_fd
 * @since 2022-11-30
 */

public class RewardPunishKeyConstants {

    /*老供应商采购接单奖惩key*/
    public static final String KEY_REWARD_PUNISH_PURCHASE_ORDER_NO_ENTRY = "production_purchase_order_no";

    /*新供应商采购接单奖惩key*/
    public static final String KEY_REWARD_PUNISH_PURCHASE_ORDER_YES_ENTRY = "production_purchase_order_yes";

    /* 采购合同提交 */
    public static final String KEY_REWARD_PUNISH_PURCHASE_CONTRACT_SUBMIT = "production_purchase_contract_submit";

    /*通用流程审核*/
    public static final String KEY_REWARD_PUNISH_PROCESS_AUDIT_COMMON = "process_audit_common";

    /*流程审核驳回扣款*/
    public static final String KEY_PROCESS_AUDIT_REJECT_COMMON = "process_audit_reject_common";

    /*采购接单奖惩key*/
    public static final String KEY_REWARD_PUNISH_PURCHASE_ORDER_ENTRY = "production_purchase_order";

    /*申购策略*/
    public static final String KEY_SUBSCRIBE_START_STRATEGY = "subscribe_start_strategy";

    /*采购策略报价*/
    public static final String KEY_SUBSCRIBE_OFFER_BID = "subscribe_offer_bid";

    /*工作计划*/
    public static final String KEY_DAILY_WORK_PLAN = "daily_work_plan";

    /*策略人确定采购人*/
    public static final String KEY_SUBSCRIBE_CHOOSE_BUYER = "subscribe_choose_buyer";

    /*回退到策略中*/
    public static final String KEY_SUBSCRIBE_BACK_STRATETY = "subscribe_back_stratety";

    /**
     * 产品审批相关
     */
    public static final String KEY_PRODUCT_COMPLETE_REJECT = "product_complete_reject";

    /**
     * 入库流程审核相关
     */
    public static final String KEY_MATERIALS_WARE_PROCESS = "materials_ware_process";

    public static final String KEY_MATERIALS_WARE_PROCESS_REJECT="materials_ware_process_reject";
    /**
     *
     */
    public static final String KEY_SUPPLIER_FLOW_REWARD = "supplier_flow_reward";

    /*供应商准时提交奖惩*/
    public static final String KEY_SUPPLIER_SUBMIT = "production_supplier_submit";

    /**
     * 质检
     */
    //接单
    public static final String KEY_QUALITY_ADD_COMMON="quality_add_common";

    //完成质检
    public static final String KEY_QUALITY_FINISH_COMMON="quality_finish_common";



    /*流程提交一次性通过奖励*/
    public static final String KEY_PROCESS_AUDIT_ALL_PASS_COMMON = "process_audit_all_pass_common";

    /*财务费用报销驳回*/
    public static final String KEY_EXPENSE_REJECT_ATT = "finance_expense_account_reject_att";
    public static final String KEY_EXPENSE_REJECT_BUSINESS = "finance_expense_account_reject_business";

    /*财务费用报销出纳审核*/
    public static final String KEY_EXPENSE_AUDIT_CASHIER = "finance_expense_account_audit_cashier";

    /**财务费用报销流程审批*/
    public static final String KEY_EXPENSE_PROCESS_AUDIT = "finance_expense_account_process_audit";

    /**研发项目管理奖惩*/
    public static final String KEY_DEVELOP_MANAGE_TASK_RECORD = "develop_manage_task_record";

    /**研发项目管理检查奖惩*/
    public static final String KEY_DEVELOP_MANAGE_TASK_RECORD_CHECK = "develop_manage_task_record_check";
}
