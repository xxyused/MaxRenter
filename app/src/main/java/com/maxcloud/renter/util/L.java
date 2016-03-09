package com.maxcloud.renter.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描    述：日志输出帮助类，提供日志打印，保存等操作。
 * 作    者：向晓阳
 * 时    间：2016/2/22
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class L {
    private static List<KeyValue> mUseLogs = new ArrayList<>();
    private static List<String> mErrLogs = new ArrayList<>();
    private static Runnable mUseRunnable;
    private static Runnable mErrRunnable;

    /**
     * 上传历史操作日志。
     */
    public static void updateHistoryUseLog() {
        File[] logFiles = findHistoryUseLog();
        if (null != logFiles && logFiles.length > 0) {
            for (File logFile : logFiles) {
                try {
                    //本地文件名：年-月-日_手机号_小时.log
                    String items[] = logFile.getName().split("_");
                    //服务器文件名：年-月-日/手机号_小时.log
                    String ossFileName = String.format("%s/%s_%s", items[0], items[1], items[2]);

                    AliOssHelper.uploadUse(logFile.getPath(), ossFileName);
                    logFile.delete();
                } catch (Exception e) {
                    L.e("uploadUse", e);
                }
            }
        }
    }

    /**
     * 输出普通日志。
     *
     * @param tag    日志tag。
     * @param format 日志内容。
     * @param args   日志内容参数。
     */
    public static void i(String tag, String format, Object... args) {
        String msg = String.format(format, args);
        try {
            Log.i(tag, msg);
        } catch (Exception e) {

        }

        //saveLogToFile(LEVEL_INFO, msg);
    }

    /**
     * 输出错误日志。
     *
     * @param tag 日志tag。
     * @param t   错误对象。
     */
    public static void e(String tag, Throwable t) {
        String message = t.getMessage();
        if (null == message) {
            message = "";
        }
        StackTraceElement[] stackTrace = t.getStackTrace();

//      Exception ex = new Exception(String.format("[%s]\t%s", tag, message));
//      ex.setStackTrace(stackTrace);
//      ex.initCause(t.getCause());
//
//      MobclickAgent.reportError(MainApplication.getContext(), ex);

        StringBuilder errorInfo = new StringBuilder(message);
        for (int i = 0; i < stackTrace.length; ++i) {
            String element = String.format("\n        at\t %s", stackTrace[i]);

            errorInfo.append(element);
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            LoginInfo loginInfo = LoginInfo.get();
            JSONObject logItem = new JSONObject();

            logItem.put("A", loginInfo.getAccount());// 当前账号
            logItem.put("C", 0);// 类型 0 错误日志，1 操作日志，2 运行日志
            logItem.put("D", format.format(MaxService.getServerTime()));
            logItem.put("V", AppHelper.getVersion());
            logItem.put("M", android.os.Build.MODEL);
            logItem.put("O", "Android " + android.os.Build.VERSION.RELEASE);
            logItem.put("S", tag);// 错误源
            logItem.put("T", errorInfo.toString());// 错误说明

            String logString = logItem.toString();
            Log.e(tag, errorInfo.toString());

            synchronized (mUseLogs) {
                mErrLogs.add(logString);
            }
        } catch (Exception e) {

        }

        startSaveErrorThread();
    }

    /**
     * 输出用户点击日志。
     *
     * @param context 点击上下文。
     * @param viewId  点击控件ID。
     */
    public static void useClick(Context context, int viewId) {
        String resName = context.getResources().getResourceName(viewId);
        String viewName = resName.split("/")[1];

        useClick(context, viewName);
    }

    /**
     * 输出用户点击日志。
     *
     * @param context  点击上下文。
     * @param viewName 点击控件名称。
     */
    public static void useClick(Context context, CharSequence viewName) {
        use(context, "Click", viewName);
    }

    /**
     * 输出用户点击日志。
     *
     * @param clsName  点击界面类名称。
     * @param title    点击界面标题。
     * @param viewName 点击控件名称。
     */
    public static void useClick(String clsName, CharSequence title, CharSequence viewName) {
        use(clsName, title, "Click", viewName);
    }

    /**
     * 输出界面恢复日志。
     *
     * @param clsName 界面类名称。
     * @param title   界面标题。
     */
    public static void useResume(String clsName, CharSequence title) {
        use(clsName, title, "Resume", null);
    }

    /**
     * 输出界面暂停日志。
     *
     * @param clsName 界面类名称。
     * @param title   界面标题。
     */
    public static void usePause(String clsName, CharSequence title) {
        use(clsName, title, "Pause", null);
    }

    // 保存操作日志。
    private static void use(Context context, CharSequence operate, CharSequence viewName) {
        try {
            String clsName = context.getClass().getSimpleName();
            CharSequence title = "";
            if (context instanceof Activity) {
                title = ((Activity) context).getTitle();
            }

            use(clsName, title, operate, viewName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 保存操作日志。
    private static void use(String clsName, CharSequence title, CharSequence operate, CharSequence viewName) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date localTime = new Date();
            Date serverTime = MaxService.getServerTime(localTime);
            LoginInfo loginInfo = LoginInfo.get();
            String fileName = dateToUseLogName(serverTime, loginInfo.getAccount());

            StringBuilder logItem = new StringBuilder(loginInfo.getAccount());// 当前账号
            logItem.append('\t');
            logItem.append(AppHelper.getVersion());//APP版本
            logItem.append('\t');
            logItem.append(clsName);// 操作视图
            logItem.append('\t');
            logItem.append(title);// 视图标题
            logItem.append('\t');
            logItem.append(operate);// 操作名称
            logItem.append('\t');
            logItem.append(TextUtils.isEmpty(viewName) ? "" : viewName);// 操作源
            logItem.append('\t');
            logItem.append(format.format(serverTime));//操作服务器时间
            logItem.append('\t');
            logItem.append(format.format(localTime));//操作本地时间

            String logString = logItem.toString();
            L.i("L.use", logString);

            synchronized (mUseLogs) {
                mUseLogs.add(new KeyValue(fileName, logString));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        startSaveUseThread();

//        Map<String, String> eventData = new HashMap<>();
//        eventData.put("account", LoginResult.get().getPhoneNo());
//
//        MobclickAgent.onEvent(context, idName, eventData);
    }

    // 查看历史操作日志。
    private static File[] findHistoryUseLog() {
        File logDir = new File(FileHelper.getUseLogDirectory());
        if (!logDir.exists()) {
            return null;
        }

        return logDir.listFiles(new FilenameFilter() {
            private String curFileDate = getDateAndHour(MaxService.getServerTime());

            private String getDateAndHour(Date time) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdf.format(time);
                int hours = time.getHours();

                return String.format("%s_%02d", dateStr, hours);
            }

            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".log") && filename.length() > 4) {
                    filename = filename.substring(0, filename.length() - 4);

                    String items[] = filename.split("_");
                    String fileDate = String.format("%s_%s", items[0], items[2]);

                    return fileDate.compareToIgnoreCase(curFileDate) < 0;
                }

                return false;
            }
        });
    }

    // 根据日期，生成带后缀的日志文件名。
    private static String dateToUseLogName(Date time, String phoneNo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(time);
        int hours = time.getHours();

        return String.format("%s_%s_%02d.log", dateStr, phoneNo, hours);
    }

    // 启动操作日志保存线程。
    private static void startSaveUseThread() {
        if (null != mUseRunnable) {
            return;
        }

        mUseRunnable = new Runnable() {

            @Override
            public void run() {
                File logDir = new File(FileHelper.getUseLogDirectory());
                if (!logDir.exists()) {
                    logDir.mkdirs();
                }

                try {
                    synchronized (mUseLogs) {
                        String writerName = null;
                        FileWriter fileWriter = null;

                        while (mUseLogs.size() > 0) {
                            KeyValue kv = mUseLogs.get(0);
                            mUseLogs.remove(0);

                            String fileName = kv.Key;
                            if (null == fileWriter || !fileName.equals(writerName)) {
                                if (null != fileWriter) {
                                    fileWriter.close();
                                    fileWriter = null;
                                }
                                writerName = fileName;
                                fileWriter = new FileWriter(new File(logDir, fileName), true);
                            }

                            fileWriter.write("\n");
                            fileWriter.write(kv.Value);
                            fileWriter.flush();
                        }

                        if (null != fileWriter) {
                            fileWriter.close();
                            fileWriter = null;
                        }
                    }
                } catch (Exception e) {

                }

                try {
                    // 上传历史操作日志。
                    updateHistoryUseLog();
                } catch (Exception e) {
                    L.e("updateHistoryUseLog", e);
                }

                mUseRunnable = null;
            }
        };

        new Thread(mUseRunnable).start();
    }

    // 启动错误日志保存线程。
    private static void startSaveErrorThread() {
        if (null != mErrRunnable) {
            return;
        }

        mErrRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (mErrLogs.size() > 0) {
                        String errLog;
                        synchronized (mErrLogs) {
                            errLog = mErrLogs.get(0);
                            mErrLogs.remove(0);
                        }

                        AliOssHelper.uploadError(errLog);
                    }
                } catch (Exception e) {
                }

                mErrRunnable = null;
            }
        };

        new Thread(mErrRunnable).start();
    }

    private static class KeyValue {
        public String Key;
        public String Value;

        public KeyValue(String key, String value) {
            Key = key;
            Value = value;
        }
    }
}
