package com.maxcloud.renter.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.TypedValue;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.home.WelcomeActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * 描    述：OS帮助类，提供OS的相关操作（系统类型判断、快捷方式的创建和删除）。
 * 作    者：向晓阳
 * 时    间：2016/2/25
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class OSHelper {
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    /**
     * 是否为MIUI系统。
     *
     * @return 如果是，则返回 true ，否则，返回 false 。
     */
    public static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * 是否为Flyme系统。
     *
     * @return 如果是，则返回 true ，否则，返回 false 。
     */
    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 创建快捷方式。
     *
     * @param context 要创建的快捷方式对应的上下文。
     */
    public static void addShortcut(ContextWrapper context) {
        // 快捷方式图标
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher);
        // 快捷方式Activity
        Intent intent = new Intent(context, WelcomeActivity.class);

        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");// 保持默认
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name)); // 快捷方式的名称
        shortcut.putExtra("duplicate", false); // 不允许重复创建
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        context.sendBroadcast(shortcut);// 广播
    }

    /**
     * 删除快捷方式。
     *
     * @param context 要删除的快捷方式对应的上下文。
     */
    public static void delShortcut(ContextWrapper context) {
        // 快捷方式Activity（和创建时的设置一致）
        Intent intent = new Intent(context, WelcomeActivity.class);
        //intent.setAction("android.intent.action.MAIN");

        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        context.sendBroadcast(shortcut);
    }

    /**
     * 判断快捷方式是否已存在
     *
     * @param context 快捷方式对应的上下文。
     * @return
     */
    public static boolean hasShortCut(ContextWrapper context) {
        // String url = "";
        Uri CONTENT_URI = Uri.parse(getAuthority(context));
        // if (android.os.Build.VERSION.SDK_INT < 8) {
        // url =
        // "content://com.android.launcher.settings/favorites?notify=true";
        // } else {
        // url =
        // "content://com.android.launcher2.settings/favorites?notify=true";
        // }
        ContentResolver resolver = context.getContentResolver();
        // Cursor cursor = resolver.query(Uri.parse(url), null, "title=?", new
        // String[]{context.getString(R.string.app_name)}, null);
        Cursor cursor = resolver.query(CONTENT_URI, null, "iconPackage=?", new String[]{context.getPackageName()}, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }

        return false;
    }

    /**
     * 判断快捷方式是否已存在
     *
     * @param context 快捷方式对应的上下文。
     * @return
     */
    public static boolean isExistShortcut(ContextWrapper context) {
        boolean isInstallShortcut = false;
        final ContentResolver cr = context.getContentResolver();
        final String AUTHORITY = getAuthority(context);
        final Uri CONTENT_URI = Uri.parse(AUTHORITY);
        Cursor c = cr.query(CONTENT_URI, new String[]{"iconPackage"}, "iconPackage=?", new String[]{context.getPackageName()}, null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
            c.close();
        }
        return isInstallShortcut;
    }

    private static String getAuthorityFromPermission(ContextWrapper context, String permission) {
        if (TextUtils.isEmpty(permission)) {
            return "";
        }

        try {
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packs == null) {
                return "";
            }
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission) || permission.equals(provider.writePermission)) {
                            if (!TextUtils.isEmpty(provider.authority)// 精准匹配launcher.settings，再一次验证
                                    && (provider.authority).contains(".launcher.settings"))
                                return provider.authority;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getCurrentLauncherPackageName(ContextWrapper context) {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res == null || res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return "";
        }
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }
    }

    private static String getAuthority(ContextWrapper context) {
        // 获取默认
        String authority = getAuthorityFromPermission(context, "com.android.launcher.permission.READ_SETTINGS");
        // 获取特殊第三方
        if (authority == null || authority.trim().equals("")) {
            String packageName = getCurrentLauncherPackageName(context);
            packageName += ".permission.READ_SETTINGS";
            authority = getAuthorityFromPermission(context, packageName);
        }
        // 还是获取不到，直接写死
        if (TextUtils.isEmpty(authority)) {
            int sdkInt = android.os.Build.VERSION.SDK_INT;
            if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                authority = "com.android.launcher.settings";
            } else if (sdkInt < 19) {// Android 4.4以下
                authority = "com.android.launcher2.settings";
            } else {// 4.4以及以上
                authority = "com.android.launcher3.settings";
            }
        }
        return "content://" + authority + "/favorites?notify=true";
    }

    public static int dp2px(Context context, float dpVal) {
        float px = TypedValue.applyDimension(1, dpVal, context.getResources().getDisplayMetrics());
        BigDecimal decimal = (new BigDecimal((double)px)).setScale(0, 4);
        return decimal.intValue();
    }

    private static class BuildProperties {

        private final Properties properties;

        private BuildProperties() throws IOException {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public boolean containsKey(final Object key) {
            return properties.containsKey(key);
        }

        public boolean containsValue(final Object value) {
            return properties.containsValue(value);
        }

        public Set<Entry<Object, Object>> entrySet() {
            return properties.entrySet();
        }

        public String getProperty(final String name) {
            return properties.getProperty(name);
        }

        public String getProperty(final String name, final String defaultValue) {
            return properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty() {
            return properties.isEmpty();
        }

        public Enumeration<Object> keys() {
            return properties.keys();
        }

        public Set<Object> keySet() {
            return properties.keySet();
        }

        public int size() {
            return properties.size();
        }

        public Collection<Object> values() {
            return properties.values();
        }

        public static BuildProperties newInstance() throws IOException {
            return new BuildProperties();
        }

    }
}
