package com.maxcloud.renter.entity.user;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MAX-XXY on 2016/3/2.
 */
public class MsgContent {
    public interface IOnResend {
        void onResend(MsgContent notifyView);
    }

    public static MsgLabel[] parseContent(String content) {
        if (TextUtils.isEmpty(content)) {
            return new MsgLabel[0];
        }
        List<MsgLabel> items = new ArrayList<>();

        StringBuilder strBuilder = new StringBuilder();
        StringBuilder jsonBuilder = new StringBuilder();

        boolean isChar = false;
        for (char c : content.toCharArray()) {
            switch (c) {
                case '\\':
                    if (isChar) {
                        strBuilder.append(c);
                        isChar = false;
                    } else {
                        isChar = true;
                    }
                    break;
                case '{':
                    if (isChar) {
                        if (jsonBuilder.length() > 0) {
                            jsonBuilder.append(c);
                        } else {
                            strBuilder.append(c);
                        }
                    } else {
                        jsonBuilder.append(c);
                    }
                    isChar = false;
                    break;
                case '}':
                    if (isChar) {
                        if (jsonBuilder.length() > 0) {
                            jsonBuilder.append(c);
                        } else {
                            strBuilder.append(c);
                        }
                    } else {
                        jsonBuilder.append(c);
                        String jsonStr = jsonBuilder.toString();
                        jsonBuilder = new StringBuilder();

                        MsgLabel viewItem = MsgLabel.fromJson(jsonStr);
                        if (strBuilder.length() > 0) {
                            items.add(new MsgLabel(strBuilder.toString()));
                            strBuilder.delete(0, strBuilder.length());
                        }
                        items.add(viewItem);
                    }
                    isChar = false;
                    break;
                default:
                    if (jsonBuilder.length() > 0) {
                        jsonBuilder.append(c);
                    } else {
                        strBuilder.append(c);
                    }
                    isChar = false;
                    break;
            }
        }

        if (strBuilder.length() > 0) {
            items.add(new MsgLabel(strBuilder.toString()));
        }
        MsgLabel viewItems[] = new MsgLabel[items.size()];
        items.toArray(viewItems);

        return viewItems;
    }

    private long mLocalId;
    private int mType;
    private long mId;
    private Date mCreateTime;
    private String mFromAccount;
    private String mFromName;
    private String mToAccount;
    private String mToName;
    private boolean mIsExpired;
    private String mData;
    private String[] mDatas;
    private Date mReceiveTime;
    private Boolean mIsSucceed;
    private boolean _isMyselfSend;

    public long getLocalId() {
        return mLocalId;
    }

    public MsgContent setLocalId(long localId) {
        mLocalId = localId;
        return this;
    }

    public int getType() {
        return mType;
    }

    public MsgContent setType(int type) {
        mType = type;
        return this;
    }

    public long getId() {
        return mId;
    }

    public MsgContent setId(long id) {
        mId = id;
        return this;
    }

    public Date getCreateTime() {
        return mCreateTime;
    }

    public MsgContent setCreateTime(Date createTime) {
        mCreateTime = createTime;
        return this;
    }

    public String getFromName() {
        return mFromName;
    }

    public MsgContent setFromName(String fromName) {
        mFromName = fromName;
        return this;
    }

    /**
     * 获取发送账号。
     *
     * @return
     */
    public String getFromAccount() {
        return mFromAccount;
    }

    public MsgContent setFromAccount(String fromAccount) {
        mFromAccount = fromAccount;
        return this;
    }

    public String getToName() {
        return mToName;
    }

    public MsgContent setToName(String toName) {
        mToName = toName;
        return this;
    }

    /**
     * 获取接收账号。
     *
     * @return
     */
    public String getToAccount() {
        return mToAccount;
    }

    public MsgContent setToAccount(String toAccount) {
        mToAccount = toAccount;
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
    public MsgContent setContent(String content) {
        mData = content;
        return this;
    }

    /**
     * 获取接收时间。
     *
     * @return
     */
    public Date getReceiveTime() {
        return mReceiveTime;
    }

    /**
     * 获取是否发送成功。
     *
     * @return
     */
    public boolean isSucceed() {
        return null == mIsSucceed ? false : mIsSucceed;
    }

    /**
     * 设置是否发送成功。
     *
     * @param isSucceed
     * @return
     */
    public MsgContent setSucceed(boolean isSucceed) {
        mIsSucceed = isSucceed;
        return this;
    }

    public boolean isMyselfSend() {
        return _isMyselfSend;
    }

    public MsgContent setMyselfSend(boolean myselfSend) {
        _isMyselfSend = myselfSend;
        return this;
    }

    private MsgLabel[] mItems;

    public MsgLabel[] getmItems() {
        return mItems;
    }

    public MsgContent() {
    }
}
