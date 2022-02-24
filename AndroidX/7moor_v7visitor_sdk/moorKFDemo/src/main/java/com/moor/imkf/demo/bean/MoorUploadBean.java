package com.moor.imkf.demo.bean;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/05/26
 *     @desc   : 上传文件
 *     @version: 1.0
 * </pre>
 */
public class MoorUploadBean {
    private String uptoken;
    private String domain;
    private String scope;

    public String getUptoken() {
        return uptoken;
    }

    public MoorUploadBean setUptoken(String uptoken) {
        this.uptoken = uptoken;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public MoorUploadBean setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public MoorUploadBean setScope(String scope) {
        this.scope = scope;
        return this;
    }
}
