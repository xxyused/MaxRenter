package com.maxcloud.renter.service.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 描    述：服务器返回基础类，包括返回码和错误原因。
 * 作    者：向晓阳
 * 时    间：2016/2/24
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class BaseResult {
    @SerializedName("code")
    @Expose
    private int _code;
    @SerializedName("reason")
    @Expose
    private String _error;

    /**
     * @return The code
     */
    public int getCode() {
        return _code;
    }

    /**
     * @param code The code
     */
    public void setCode(int code) {
        this._code = code;
    }

    /**
     * @return The code
     */
    public String getError() {
        return _error;
    }

    /**
     * @param error The code
     */
    public void setError(String error) {
        this._error = error;
    }

    /**
     * 判断是否成功。
     *
     * @return 如果成功，则返回 true ，否则，返回 false 。
     */
    public boolean isSuccess() {
        return _code == 0;
    }
}
