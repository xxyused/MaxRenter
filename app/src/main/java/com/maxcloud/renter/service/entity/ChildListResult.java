package com.maxcloud.renter.service.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：服务器返回的小孩信息。
 * 作    者：向晓阳
 * 时    间：2016/3/3
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class ChildListResult extends BaseResult {
    @SerializedName("data")
    @Expose
    private List<ChildInfoResult> childList = new ArrayList<>();

    /**
     * @return The childList
     */
    public List<ChildInfoResult> getChildList() {
        return childList;
    }

    /**
     * @param childList The childList
     */
    public void setChildList(List<ChildInfoResult> childList) {
        this.childList = childList;
    }
}
