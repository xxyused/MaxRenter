package com.maxcloud.renter.entity;

public interface IOssObject {
    /**
     * 获取服务器。
     *
     * @return
     */
    String getService();

    /**
     * 获取Bucket。
     *
     * @return
     */
    String getBucket();

    /**
     * 获取OSS临时Path。
     *
     * @return
     */
    String getPath();
}
