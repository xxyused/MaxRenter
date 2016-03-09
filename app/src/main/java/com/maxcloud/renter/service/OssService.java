package com.maxcloud.renter.service;

import android.support.annotation.StringDef;

import com.maxcloud.renter.util.L;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 描    述：OSS服务类，提供OSS文件的下载功能。
 * 作    者：向晓阳
 * 时    间：2016/1/29
 * 版    权：迈斯云门禁网络科技有限公司
 */
 class OssService {
    public static final String OSS_HOST_ID = "oss-cn-shenzhen.aliyuncs.com";

    @StringDef(value = {BUCKET_CONFIG_CLOUD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BucketEnum {
    }

    public static final String BUCKET_CONFIG_CLOUD = "config-cloud";

    private Retrofit _retrofit;
    private IConfigService _configService;

    public OssService(@BucketEnum String bucketName) {
        _retrofit = new Retrofit.Builder()
                .baseUrl(String.format("http://%s.%s", bucketName, OSS_HOST_ID))
                .build();
        _configService = _retrofit.create(IConfigService.class);
    }

    /**
     * 下载文件。
     *
     * @param ossPath OSS路径。
     * @return 文件流。
     * @throws IOException
     */
    public InputStream download(String ossPath) throws IOException {
        try {
            ResponseBody response = _configService.download(ossPath).execute().body();

            return response.byteStream();
        } catch (IOException e) {
            L.e("download", e);
return null;
            //throw formatException(e);
        }
    }

    /**
     * 下载文本文件到指定路径。
     *
     * @param ossPath   OSS路径。
     * @param localPath 本地路径。
     * @throws IOException
     */
    public void downloadText(String ossPath, String localPath) throws IOException {
        InputStream is = download(ossPath);
        FileOutputStream fos = new FileOutputStream(new File(localPath));

        try {
            int length;
            byte[] buffer = new byte[1024];
            length = is.read(buffer);
            while (length > 0) {
                fos.write(buffer, 0, length);

                length = is.read(buffer);
            }
            fos.flush();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
            }

            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取文本文件的内容。
     *
     * @param ossPath     OSS路径。
     * @param charsetName 文本文件字符集名称。
     * @return 文本文件内容。
     * @throws IOException
     */
    public String getText(String ossPath, String charsetName) throws IOException {
        InputStream is = download(ossPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            int length;
            byte[] buffer = new byte[1024];
            length = is.read(buffer);
            while (length > 0) {
                baos.write(buffer, 0, length);

                length = is.read(buffer);
            }
            baos.flush();

            return baos.toString(charsetName);
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }

            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }

    private interface IConfigService {
        @GET("/{filePath}")
        Call<ResponseBody> download(@Path("filePath") String filePath);
    }
}
