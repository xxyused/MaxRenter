package com.maxcloud.renter.service;

import com.maxcloud.renter.service.entity.MsgContentResult;
import com.maxcloud.renter.service.entity.MsgUnreadGroupListResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 描    述：消息服务接口。
 * 作    者：向晓阳
 * 时    间：2016/3/8
 * 版    权：迈斯云门禁网络科技有限公司
 */
public interface IMessageService {
    @FormUrlEncoded
    @POST("")
    Call<MsgContentResult> getHistoryMessage(@Field("username") String account);

    @FormUrlEncoded
    @POST("getNotReadedMessageCount")
    Call<MsgUnreadGroupListResult> groupUnreadMessage(@Field("receiverId") String account);

    @FormUrlEncoded
    @POST("resetPassword")
    Call<MsgContentResult> readMessage(@Field("username") String account);
}
