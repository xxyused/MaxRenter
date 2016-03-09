package com.maxcloud.renter.service.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 描    述：小孩卡楼栋信息。
 * 作    者：向晓阳
 * 时    间：2016/3/7
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class ChildBuildingResult {
    @SerializedName("name")
    @Expose
    private String name;

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }
}
