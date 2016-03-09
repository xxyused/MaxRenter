package com.maxcloud.renter.service;

import com.maxcloud.renter.service.entity.BaseResult;
import com.maxcloud.renter.service.entity.BuildInfoListResult;
import com.maxcloud.renter.service.entity.CardInfoResult;
import com.maxcloud.renter.service.entity.ChildListResult;
import com.maxcloud.renter.service.entity.OpenDoorResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 描    述：楼栋服务接口。
 * 作    者：向晓阳
 * 时    间：2016/1/29
 * 版    权：迈斯云门禁网络科技有限公司
 */
public interface IBuildService {
    @FormUrlEncoded
    @POST("open")
    Call<OpenDoorResult> openDoor(@Field("username") String account, @Field("serverid") String serverId, @Field("doorid") int doorId);

    @FormUrlEncoded
    @POST("getAreasByIdCardNo")
    Call<BuildInfoListResult> getBuildByIdCardNo(@Field("username") String account, @Field("idcard") String idCardNo);

    @FormUrlEncoded
    @POST("getBrushCardInfo")
    Call<CardInfoResult> readCardInfo(@Field("username") String account, @Field("serverid") String serverId, @Field("areaid") int buildingId);

    @FormUrlEncoded
    @POST("removeUnregisterCard")
    Call<CardInfoResult> cancelReadCardInfo(@Field("username") String account, @Field("serverid") String serverId, @Field("areaid") int buildingId, @Field("doorid") int doorId);

    @FormUrlEncoded
    @POST("activeAccount")
    Call<BaseResult> activeCard(@Field("username") String account, @Field("serverid") String serverId, @Field("personid") int lowPersonId);

    @FormUrlEncoded
    @POST("getChildInfo")
    Call<ChildListResult> getAllChild(@Field("username") String account);
}
