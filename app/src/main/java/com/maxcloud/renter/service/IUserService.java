package com.maxcloud.renter.service;

import com.maxcloud.renter.service.entity.BaseResult;
import com.maxcloud.renter.service.entity.LoginResult;
import com.maxcloud.renter.service.entity.StsInfoResult;
import com.maxcloud.renter.service.entity.UserInfoResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 描    述：用户服务接口。
 * 作    者：向晓阳
 * 时    间：2016/3/8
 * 版    权：迈斯云门禁网络科技有限公司
 */
public interface IUserService {
    @FormUrlEncoded
    @POST("login")
    Call<LoginResult> login(@Field("username") String account, @Field("password") String password, @Field("extdata") String extendData);

    @FormUrlEncoded
    @POST("logout")
    Call<BaseResult> logout(@Field("username") String account);

    @FormUrlEncoded
    @POST("registerPhone")
    Call<BaseResult> register(@Field("username") String account, @Field("accesscode") int securityCode, @Field("password") String password, @Field("extdata") String extendData);

    @FormUrlEncoded
    @POST("resetPassword")
    Call<BaseResult> resetPassword(@Field("username") String account, @Field("password") String password, @Field("accesscode") int securityCode);

    @FormUrlEncoded
    @POST("getUserInfo")
    Call<UserInfoResult> getUserInfo(@Field("username") String account);

    @FormUrlEncoded
    @POST("getOCRAssume")
    Call<StsInfoResult> createImageSts(@Field("username") String account
            , @Field("account") String tagAccount, @Field("idcardno") String tagIdCardNo
            , @Field("roletype") int tagRoleType, @Field("imagetype") int imageType
            , @Field("height") int zoomHeight, @Field("width") int zoomWidth);

    @FormUrlEncoded
    @POST("getAccessCode")
    Call<BaseResult> sendSecurityCode(@Field("username") String account, @Field("needcheck") int checkType);

    @FormUrlEncoded
    @POST("getChildInfo")
    Call<BaseResult> getChildInfo(@Field("username") String account, @Field("jsoncmd") String jsonTemplet);
}
