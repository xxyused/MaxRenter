package com.maxcloud.renter.service.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 描    述：服务器返回的开门信息。
 * 作    者：向晓阳
 * 时    间：2016/2/17
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class OpenDoorResult extends BaseResult {
    @SerializedName("leftOver")
    @Expose
    private int remainCount;

    /**
     * 获取开门剩余次数。
     * @return 开门剩余次数。
     */
    public int getRemainCount(){
        return remainCount;
    }


    public OpenDoorResult setRemainCount(int remainCount){
        this.remainCount=remainCount;
        return this;
    }
}
