package com.maxcloud.renter.service.entity;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

/**
 * Created by MAX-XXY on 2016/3/2.
 */
public class MsgContentResult extends BaseResult {
    /**
     * 未知。
     */
    public static final int TYPE_UNKNOWN = 0;
    /**
     * 账号。
     */
    public static final int TYPE_ACCOUNT = 1;
    /**
     * 系统账号。
     */
    public static final int TYPE_SYSTEM = 2;

    @IntDef({TYPE_UNKNOWN, TYPE_ACCOUNT, TYPE_SYSTEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AccountTypeEnum {
    }

    private int mType;
    private long mId;
    private Date mCreateTime;
    private String mFromId;
    private String mFromName;
    private String mToId;
    private String mToName;
    private boolean mIsExpired;
    private String mData;

    public int getType() {
        return mType;
    }

    public MsgContentResult setType(int type) {
        mType = type;
        return this;
    }

    public long getId() {
        return mId;
    }

    public MsgContentResult setId(long id) {
        mId = id;
        return this;
    }

    public Date getCreateTime() {
        return mCreateTime;
    }

    public MsgContentResult setCreateTime(Date createTime) {
        mCreateTime = createTime;
        return this;
    }

    public String getFromName() {
        return mFromName;
    }

    public MsgContentResult setFromName(String fromName) {
        mFromName = fromName;
        return this;
    }

    /**
     * 获取发送账号。
     *
     * @return
     */
    public String getFromId() {
        return mFromId;
    }

    public MsgContentResult setFromId(String fromId) {
        mFromId = fromId;
        return this;
    }

    public String getToName() {
        return mToName;
    }

    public MsgContentResult setToName(String toName) {
        mToName = toName;
        return this;
    }

    /**
     * 获取接收账号。
     *
     * @return
     */
    public String getToId() {
        return mToId;
    }

    public MsgContentResult setToId(String toId) {
        mToId = toId;
        return this;
    }

    /**
     * 获取是否已过期。
     *
     * @return
     */
    public boolean isExpired() {
        return mIsExpired;
    }

    /**
     * 获取数据。
     *
     * @return
     */
    public String getContent() {
        return mData;
    }

    /**
     * 设置数据。
     */
    public MsgContentResult setContent(String content) {
        mData = content;
        return this;
    }
}
