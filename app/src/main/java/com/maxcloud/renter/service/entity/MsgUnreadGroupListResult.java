package com.maxcloud.renter.service.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：服务器返回的未读消息组列表。
 * 作    者：向晓阳
 * 时    间：2016/3/8
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class MsgUnreadGroupListResult extends BaseResult {
    @SerializedName("data")
    @Expose
    private List<MsgUnreadGroupResult> data = new ArrayList<>();

    /**
     * @return The data
     */
    public List<MsgUnreadGroupResult> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<MsgUnreadGroupResult> data) {
        this.data = data;
    }
}
