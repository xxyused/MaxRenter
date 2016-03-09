package com.maxcloud.renter.service.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MAX-XXY on 2016/3/2.
 */
public class StsInfoResult extends BaseResult {
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("secret")
    @Expose
    private String secret;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("endpoint")
    @Expose
    private String endpoint;
    @SerializedName("bucket")
    @Expose
    private String bucket;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("lastTime")
    @Expose
    private String lastTime;

    /**
     * @return The key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return The secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @param secret The secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return The endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * @param endpoint The endpoint
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * @return The bucket
     */
    public String getBucket() {
        return bucket;
    }

    /**
     * @param bucket The bucket
     */
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /**
     * @return The path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path The path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return The lastTime
     */
    public String getLastTime() {
        return lastTime;
    }

    /**
     * @param lastTime The lastTime
     */
    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
