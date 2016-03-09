package com.maxcloud.renter.entity.oss;

import com.maxcloud.renter.entity.IOssObject;

public class OssObject implements IOssObject {
    private String mService;
    private String mBucket;
    private String mPath;

    /**
     * 获取服务器。
     *
     * @return
     */
    public String getService() {
        return mService;
    }

    /**
     * 设置服务器。
     *
     * @param service
     * @return
     */
    public OssObject setService(String service) {
        mService = service;
        return this;
    }

    /**
     * 获取Bucket。
     *
     * @return
     */
    public String getBucket() {
        return mBucket;
    }

    /**
     * 设置Bucket。
     *
     * @param bucket
     * @return
     */
    public OssObject setBucket(String bucket) {
        mBucket = bucket;
        return this;
    }

    /**
     * 获取OSS临时Path。
     *
     * @return
     */
    public String getPath() {
        return mPath;
    }

    /**
     * @param path
     * @return
     */
    public OssObject setPath(String path) {
        mPath = path;
        return this;
    }
}
