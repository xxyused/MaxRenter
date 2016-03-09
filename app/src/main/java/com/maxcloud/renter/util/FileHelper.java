package com.maxcloud.renter.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 描    述：文件帮助类，提供APP所有文件和目录路径及相关路径是否存在的判断。
 * 作    者：向晓阳
 * 时    间：2016/2/18
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class FileHelper {
    private static final String OSS_CITY_LIST_FILE = "WeatherInfo/CityList.txt";
    private static final String OSS_WEATHER_FILE = "WeatherInfo/new";
    private static final String OSS_SIGN_FILE = "ConstellationInfo/new.txt";

    private static final String USER_DATA_FILE = "user.dat";
    private static final String USER_PORTRAIT_FILE = "portrait.dat";
    private static final String USER_ID_CARD_FILE = "idCard.dat";
    private static final String CITY_LIST_FILE = "cityList.txt";
    private static final String CITY_WEATHER_FILE = "weather.txt";
    private static final String SIGN_FILE = "sign.txt";
    private static final String NEW_APK = "max_renter.apk";

    /**
     * 清除所有用户数据。
     */
    public static void clearAllData() {
        File dataDir = new File(getDataDirectory());
        File[] subFiles = dataDir.listFiles();
        for (File subFile : subFiles) {
            subFile.delete();
        }
    }

    /**
     * 获取更新目录的路径。
     *
     * @return 更新目录的路径。
     */
    public static String getUpdateDirectory() {
        return String.format("%s/com.maxcloud/renter/update/", Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    /**
     * 获取数据目录的路径。
     *
     * @return 数据目录的路径。
     */
    public static String getDataDirectory() {
        return String.format("%s/com.maxcloud/renter/data/", Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    /**
     * 获取日志目录的路径。
     *
     * @return 日志目录的路径。
     */
    public static String getLogDirectory() {
        return String.format("%s/com.maxcloud/renter/log/", Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    /**
     * 获取操作日志目录的路径。
     *
     * @return 操作日志目录的路径。
     */
    public static String getUseLogDirectory() {
        return String.format("%s/com.maxcloud/renter/log_use/", Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    /**
     * 获取缓存目录的路径。
     *
     * @return 缓存目录的路径。
     */
    public static String getCacheDirectory() {
        return String.format("%s/com.maxcloud/renter/cache/", Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    /**
     * 获取用户数据文件的路径。
     *
     * @return 用户数据文件的路径。
     */
    public static String getUserData() {
        return String.format("%s/%s", FileHelper.getDataDirectory(), USER_DATA_FILE);
    }

    /**
     * 判断用户数据文件是否存在。
     *
     * @return 如果已存在，则返回 true ，否则返回 false。
     */
    public static boolean existsUserData() {
        File file = new File(getUserData());

        return file.exists();
    }

    /**
     * 获取头像文件的路径。
     *
     * @return 头像文件的路径。
     */
    public static String getUserPortrait() {
        return String.format("%s/%s", FileHelper.getDataDirectory(), USER_PORTRAIT_FILE);
    }

    /**
     * 判断头像文件是否存在。
     *
     * @return 如果已存在，则返回 true ，否则返回 false。
     */
    public static boolean existsUserPortrait() {
        File file = new File(getUserPortrait());

        return file.exists();
    }

    /**
     * 获取身份证照片文件的路径。
     *
     * @return 身份证照片文件的路径。
     */
    public static String getUserIdCard() {
        return String.format("%s/%s", FileHelper.getDataDirectory(), USER_ID_CARD_FILE);
    }

    /**
     * 判断身份证照片文件是否存在。
     *
     * @return 如果已存在，则返回 true ，否则返回 false。
     */
    public static boolean existsUserIdCard() {
        File file = new File(getUserIdCard());

        return file.exists();
    }

    /**
     * 获取城市列表文件的路径。
     *
     * @return 城市列表文件的路径。
     */
    public static String getCityList() {
        return String.format("%s/%s", FileHelper.getDataDirectory(), CITY_LIST_FILE);
    }

    /**
     * 判断城市列表文件是否存在。
     *
     * @return 如果已存在，则返回 true ，否则返回 false。
     */
    public static boolean existsCityList() {
        File file = new File(getCityList());

        return file.exists();
    }

    /**
     * 获取天气文件的路径。
     *
     * @return 天气文件的路径。
     */
    public static String getWeather() {
        return String.format("%s/%s", FileHelper.getDataDirectory(), CITY_WEATHER_FILE);
    }

    /**
     * 判断天气文件是否存在。
     *
     * @return 如果已存在，则返回 true ，否则返回 false。
     */
    public static boolean existsWeather() {
        File file = new File(getWeather());

        return file.exists();
    }

    /**
     * 获取星座文件的路径。
     *
     * @return 星座文件的路径。
     */
    public static String getSign() {
        return String.format("%s/%s", FileHelper.getDataDirectory(), SIGN_FILE);
    }

    /**
     * 判断星座文件是否存在。
     *
     * @return 如果已存在，则返回 true ，否则返回 false。
     */
    public static boolean existsSign() {
        File file = new File(getSign());

        return file.exists();
    }

    /**
     * 获取更新包路径。
     *
     * @return 更新包路径。
     */
    public static String getUpdateApk() {
        return String.format("%s/%s", FileHelper.getUpdateDirectory(), NEW_APK);
    }

    /**
     * 生成一个缓存路径。
     *
     * @return 缓存文件路径。
     */
    public static String createCacheFile() {
        int random = (int) (Math.random() * 10);
        String fileName = String.format("%d%d.dat", System.currentTimeMillis(), random);

        return String.format("%s/%s", FileHelper.getCacheDirectory(), fileName);
    }

    /**
     * 创建网络请求。
     *
     * @param netUrl 网络路径。
     * @return 成功的请求。
     * @throws IOException
     */
    public static ResponseBody createResponse(String netUrl) throws IOException {
        if (TextUtils.isEmpty(netUrl)) {
            return null;
        }

        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        Request request = new Request.Builder()
                .url(netUrl)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        int responseCode = response.code();
        if (responseCode >= 200 && responseCode < 300) {
            return response.body();
        } else {
            throw new IOException(String.format("{\"code\":%d,\"url\":\"%s\",\"message\":\"%s\"}", responseCode, netUrl, response.message()));
        }
    }

    /**
     * 下载网络文件。
     *
     * @param netUrl    要下载的网络文件路径。
     * @param localPath 要下载到本地的路径。
     * @return 如果下载成功，返回 true ，否则，返回 false 。
     * @throws IOException
     */
    public static boolean downloadFile(String netUrl, String localPath) throws IOException {
        File localFile = new File(localPath);
        if (localFile.exists()) {
            localFile.delete();
        }

        if (TextUtils.isEmpty(netUrl)) {
            return false;
        }

        ResponseBody response = createResponse(netUrl);
        if (null != response) {
            InputStream stream = response.byteStream();
            FileOutputStream writer = null;

            try {
                writer = new FileOutputStream(localFile);

                int length;
                byte[] buffer = new byte[1024];
                length = stream.read(buffer);
                while (length > 0) {
                    writer.write(buffer, 0, length);

                    length = stream.read(buffer);
                }
                writer.flush();
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                }

                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }

        return false;
    }

    /**
     * 下载城市列表数据。
     *
     * @return 如果下载成功，返回 true ，否则，返回 false 。
     * @throws IOException
     */
    public static boolean downloadCityList() throws IOException {
        String ossPath = String.format("http://config-cloud.oss-cn-shenzhen.aliyuncs.com/%s", OSS_CITY_LIST_FILE);

        return downloadFile(ossPath, getCityList());
    }

    /**
     * 下载天气数据。
     *
     * @return 如果下载成功，返回 true ，否则，返回 false 。
     * @throws IOException
     */
    public static boolean downloadWeather(String cityCode) throws IOException {
        String ossPath = String.format("http://config-cloud.oss-cn-shenzhen.aliyuncs.com/%s/%s.txt", OSS_WEATHER_FILE, cityCode);

        return downloadFile(ossPath, getWeather());
    }

    /**
     * 下载星座数据。
     *
     * @return 如果下载成功，返回 true ，否则，返回 false 。
     * @throws IOException
     */
    public static boolean downloadSign() throws IOException {
        String ossPath = String.format("http://config-cloud.oss-cn-shenzhen.aliyuncs.com/%s", OSS_SIGN_FILE);

        return downloadFile(ossPath, getSign());
    }
}
