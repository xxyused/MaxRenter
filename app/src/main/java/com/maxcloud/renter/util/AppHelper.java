package com.maxcloud.renter.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.maxcloud.renter.MainApplication;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.ResponseBody;

/**
 * 描    述：APP帮助类，提供APP相关的一些数据（APP类型、版本号、是否为Debug版本、是否为Develop版本、设备ID和网络类型）。
 * 作    者：向晓阳
 * 时    间：2016/2/18
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class AppHelper {
    private static final String CFG_SERVER = "http://config-cloud.oss-cn-shenzhen.aliyuncs.com";
    public static final String CFG_FULL_PATH = CFG_SERVER + "/app/android/" + getAppType() + "/Config.xml";
    public static final String APK_FULL_PATH = CFG_SERVER + "/app/android/" + getAppType() + "/update/MaxRenter.apk";
    public static final String APK_D_FULL_PATH = CFG_SERVER + "/app/android/" + getAppType() + "/update/MaxRenter_D.apk";

    private static String _version;
    private static Boolean _isDevelop;
    private static String _deviceId;

    /**
     * 获取APP类型。
     *
     * @return APP类型。
     */
    public static int getAppType() {
        // 类型：0 未知，1 微信，2 Android APP（开门），3 IOS APP，4 PC Web， 5 Mobile Web，
        // 6 Android APP（管理），7 Android APP（迈斯租客）
        return 7;
    }

    /**
     * 获取当前版本号。
     *
     * @return 当前版本号。
     */
    public static String getVersion() {
        if (TextUtils.isEmpty(_version)) {
            Context context = MainApplication.getContext();
            PackageManager manager = context.getPackageManager();
            try {
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

                _version = info.versionName;
            } catch (Exception e) {
                _version = "未知版本";
            }
        }

        return _version;
    }

    /**
     * 把版本号转换成整形。
     *
     * @param versionName 要转换的版本号。
     * @return 整形版本号。
     */
    public static int versionToInt(String versionName) {
        if (TextUtils.isEmpty(versionName)) {
            return 0;
        }

        String[] vers = null;

        int index = versionName.indexOf('/');
        if (index >= 0) {
            vers = versionName.substring(0, index).split("\\.");
        } else {
            vers = versionName.split("\\.");
        }

        int version = Integer.valueOf(vers[0]) << 24
                | Integer.valueOf(vers[1]) << 16 | Integer.valueOf(vers[2]);

        return version;
    }

    /**
     * 把上下文的版本号转换成整形。
     *
     * @return整形版本号。
     */
    public static int versionToInt() {
        try {
            return versionToInt(getVersion());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 把版本代号转换成版本号。
     *
     * @param versionCode 要转换的版本代号。
     * @return 版本号。
     */
    public static String intToVersion(int versionCode) {
        int ver1 = ((versionCode >> 24) & 0xff);
        int ver2 = ((versionCode >> 16) & 0xff);
        int ver3 = (versionCode & 0xffff);

        return String.format("%d.%d.%d", ver1, ver2, ver3);
    }

    /**
     * 判断是否为debug版本。
     *
     * @return 如果是则返回 true ，否则，返回 false 。
     */
    public static boolean isDebug() {
        try {
            String versionName = getVersion();

            return versionName.contains("/D");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断是否为develop版本。
     *
     * @return 如果是则返回 true ，否则，返回 false 。
     */
    public static boolean isDevelop() {
        if (null == _isDevelop) {
            try {
                ApplicationInfo info = MainApplication.getContext().getApplicationInfo();
                _isDevelop = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            } catch (Exception e) {

            }
        }

        return _isDevelop;
    }

    /**
     * 获取设备ID。
     *
     * @return 设备ID。
     */
    public static String getDeviceId() {
        if (TextUtils.isEmpty(_deviceId)) {
            Context context = MainApplication.getContext();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            _deviceId = telephonyManager.getDeviceId();
        }

        return _deviceId;
    }

    /**
     * 获取网络类型。
     *
     * @return 网络类型。
     */
    public static String getNetType() {
        String netTypeName = "Invalid";
        Context context = MainApplication.getContext();
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String subName = networkInfo.getSubtypeName();
            if (TextUtils.isEmpty(subName)) {
                netTypeName = networkInfo.getTypeName();
            } else {
                netTypeName = String.format("%s(%s)", networkInfo.getTypeName(), networkInfo.getSubtypeName());
            }
        }

        return netTypeName;
    }

    /**
     * 获取更新信息。
     *
     * @return 更新信息。
     * @throws IOException
     */
    public static UpdateInfo getUpdateInfo() throws IOException {
        InputStream stream = null;
        try {
            ResponseBody response = FileHelper.createResponse(CFG_FULL_PATH);
            if (null != response) {
                stream = response.byteStream();

                return new UpdateInfo(stream);
            }
        } finally {
            if (null != stream) {
                stream.close();
            }
        }

        return null;
    }

    /**
     * 获取是否有新版。
     *
     * @return 如果有新版本，返回 true ，否则，返回 false 。
     */
    public static boolean hasNewVersion() {
        UpdateInfo updateInfo = null;
        try {
            updateInfo = getUpdateInfo();
        } catch (Exception e) {
            L.e("hasNewVersion", e);
        }

        return hasNewVersion(updateInfo);
    }

    /**
     * 获取是否有新版本。
     *
     * @param updateInfo 更新信息。
     * @return 如果有新版本，返回 true ，否则，返回 false 。
     */
    public static boolean hasNewVersion(UpdateInfo updateInfo) {
        if (null != updateInfo && updateInfo.getVersion() > versionToInt()) {
            return true;
        }

        return false;
    }

    /**
     * 获取是否有新的最小版本。
     *
     * @param updateInfo 更新信息。
     * @return 如果有新版本，返回 true ，否则，返回 false 。
     */
    public static boolean hasNewMinVersion(UpdateInfo updateInfo) {
        if (null != updateInfo && updateInfo.getMinVersion() > versionToInt()) {
            return true;
        }

        return false;
    }

    public static class UpdateInfo {
        private int mVersion;
        private int mMinVer;
        private boolean mMaintenance;
        private Date mStartTime;
        private Date mEndTime;
        private String mUpdateDesc;

        public int getVersion() {
            return mVersion;
        }

        public int getMinVersion() {
            return mMinVer;
        }

        public boolean isMaintenance() {
            if (!mMaintenance) {
                return false;
            }
            Date curDate = new Date(System.currentTimeMillis());
            if (null != mStartTime && curDate.before(mStartTime)) {
                return false;
            }
            if (null != mEndTime && curDate.after(mEndTime)) {
                return false;
            }

            return true;
        }

        public Date getStartTime() {
            return mStartTime;
        }

        public Date getEndTime() {
            return mEndTime;
        }

        public String getUpdateDesc() {
            return mUpdateDesc;
        }

        public UpdateInfo(InputStream inputStream) {
            DocumentBuilderFactory factory = null;
            DocumentBuilder builder = null;
            Document document = null;
            // 首先找到xml文件
            factory = DocumentBuilderFactory.newInstance();
            try {
                // 找到xml，并加载文档
                builder = factory.newDocumentBuilder();
                document = builder.parse(inputStream);

                // 找到根Element
                Element root = document.getDocumentElement();
                if (null != root) {
                    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    String version = getElementValue(root, "version");
                    mVersion = versionToInt(version);
                    String minVersion = getElementValue(root, "minversion");
                    if (!TextUtils.isEmpty(minVersion)) {
                        mMinVer = versionToInt(minVersion);
                    }
//                    String updateTime = getElementValue(root, "updatetime");
//                    if (!TextUtils.isEmpty(updateTime)) {
//                        mUpdateTime = dateFmt.parse(updateTime);
//                    }
//                    String minApi = getElementValue(root, "minapi");
//                    if (!TextUtils.isEmpty(minApi)) {
//                        mMinApi = Integer.valueOf(minApi);
//                    }
                    String maintenance = getElementValue(root, "ismaintenance");
                    if (!TextUtils.isEmpty(maintenance)) {
                        mMaintenance = Boolean.valueOf(maintenance);
                    }
                    String startTime = getElementValue(root, "starttime");
                    if (!TextUtils.isEmpty(startTime)) {
                        mStartTime = dateFmt.parse(startTime);
                    }
                    String endTime = getElementValue(root, "endtime");
                    if (!TextUtils.isEmpty(endTime)) {
                        mEndTime = dateFmt.parse(endTime);
                    }
                    String updateDesc = getElementValue(root, "updatedesc");
                    if (!TextUtils.isEmpty(updateDesc)) {
                        mUpdateDesc = updateDesc;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String getElementValue(Element parent, String childTagName) {
            NodeList nodes = parent.getElementsByTagName(childTagName);

            if (nodes.getLength() > 0) {
                Element childNode = (Element) nodes.item(0);

                if (childNode.hasAttribute("value")) {
                    return childNode.getAttribute("value");
                }
            }

            return null;
        }
    }
}
