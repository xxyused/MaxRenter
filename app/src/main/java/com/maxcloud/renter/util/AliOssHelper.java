package com.maxcloud.renter.util;

import android.text.TextUtils;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.model.OSSFederationToken;
import com.alibaba.sdk.android.oss.model.OSSResponseInfo;
import com.alibaba.sdk.android.oss.model.StsTokenGetter;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.alibaba.sdk.android.oss.storage.OSSFile;
import com.alibaba.sdk.android.oss.util.OSSToolKit;
import com.maxcloud.renter.entity.IOssObject;
import com.maxcloud.renter.entity.IStsAccessInfo;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

/**
 * 描    述：阿里OSS帮助类，提供OSS上传的相关操作（上传操作日志、上传错误日志和上传反馈信息）。
 * 作    者：向晓阳
 * 时    间：2016/2/25
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class AliOssHelper {
    private static final String OSS_HOST_ID = "oss-cn-shenzhen.aliyuncs.com";

    private static final Object _ossLock = new Object();
    private static TokenGenerator mTokenGen = new TokenGenerator() {
        @Override
        public String generateToken(String httpMethod, String md5, String type, String date, String ossHeaders, String resource) {
            String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n" + ossHeaders + resource;

            return OSSToolKit.generateToken("p4aEY57r4N6ibyOq", "cx6woxf3qkrqpdL68KugZwIrIYNrQ9", content);
        }
    };

    /**
     * 上传操作日志。
     *
     * @param localFile   操作日志本地文件路径。
     * @param ossFileName 上传到OSS的新名称。
     * @throws FileNotFoundException
     * @throws OSSException
     */
    public static void uploadUse(String localFile, String ossFileName) throws FileNotFoundException, OSSException {
        synchronized (_ossLock) {
            long serverTick = MaxService.getServerTime().getTime();

            OSSService ossService = OSSServiceProvider.getService();
            // 设置加签类型为原始AK/SK加签
            ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK);
            ossService.setCustomStandardTimeWithEpochSec(serverTick / 1000);

            OSSBucket bucket = ossService.getOssBucket("max-log");
            bucket.setBucketHostId(OSS_HOST_ID);
            bucket.setBucketTokenGen(mTokenGen);

            String ossPath;
            if (AppHelper.isDebug() || AppHelper.isDevelop()) {
                ossPath = String.format("app_%d/debug_use/%s", AppHelper.getAppType(), ossFileName);
            } else {
                ossPath = String.format("app_%d/use/%s", AppHelper.getAppType(), ossFileName);
            }
            OSSFile ossFile = ossService.getOssFile(bucket, ossPath);
            ossFile.setUploadFilePath(localFile, "UTF-8");
            ossFile.upload();
        }
    }

    /**
     * 上传错误日志。
     *
     * @param error 错误内容。
     * @throws OSSException
     * @throws UnsupportedEncodingException
     */
    public static void uploadError(String error) throws OSSException, UnsupportedEncodingException {
        synchronized (_ossLock) {
            LoginInfo loginInfo = LoginInfo.get();
            String phoneNo = loginInfo.getAccount();
            Date serverTime = MaxService.getServerTime();
            long serverTick = serverTime.getTime();

            OSSService ossService = OSSServiceProvider.getService();
            // 设置加签类型为原始AK/SK加签
            ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK);
            ossService.setCustomStandardTimeWithEpochSec(serverTick / 1000);

            OSSBucket bucket = ossService.getOssBucket("max-log");
            bucket.setBucketHostId(OSS_HOST_ID);
            bucket.setBucketTokenGen(mTokenGen);

            long random = serverTick * 1000 + new Random().nextInt(1000);
            String ossPath;
            if (AppHelper.isDebug() || AppHelper.isDevelop()) {
                ossPath = String.format("app_%d/debug_err/%s/%tF_%d.log", AppHelper.getAppType(), phoneNo, serverTime, random);
            } else {
                ossPath = String.format("app_%d/err/%s/%tF_%d.log", AppHelper.getAppType(), phoneNo, serverTime, random);
            }
            OSSData ossData = ossService.getOssData(bucket, ossPath);
            ossData.setData(error.getBytes("UTF-8"), "text/plain");
            ossData.upload();
        }
    }

    /**
     * 上传反馈信息。
     *
     * @param tel  联系电话。
     * @param text 反馈内容。
     * @throws OSSException
     * @throws UnsupportedEncodingException
     */
    public static void uploadFeedback(String tel, String text) throws OSSException, UnsupportedEncodingException {
        synchronized (_ossLock) {
            LoginInfo loginInfo = LoginInfo.get();
            String phoneNo = loginInfo.getAccount();
            Date serverTime = MaxService.getServerTime();
            long serverTick = serverTime.getTime();

            OSSService ossService = OSSServiceProvider.getService();
            // 设置加签类型为原始AK/SK加签
            ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK);
            ossService.setCustomStandardTimeWithEpochSec(serverTick / 1000);

            OSSBucket bucket = ossService.getOssBucket("max-feedback");
            bucket.setBucketHostId(OSS_HOST_ID);
            bucket.setBucketTokenGen(mTokenGen);

            String feedback = "{\"Type\":" + AppHelper.getAppType() +
                    ", \"Version\":\"" + AppHelper.getVersion() + '\"' +
                    ", \"Tel\":\"" + tel + '\"' +
                    ", \"Feedback\":\"" + text + '\"' +
                    ", \"LocalTime\":\"" + String.format("%1$tF %1$tT", new Date()) + "\"}";

            long random = serverTick * 1000 + new Random().nextInt(1000);
            String ossPath;
            if (AppHelper.isDebug() || AppHelper.isDevelop()) {
                ossPath = String.format("debug/%s/%tF_%d.txt", phoneNo, serverTime, random);
            } else {
                ossPath = String.format("%s/%tF_%d.txt", phoneNo, serverTime, random);
            }
            OSSData ossData = ossService.getOssData(bucket, ossPath);
            ossData.setData(feedback.getBytes("UTF-8"), "text/plain");
            ossData.upload();
        }
    }

    /**
     * 上传图像到OSS服务器。
     * @param accessInfo STS账号信息。
     * @param object 图像信息。
     * @param localPath 图像本地路径。
     * @throws IOException
     */
    public static void uploadImage(IStsAccessInfo accessInfo, IOssObject object, String localPath) throws IOException {
        synchronized (_ossLock) {
            if (TextUtils.isEmpty(localPath)) {
                return;
            }

            Date serverTime = MaxService.getServerTime();
            long serverTick = serverTime.getTime();

            String hostId = object.getService();
            if (hostId.startsWith("http://")) {
                hostId = hostId.substring(7);
            }
            OSSService ossService = OSSServiceProvider.getService();
            // 设置加签类型为Token加签
            ossService.setAuthenticationType(AuthenticationType.FEDERATION_TOKEN);
            ossService.setGlobalDefaultStsTokenGetter(new StsTokenInfo(accessInfo, 600000));
            ossService.setCustomStandardTimeWithEpochSec(serverTick / 1000);
            OSSBucket bucket = ossService.getOssBucket(object.getBucket());
            bucket.setBucketHostId(hostId);
            if (null != bucket) {
                try {
                    OSSFile ossFile = ossService.getOssFile(bucket, object.getPath());

                    ossFile.setUploadFilePath(localPath, "image/jpg");
                    ossFile.enableUploadCheckMd5sum();// 开启上传MD5校验

                    ossFile.upload();
                } catch (OSSException e) {
                    String message;
                    OSSException.ExceptionType eType = e.getExceptionType();
                    if (eType == OSSException.ExceptionType.LOCAL_EXCEPTION) {
                        message = "OSSException type: LOCAL_EXCEPTION \nobjectKey: " + e.getObjectKey() + "\nmessage: " + e.getException().getMessage();
                    } else if (eType == OSSException.ExceptionType.OSS_EXCEPTION) {
                        OSSResponseInfo respInfo = e.getOssRespInfo();
                        message = "OSSException type: OSS_EXCEPTION \nobjectKey: " + e.getObjectKey() + "\nstatusCode: " + respInfo.getStatusCode() + "\nrequestId: " + respInfo.getRequestId() + "\nresponseCode: " + respInfo.getCode() + "\nmessage: " + respInfo.getMessage();
                    } else {
                        message = "OSSException type: unknown \nobjectKey: " + e.getObjectKey() + "\nmessage: " + e.getMessage();
                    }

                    IOException newExc = new IOException(message);
                    newExc.setStackTrace(e.getStackTrace());
                    newExc.initCause(e.getCause());

                    throw newExc;
                }
            }
        }
    }

    private static class StsTokenInfo extends StsTokenGetter {
        private OSSFederationToken mFederationToken;

        public StsTokenInfo(IStsAccessInfo stsAccess, long expiration) {
            mFederationToken = new OSSFederationToken();
            mFederationToken.setTempAk(stsAccess.getAccessKey());
            mFederationToken.setTempSk(stsAccess.getAccessSecret());
            mFederationToken.setSecurityToken(stsAccess.getAccessToken());
            mFederationToken.setExpiration(expiration);
        }

        @Override
        public OSSFederationToken getFederationToken() {
            return mFederationToken;
        }
    }
}
