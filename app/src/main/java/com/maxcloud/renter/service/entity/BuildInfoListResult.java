package com.maxcloud.renter.service.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：服务器返回的楼栋列表。
 * 作    者：向晓阳
 * 时    间：2016/2/29
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class BuildInfoListResult extends BaseResult {
    @SerializedName("data")
    @Expose
    private List<BuildInfoResult> data = new ArrayList<>();

    /**
     * @return The data
     */
    public List<BuildInfoResult> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<BuildInfoResult> data) {
        this.data = data;
    }
}
