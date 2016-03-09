package com.maxcloud.renter.entity.user;

import java.util.Date;

/**
 * 描    述：消息分组对象。
 * 作    者：向晓阳
 * 时    间：2016/3/2
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class MsgGroup {
    private int mCount;
    private String mAccount;
    private String mName;
    private String mContent;
    private Date mSendTime;

    public int getCount() {
        return mCount;
    }

    public String getAccount() {
        return mAccount;
    }

    public String getName() {
        return mName;
    }

    public String getContent() {
        return mContent;
    }

    public Date getSendTime() {
        return mSendTime;
    }

    public MsgGroup(int count, String account, String name, String content, Date sendTime) {
        mCount = count;
        mAccount = account;
        mName = name;
        mContent = content;
        mSendTime = sendTime;
    }

    public void fill(MsgGroup msgGroup) {
        mCount = msgGroup.getCount();
        mAccount = msgGroup.getAccount();
        mName = msgGroup.getName();
        mContent = msgGroup.getContent();
        mSendTime = msgGroup.getSendTime();
    }

    public void merge(MsgGroup msgGroup) {
        mCount += msgGroup.getCount();
        if (msgGroup.getSendTime().after(mSendTime)) {
            mAccount = msgGroup.getAccount();
            mName = msgGroup.getName();
            mSendTime = msgGroup.getSendTime();
            mContent = msgGroup.getContent();
        }
    }
}
