package com.maxcloud.renter.entity.oss;

import com.maxcloud.renter.entity.IStsAccessInfo;

public class StsAccessInfo implements IStsAccessInfo {
    private String mAccessKey;
    private String mAccessSecret;
    private String mAccessToken;

    /**
     * 获取OSS临时AccessKey。
     *
     * @return
     */
    public String getAccessKey() {
        return mAccessKey;
    }

    /**
     * @param accessKey
     * @return
     */
    public StsAccessInfo setAccessKey(String accessKey) {
        mAccessKey = accessKey;
        return this;
    }

    /**
     * 获取OSS临时AccessSecret。
     *
     * @return
     */
    public String getAccessSecret() {
        return mAccessSecret;
    }

    /**
     * @param accessSecret
     * @return
     */
    public StsAccessInfo setAccessSecret(String accessSecret) {
        mAccessSecret = accessSecret;
        return this;
    }

    /**
     * 获取OSS临时AccesToken。
     *
     * @return
     */
    public String getAccessToken() {
        return mAccessToken;
    }

    /**
     * @param accessToken
     * @return
     */
    public StsAccessInfo setAccessToken(String accessToken) {
        mAccessToken = accessToken;
        return this;
    }
}
