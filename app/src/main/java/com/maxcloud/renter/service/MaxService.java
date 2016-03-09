package com.maxcloud.renter.service;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maxcloud.renter.MainApplication;
import com.maxcloud.renter.R;
import com.maxcloud.renter.entity.user.MsgContent;
import com.maxcloud.renter.entity.user.MsgGroup;
import com.maxcloud.renter.service.converter.gson.GsonConverterFactory;
import com.maxcloud.renter.service.entity.BaseResult;
import com.maxcloud.renter.service.entity.BuildInfoListResult;
import com.maxcloud.renter.service.entity.CardInfoResult;
import com.maxcloud.renter.service.entity.ChildInfoResult;
import com.maxcloud.renter.service.entity.ChildListResult;
import com.maxcloud.renter.service.entity.LoginResult;
import com.maxcloud.renter.service.entity.MsgContentResult;
import com.maxcloud.renter.service.entity.MsgUnreadGroupListResult;
import com.maxcloud.renter.service.entity.MsgUnreadGroupResult;
import com.maxcloud.renter.service.entity.OpenDoorResult;
import com.maxcloud.renter.service.entity.StsInfoResult;
import com.maxcloud.renter.service.entity.UserInfoResult;
import com.maxcloud.renter.util.AppHelper;
import com.maxcloud.renter.util.L;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import okio.Buffer;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 描    述：迈斯服务，提供迈斯服务所有接口。
 * 作    者：向晓阳
 * 时    间：2016/1/29
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class MaxService {
    //迈斯云证书
    private static final String CER_MAX_CLOUD = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDXTCCAkWgAwIBAgIJAM12tdY7351lMA0GCSqGSIb3DQEBCwUAMEUxCzAJBgNV\n" +
            "BAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBX\n" +
            "aWRnaXRzIFB0eSBMdGQwHhcNMTYwMjE4MDg1NzU2WhcNMTYwMzE5MDg1NzU2WjBF\n" +
            "MQswCQYDVQQGEwJBVTETMBEGA1UECAwKU29tZS1TdGF0ZTEhMB8GA1UECgwYSW50\n" +
            "ZXJuZXQgV2lkZ2l0cyBQdHkgTHRkMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB\n" +
            "CgKCAQEAtvXdZS9ehq0aKy3mXM2+2Aqnb4T4uswN/NF8gdRqPZJfgsV3zggbIumv\n" +
            "DDQ6Sfee2jUc+XmlBkctA50j1MV7WX09EAMdg7hxZKQg1iWowveQvpJx+Dvkqf/1\n" +
            "7ccKpT/QunqTkhYwVgIJmbpDXC3SHgn0dklo8FI2JMxOBL+tDQnBQq99uw3QSXfT\n" +
            "ewcv1ZUDaFO2ARXyd59n5aT6bukdi+1Ig5rd2eFLbFOJAMmej9V+jXRyofiR2oBX\n" +
            "UUHrhW6phGiMkSQGWXpdIQqcdxyJ7CEA+ZwG/IkFC3nfoCNMK1Bz4BIGt9IsznEg\n" +
            "XOypkYrdPki0IvTtAShS6eonGutwCwIDAQABo1AwTjAdBgNVHQ4EFgQUnQckD5LF\n" +
            "r3xr+dgKMOuyym+qufIwHwYDVR0jBBgwFoAUnQckD5LFr3xr+dgKMOuyym+qufIw\n" +
            "DAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEADGIamgCX3dWFy8tf+z2S\n" +
            "h/89mOXNREeQdrYOxFohMms1/6WfE0btG53AO5FxvoO6HSAoq6gYeuhQy3hOd8zV\n" +
            "7YbdzUppHTVHUb08Ni/d1xIV8zjpqBRcAtj8JDuYyhkBDUt4WLdT9PME0XB8YMPv\n" +
            "3xSoASOZaPOjPFXA/x9wDF2zGD0u44Ba1Sw6Lcr/MqleYSYmEuSsgOD38ztORIg+\n" +
            "dR/5TaS9WMBfC9cQrXe8hWufQRtS3gz/FJNMJkVU7cHEEyVdT6a6O103LX4/eM+Z\n" +
            "Z+lppWsRZYzrK5smAzCPxgBmUeUVPzoVWce/pzYWkdQklkPZUT8BI5cLdDAVbcFL\n" +
            "oQ==\n" +
            "-----END CERTIFICATE-----";

    private static final String SERVER_ADDRESS = "https://112.74.23.17:3300";
    private static MaxService _service;
    private static String _account;
    private static String _password;
    private static String _netError408;
    private static String _netErrorOther;
    private static long _timeDiff = 0;

    static {
        Context context = MainApplication.getContext();
        //加载本地保存的账号和密码。
        SharedPreferences sp = context.getSharedPreferences("AccInfo", Application.MODE_PRIVATE);
        _account = sp.getString("Account", "");
        _password = sp.getString("Password", "");
        //加载网络错误。
        _netError408 = context.getString(R.string.net_error_408);
        _netErrorOther = context.getString(R.string.net_error_other);
    }

    //保存账号和密码。
    private static void saveAccountInfo() {
        //保存账号和密码
        SharedPreferences sp = MainApplication.getContext().getSharedPreferences("AccInfo", Application.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Account", _account);
        editor.putString("Password", _password);
        editor.commit();
    }

    public static MaxService get() {
        if (null == _service) {
            _service = new MaxService();
        }

        return _service;
    }

    /**
     * 获取是否已保存账号信息。
     *
     * @return 如果已保存，则返回 true ，否则，返回 false 。
     */
    public static boolean existAccount() {
        return !TextUtils.isEmpty(_account) && !TextUtils.isEmpty(_password);
    }

//    private static final Object _lock = new Object();
//    private static final Map<String, String> _errMap = new HashMap<>();

//    private static void loadLocalErrorMap() {
//        if (FileHelper.existsErrorMap()) {
//            try {
//                TextFileReader reader = new TextFileReader(new File(FileHelper.getErrorMap()));
//                String line = reader.readLine();
//                synchronized (_lock) {
//                    _errMap.clear();
//                    while (null != line) {
//                        String items[] = line.split("\t");
//                        if (items.length >= 2) {
//                            _errMap.put(items[0], items[1]);
//                        }
//
//                        line = reader.readLine();
//                    }
//                }
//            } catch (Exception e) {
//                L.e("loadLocalErrorMap", e);
//            }
//        }
//    }
//
//    /**
//     * 加载服务器错误词典。
//     */
//    public static void loadErrorMap() {
//        loadLocalErrorMap();
//
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    OssService service = new OssService(OssService.BUCKET_CONFIG_CLOUD);
//                    service.downloadText("app/android/errorMap.txt", FileHelper.getErrorMap());
//
//                    loadLocalErrorMap();
//                } catch (Exception e) {
//                    L.e("loadErrorMap", e);
//                }
//
//                return null;
//            }
//        }.execute();
//    }
//
//    /**
//     * 取得服务器错误。
//     *
//     * @return 错误。
//     */
//    public static String getError(String errorCode) {
//        synchronized (_lock) {
//            if (TextUtils.isEmpty(errorCode)) {
//                errorCode = "unknown error";
//            }
//            if (_errMap.containsKey(errorCode)) {
//                return _errMap.get(errorCode);
//            }
//
//            return errorCode;
//        }
//    }

    /**
     * 取得服务器时间。
     *
     * @param localTime 本地时间。
     * @return 服务器时间。
     */
    public static Date getServerTime(Date localTime) {
        return new Date(localTime.getTime() + _timeDiff);
    }

    /**
     * 取得服务器时间。
     *
     * @return 服务器时间。
     */
    public static Date getServerTime() {
        return getServerTime(new Date());
    }

    /**
     * 不检查手机号。
     */
    public static final int CHECK_TYPE_NONE = 0;
    /**
     * 手机号必须已注册。
     */
    public static final int CHECK_TYPE_REGISTERED = 1;
    /**
     * 手机号必须未注册。
     */
    public static final int CHECK_TYPE_UNREGISTERED = 2;

    @IntDef({CHECK_TYPE_NONE, CHECK_TYPE_REGISTERED, CHECK_TYPE_UNREGISTERED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CheckTypeEnum {
    }

    /**
     * 身份证正面。
     */
    public static final int IMAGE_TYPE_ID_CARD = 1;
    /**
     * 房产证。
     */
    public static final int IMAGE_TYPE_PROPERTY_CARD = 2;
    /**
     * 头像。
     */
    public static final int IMAGE_TYPE_PORTRAIT = 3;
    /**
     * 申请身份证正面。
     */
    public static final int IMAGE_TYPE_APPLY_ID_CARD = 4;
    /**
     * 申请头像。
     */
    public static final int IMAGE_TYPE_APPLY_PORTRAIT = 5;
    /**
     * 身份证背面。
     */
    public static final int IMAGE_TYPE_ID_CARD_BACK = 6;

    @IntDef({IMAGE_TYPE_ID_CARD, IMAGE_TYPE_PROPERTY_CARD, IMAGE_TYPE_PORTRAIT
            , IMAGE_TYPE_APPLY_ID_CARD, IMAGE_TYPE_APPLY_PORTRAIT, IMAGE_TYPE_ID_CARD_BACK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GetImageTypeEnum {
    }

    @IntDef({IMAGE_TYPE_APPLY_ID_CARD, IMAGE_TYPE_APPLY_PORTRAIT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PutImageTypeEnum {
    }

    private IUserService _userService;
    private IBuildService _buildService;
    private IMessageService _msgService;
    private final Object _maxLock = new Object();

    private MaxService() {
        //加载SSL证书
        OkHttpClient okHttpClient = createSSLHttpClient(new Buffer()
                .writeUtf8(CER_MAX_CLOUD)
                .inputStream());

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")//时间转化为特定格式
                .excludeFieldsWithoutExposeAnnotation()//不导出实体中没有用@Expose注解的属性
                        //.setPrettyPrinting() //对json结果格式化
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        _userService = retrofit.create(IUserService.class);
        _buildService = retrofit.create(IBuildService.class);
        _msgService = retrofit.create(IMessageService.class);
    }

    /**
     * 登录。
     *
     * @return 登录信息。
     * @throws IOException
     */
    public LoginResult login() throws IOException {
        return login(_account, _password);
    }

    /**
     * 登录。
     *
     * @param account  账号。
     * @param password 密码。
     * @return 登录信息。
     * @throws IOException
     */
    public LoginResult login(String account, String password) throws IOException {
        synchronized (_maxLock) {
            Response<LoginResult> response;
            long requestTime = System.currentTimeMillis();

            try {
                if (null == account) {
                    account = "";
                }
                if (null == password) {
                    password = "";
                }

                response = _userService.login(account, password, getTelephonyInfo()).execute();
            } catch (IOException e) {
                L.e("login", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                LoginResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }

                long localTime = System.currentTimeMillis();
                String nowTime = result.getNowtime();
                SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date serverTime;
                try {
                    serverTime = dateFmt.parse(nowTime);
                } catch (Exception e) {
                    serverTime = null;
                }
                if (null == serverTime) {
                    _timeDiff = 0;
                } else {
                    _timeDiff = serverTime.getTime() - (localTime + requestTime) / 2;
                }

                _account = account;
                _password = password;

                saveAccountInfo();
                L.i("login", "校正时间成功：时间差为 %d 毫秒", _timeDiff);

                return result;
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 登出账号。
     */
    public void logout() throws IOException {
        synchronized (_maxLock) {
            Response<BaseResult> response;
            try {
                response = _userService.logout(_account).execute();
            } catch (IOException e) {
                L.e("logout", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                BaseResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }

                _account = "";
                _password = "";

                saveAccountInfo();
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 注册。
     *
     * @param account      账号。
     * @param password     密码。
     * @param securityCode 短信验证码。
     * @throws IOException
     */
    public void register(String account, String password, int securityCode) throws IOException {
        synchronized (_maxLock) {
            Response<BaseResult> response;
            try {
                if (null == account) {
                    account = "";
                }
                if (null == password) {
                    password = "";
                }

                response = _userService.register(account, securityCode, password, getTelephonyInfo()).execute();
            } catch (IOException e) {
                L.e("register", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                BaseResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 设置密码。
     *
     * @param account      账号。
     * @param password     密码。
     * @param securityCode 短信验证码。
     * @throws IOException
     */
    public void setPassword(String account, String password, int securityCode) throws IOException {
        synchronized (_maxLock) {
            Response<BaseResult> response;
            try {
                if (null == account) {
                    account = "";
                }
                if (null == password) {
                    password = "";
                }

                response = _userService.resetPassword(account, password, securityCode).execute();
            } catch (IOException e) {
                L.e("setPassword", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                BaseResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 获取用户信息。
     *
     * @param account 账号。
     * @return 用户信息。
     * @throws IOException
     */
    public UserInfoResult getUserInfo(String account) throws IOException {
        synchronized (_maxLock) {
            Response<UserInfoResult> response;
            try {
                if (null == account) {
                    account = "";
                }

                response = _userService.getUserInfo(account).execute();
            } catch (IOException e) {
                L.e("getUserInfo", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                UserInfoResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }

                return result;
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 发送短信验证码。
     *
     * @param phoneNo   手机号。
     * @param checkType 检查类型。
     * @throws IOException
     */
    public void sendSecurityCode(String phoneNo, @CheckTypeEnum int checkType) throws IOException {
        synchronized (_maxLock) {
            Response<BaseResult> response;
            try {
                if (null == phoneNo) {
                    phoneNo = "";
                }

                response = _userService.sendSecurityCode(phoneNo, checkType).execute();
            } catch (IOException e) {
                L.e("sendSecurityCode", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                BaseResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 获取图像下载的STS。
     *
     * @param account    账号。
     * @param tagAccount 图像所在账号。
     * @param idCardNo   图像所在身份证号。
     * @param imageType  图像类型。
     * @param zoomWidth  图像缩放宽度。
     * @param zoomHeight 图像缩放高度。
     * @return Sts信息。
     * @throws IOException
     */
    public StsInfoResult imageStsByGet(String account, String tagAccount, String idCardNo
            , @GetImageTypeEnum int imageType, int zoomWidth, int zoomHeight) throws IOException {
        return createImageSts(account, tagAccount, idCardNo, 2, imageType, zoomHeight, zoomWidth);
    }

    /**
     * 获取图像下载的STS。
     *
     * @param account    账号。
     * @param tagAccount 图像所在账号。
     * @param idCardNo   图像所在身份证号。
     * @param imageType  图像类型。
     * @return Sts信息。
     * @throws IOException
     */
    public StsInfoResult imageStsByGet(String account, String tagAccount, String idCardNo
            , @GetImageTypeEnum int imageType) throws IOException {
        return imageStsByGet(account, tagAccount, idCardNo, imageType, 0, 0);
    }

    /**
     * 获取图像上传的STS。
     *
     * @param account    账号。
     * @param tagAccount 图像所在账号。
     * @param idCardNo   图像所在身份证号。
     * @param imageType  图像类型。
     * @return Sts信息。
     * @throws IOException
     */
    public StsInfoResult imageStsByPut(String account, String tagAccount, String idCardNo
            , @PutImageTypeEnum int imageType) throws IOException {
        return createImageSts(account, tagAccount, idCardNo, 1, imageType, 0, 0);
    }

    /**
     * 开门。
     *
     * @param account  账号。
     * @param serverId 底端服务ID。
     * @param doorId   门ID。
     * @return 开门信息。
     * @throws IOException
     */
    public OpenDoorResult openDoor(String account, String serverId, int doorId) throws IOException {
        synchronized (_maxLock) {
            Response<OpenDoorResult> response;
            try {
                if (null == account) {
                    account = "";
                }
                if (null == serverId) {
                    serverId = "";
                }

                response = _buildService.openDoor(account, serverId, doorId).execute();
            } catch (IOException e) {
                L.e("openDoor", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                OpenDoorResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }

                return result;
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 根据身份证号获取楼栋。
     *
     * @param account  账号。
     * @param idCardNo 身份证号。
     * @return 楼栋列表信息。
     * @throws IOException
     */
    public BuildInfoListResult getBuildByIdCardNo(String account, String idCardNo) throws IOException {
        synchronized (_maxLock) {
            Response<BuildInfoListResult> response;
            try {
                if (null == account) {
                    account = "";
                }
                if (null == idCardNo) {
                    idCardNo = "";
                }

                response = _buildService.getBuildByIdCardNo(account, idCardNo).execute();
            } catch (IOException e) {
                L.e("getBuildByIdCardNo", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                BuildInfoListResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }

                return result;
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 读取用户刷卡信息（注意：完成获取操作后，需要调用cancelReadCardInfo方法）。
     *
     * @param account    账号。
     * @param serverId   底端服务ID。
     * @param buildingId 楼栋ID。
     * @return 卡信息。
     * @throws IOException
     */
    public CardInfoResult readCardInfo(String account, String serverId, int buildingId) throws IOException {
        synchronized (_maxLock) {
            Response<CardInfoResult> response;
            try {
                if (null == account) {
                    account = "";
                }
                if (null == serverId) {
                    serverId = "";
                }

                response = _buildService.readCardInfo(account, serverId, buildingId).execute();
            } catch (IOException e) {
                L.e("readCardInfo", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                CardInfoResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }

                try {
                    _buildService.cancelReadCardInfo(account, serverId, buildingId, 0).execute();
                } catch (Exception e) {
                    L.e("cancelReadCardInfo", e);
                }
                return result;
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 取消读取用户刷卡信息。
     *
     * @param account    账号。
     * @param serverId   底端服务ID。
     * @param buildingId 楼栋ID。
     * @throws IOException
     */
    public void cancelReadCardInfo(String account, String serverId, int buildingId) throws IOException {
        synchronized (_maxLock) {
            Response<CardInfoResult> response;
            try {
                if (null == account) {
                    account = "";
                }
                if (null == serverId) {
                    serverId = "";
                }

                response = _buildService.cancelReadCardInfo(account, serverId, buildingId, 0).execute();
            } catch (IOException e) {
                L.e("cancelReadCardInfo", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                BaseResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 激活门禁卡。
     *
     * @param account     账号。
     * @param serverId    底端服务器ID。
     * @param lowPersonId 底端人员ID。
     * @throws IOException
     */
    public void activeCard(String account, String serverId, int lowPersonId) throws IOException {
        synchronized (_maxLock) {
            Response<BaseResult> response;
            try {
                if (null == account) {
                    account = "";
                }
                if (null == serverId) {
                    serverId = "";
                }

                response = _buildService.activeCard(account, serverId, lowPersonId).execute();
            } catch (IOException e) {
                L.e("activeCard", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                BaseResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 获取所有小孩。
     *
     * @param account 账号。
     * @throws IOException
     */
    public List<ChildInfoResult> getAllChild(String account) throws IOException {
        synchronized (_maxLock) {
            Response<ChildListResult> response;
            try {
                if (null == account) {
                    account = "";
                }

                response = _buildService.getAllChild(account).execute();
            } catch (IOException e) {
                L.e("getAllChild", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                ChildListResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }

                return result.getChildList();
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 获取历史消息列表。
     *
     * @param account  账号。
     * @param minMsgId 最小消息ID。
     * @return 历史消息列表。
     * @throws IOException
     */
    public MsgContent[] getHistoryMessage(String account, long minMsgId) throws IOException {
        synchronized (_maxLock) {
            Response<MsgContentResult> response;
            try {
                if (null == account) {
                    account = "";
                }

                response = _msgService.getHistoryMessage(account).execute();
            } catch (IOException e) {
                L.e("getHistoryMessage", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                MsgContentResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }

                return null;
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 获取未读消息组列表。
     *
     * @param account 账号。
     * @return 未读消息组列表。
     * @throws IOException
     */
    public MsgGroup[] groupUnreadMessage(String account) throws IOException {
        synchronized (_maxLock) {
            Response<MsgUnreadGroupListResult> response;
            try {
                if (null == account) {
                    account = "";
                }

                response = _msgService.groupUnreadMessage(account).execute();
            } catch (IOException e) {
                L.e("groupMessage", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                MsgUnreadGroupListResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }

                List<MsgUnreadGroupResult> groupResults = result.getData();
                MsgGroup[] msgGroups = new MsgGroup[groupResults.size()];
                for (int i = 0; i < groupResults.size(); i++) {
                    MsgUnreadGroupResult groupResult = groupResults.get(i);
                    String groupAccount = groupResult.getAccount();
                    String groupName = groupResult.getName();
                    if (groupResult.getSendtype() == MsgUnreadGroupResult.TYPE_SYSTEM) {
                        groupAccount = "System";
                        groupName = "迈斯云门禁";
                    }
                    MsgGroup msgGroup = new MsgGroup(groupResult.getNotifycount(), groupAccount, groupName, groupResult.getContent(), groupResult.getTime());

                    msgGroups[i] = msgGroup;
                }
                return msgGroups;
            } else {
                throw new IOException(response.message());
            }
        }
    }

    /**
     * 获取未读消息列表。
     *
     * @param account 账号。
     * @return 未读消息列表。
     * @throws IOException
     */
    public MsgContent[] readMessage(String account) throws IOException {
        synchronized (_maxLock) {
            Response<MsgContentResult> response;
            try {
                if (null == account) {
                    account = "";
                }

                response = _msgService.readMessage(account).execute();
            } catch (IOException e) {
                L.e("readMessage", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                MsgContentResult result = response.body();
                if (result.getCode() != 0) {
                    throw new IOException(result.getError());
                }

                return null;
            } else {
                throw new IOException(response.message());
            }
        }
    }

    //格式化错误。
    private IOException formatException(IOException e) {
        if (e instanceof SocketTimeoutException) {
            return new IOException(_netError408);
        } else if (e instanceof SocketException) {
            return new IOException(_netErrorOther);
        }

        return e;
    }

    //获取手机基本信息。
    private String getTelephonyInfo() {
        return "{\"AppType\":" + AppHelper.getAppType() +// APP类型
                ", \"Version\":\"" + AppHelper.getVersion() + "\"" +// APP版本号
                ", \"Model\":\"" + android.os.Build.MODEL + "\"" +// 手机型号
                ", \"OS\":\"Android " + android.os.Build.VERSION.RELEASE + "\"" +// 操作系统版本
                ", \"IMEI\":\"" + AppHelper.getDeviceId() + "\"" +// IMEI
                ", \"NetType\":\"" + AppHelper.getNetType() + "\"}";// 网络类型
    }

    private OkHttpClient createSSLHttpClient(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                    L.e("closeCertificate", e);
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            //判断当前访问网站和证书指定的网站是否一至。
                            return true;
                        }
                    });
            return httpClientBuilder.build();

        } catch (Exception e) {
            L.e("createSSLHttpClient", e);
        }

        return new OkHttpClient();
    }

    // 创建图像临时的STS。
    private StsInfoResult createImageSts(String account, String tagAccount, String idCardNo, int roleType
            , int imageType, int zoomHeight, int zoomWidth) throws IOException {
        synchronized (_maxLock) {
            Response<StsInfoResult> response;
            try {
                if (null == account) {
                    account = "";
                }
                if (null == tagAccount) {
                    tagAccount = "";
                }
                if (null == idCardNo) {
                    idCardNo = "";
                }

                response = _userService.createImageSts(account, tagAccount,
                        idCardNo, roleType, imageType, zoomHeight, zoomWidth).execute();
            } catch (IOException e) {
                L.e("createImageSts", e);

                throw formatException(e);
            }

            if (response.isSuccess()) {
                StsInfoResult result = response.body();
                if (!result.isSuccess()) {
                    throw new IOException(result.getError());
                }

                return result;
            } else {
                throw new IOException(response.message());
            }
        }
    }
}
