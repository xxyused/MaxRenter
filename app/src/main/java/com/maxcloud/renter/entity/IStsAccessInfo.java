package com.maxcloud.renter.entity;

public interface IStsAccessInfo {
    /**
     * 获取OSS临时AccessKey。
     *
     * @return
     */
    String getAccessKey();

    /**
     * 获取OSS临时AccessSecret。
     *
     * @return
     */
    String getAccessSecret();

    /**
     * 获取OSS临时AccesToken。
     *
     * @return
     */
    String getAccessToken();
}
