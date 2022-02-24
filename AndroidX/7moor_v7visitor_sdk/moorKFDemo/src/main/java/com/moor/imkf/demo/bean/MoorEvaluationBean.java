package com.moor.imkf.demo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/05/31
 *     @desc   : 满意度配置bean
 *     @version: 1.0
 * </pre>
 */
public class MoorEvaluationBean implements Serializable {

    private ArrayList<RadioTagTextBean> radioTagText;
    /**
     * 账户id
     */
    private String account;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建用户
     */
    private String createUser;
    /**
     * 创建时间戳
     */
    private long createTimestamp;
    /**
     * 更新用户
     */
    private String updateUser;
    /**
     * 更新时间戳
     */
    private long updateTimestamp;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 模板名称
     */
    private String name;
    /**
     * 是否为默认模板
     */
    private Boolean isDefault;
    /**
     * 模板类型 按钮模板:radioTagText
     */
    private String type;
    /**
     * 评价邀请文案
     */
    private String satisfyTitle;
    /**
     * 评价感谢文案
     */
    private String satisfyThank;
//    /**
//     * 满意度时效开关
//     */
//    private Boolean isAging;
//    /**
//     * 满意度时效时长 单位小时
//     */
//    private Integer agingTimeout;
//    /**
//     * 系统自动结束会话推送开关
//     */
//    private Boolean isSystemClosePushCsr;
//    /**
//     * 坐席结束会话推送开关
//     */
//    private Boolean isAgentClosePushCsr;
//    /**
//     * 访客结束会话展示开关
//     */
//    private Boolean isCustomerClosePushCsr;
//    /**
//     * 坐席主动邀请评价推送开关
//     */
//    private Boolean isAgentPushCsr;
//    /**
//     * 访客主动评价展示开关
//     */
//    private Boolean isCustomerPushCsr;


    public ArrayList<RadioTagTextBean> getRadioTagText() {
        return radioTagText;
    }

    public MoorEvaluationBean setRadioTagText(ArrayList<RadioTagTextBean> radioTagText) {
        this.radioTagText = radioTagText;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public MoorEvaluationBean setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public MoorEvaluationBean setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public MoorEvaluationBean setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public MoorEvaluationBean setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public MoorEvaluationBean setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public MoorEvaluationBean setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public MoorEvaluationBean setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getName() {
        return name;
    }

    public MoorEvaluationBean setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public MoorEvaluationBean setDefault(Boolean aDefault) {
        isDefault = aDefault;
        return this;
    }

    public String getType() {
        return type;
    }

    public MoorEvaluationBean setType(String type) {
        this.type = type;
        return this;
    }

    public String getSatisfyTitle() {
        return satisfyTitle;
    }

    public MoorEvaluationBean setSatisfyTitle(String satisfyTitle) {
        this.satisfyTitle = satisfyTitle;
        return this;
    }

    public String getSatisfyThank() {
        return satisfyThank;
    }

    public MoorEvaluationBean setSatisfyThank(String satisfyThank) {
        this.satisfyThank = satisfyThank;
        return this;
    }

//    public Boolean getAging() {
//        return isAging;
//    }
//
//    public MoorEvaluationBean setAging(Boolean aging) {
//        isAging = aging;
//        return this;
//    }
//
//    public Integer getAgingTimeout() {
//        return agingTimeout;
//    }
//
//    public MoorEvaluationBean setAgingTimeout(Integer agingTimeout) {
//        this.agingTimeout = agingTimeout;
//        return this;
//    }
//
//    public Boolean getSystemClosePushCsr() {
//        return isSystemClosePushCsr;
//    }
//
//    public MoorEvaluationBean setSystemClosePushCsr(Boolean systemClosePushCsr) {
//        isSystemClosePushCsr = systemClosePushCsr;
//        return this;
//    }
//
//    public Boolean getAgentClosePushCsr() {
//        return isAgentClosePushCsr;
//    }
//
//    public MoorEvaluationBean setAgentClosePushCsr(Boolean agentClosePushCsr) {
//        isAgentClosePushCsr = agentClosePushCsr;
//        return this;
//    }
//
//    public Boolean getCustomerClosePushCsr() {
//        return isCustomerClosePushCsr;
//    }
//
//    public MoorEvaluationBean setCustomerClosePushCsr(Boolean customerClosePushCsr) {
//        isCustomerClosePushCsr = customerClosePushCsr;
//        return this;
//    }
//
//    public Boolean getAgentPushCsr() {
//        return isAgentPushCsr;
//    }
//
//    public MoorEvaluationBean setAgentPushCsr(Boolean agentPushCsr) {
//        isAgentPushCsr = agentPushCsr;
//        return this;
//    }
//
//    public Boolean getCustomerPushCsr() {
//        return isCustomerPushCsr;
//    }
//
//    public MoorEvaluationBean setCustomerPushCsr(Boolean customerPushCsr) {
//        isCustomerPushCsr = customerPushCsr;
//        return this;
//    }

    public static class RadioTagTextBean implements Serializable{
        private String key;
        private String name;
        private List<String> reason;
        private String proposalStatus;
        private Integer defaultIndex;
        private Boolean isUse;
        private boolean isSlected;

        public boolean isSlected() {
            return isSlected;
        }

        public void setSlected(boolean slected) {
            isSlected = slected;
        }

        public String getKey() {
            return key;
        }

        public RadioTagTextBean setKey(String key) {
            this.key = key;
            return this;
        }

        public String getName() {
            return name;
        }

        public RadioTagTextBean setName(String name) {
            this.name = name;
            return this;
        }

        public List<String> getReason() {
            return reason;
        }

        public RadioTagTextBean setReason(List<String> reason) {
            this.reason = reason;
            return this;
        }

        public String getProposalStatus() {
            return proposalStatus;
        }

        public RadioTagTextBean setProposalStatus(String proposalStatus) {
            this.proposalStatus = proposalStatus;
            return this;
        }

        public Integer getDefaultIndex() {
            return defaultIndex;
        }

        public RadioTagTextBean setDefaultIndex(Integer defaultIndex) {
            this.defaultIndex = defaultIndex;
            return this;
        }

        public Boolean getUse() {
            return isUse;
        }

        public RadioTagTextBean setUse(Boolean use) {
            isUse = use;
            return this;
        }
    }
}
