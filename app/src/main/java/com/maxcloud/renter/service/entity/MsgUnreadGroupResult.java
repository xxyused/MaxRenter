package com.maxcloud.renter.service.entity;

import android.support.annotation.IntDef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

/**
 * 描    述：消息分组对象。
 * 作    者：向晓阳
 * 时    间：2016/3/2
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class MsgUnreadGroupResult extends BaseResult {
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

    @SerializedName("notifycount")
    @Expose
    private int notifycount;
    @SerializedName("messagetype")
    @Expose
    private String messagetype;
    @SerializedName("sendtype")
    @Expose
    private int sendtype;
    @SerializedName("senderaccount")
    @Expose
    private String account;
    @SerializedName("sendername")
    @Expose
    private String name;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("time")
    @Expose
    private Date time;

    /**
     * @return The notifycount
     */
    public int getNotifycount() {
        return notifycount;
    }

    /**
     * @param notifycount The notifycount
     */
    public void setNotifycount(int notifycount) {
        this.notifycount = notifycount;
    }

    /**
     * @return The messagetype
     */
    public String getMessagetype() {
        return messagetype;
    }

    /**
     * @param messagetype The messagetype
     */
    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    /**
     * @return The sendtype
     */
    public int getSendtype() {
        return sendtype;
    }

    /**
     * @param sendtype The sendtype
     */
    public void setSendtype(@AccountTypeEnum int sendtype) {
        this.sendtype = sendtype;
    }

    /**
     * @return The account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account The account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return The time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time The time
     */
    public void setTime(Date time) {
        this.time = time;
    }
}
