package com.maxcloud.renter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.maxcloud.renter.R;
import com.maxcloud.renter.util.L;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描    述：Activity基础类，封闭显示进度条、关闭进度条、显示Toast、隐藏输入软键盘和友盟统计
 * 作    者：向晓阳
 * 时    间：2016/2/18
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class BaseActivity extends Activity {
    private ProgressDialog _proDialog;
    private Toast _toast;

    /**
     * 格式化日期为字符串。
     *
     * @param date 要格式化的日期。
     * @return
     */
    public CharSequence formatDate(Date date) {
        if (null == date) {
            return "";
        }

        SimpleDateFormat dateFmt = new SimpleDateFormat(
                getString(R.string.date_display_format));

        return dateFmt.format(date);
    }

    /**
     * 格式化时间为字符串。
     *
     * @param date 要格式化的日期。
     * @return
     */
    public CharSequence formatTime(Date date) {
        if (null == date) {
            return "";
        }

        SimpleDateFormat timeFmt = new SimpleDateFormat(
                getString(R.string.time_display_format));

        return timeFmt.format(date);
    }

    /**
     * 格式化时间为字符串。
     *
     * @param date 要格式化的日期。
     * @return
     */
    public CharSequence formatShortTime(Date date) {
        if (null == date) {
            return "";
        }

        SimpleDateFormat timeFmt = new SimpleDateFormat(
                getString(R.string.time_short_display_format));

        return timeFmt.format(date);
    }

    /**
     * 格式化日期为字符串。
     *
     * @param date 要格式化的日期。
     * @return
     */
    public CharSequence formatDateTime(Date date) {
        if (null == date) {
            return "";
        }

        SimpleDateFormat dateFmt = new SimpleDateFormat(String.format("%s %s",
                getString(R.string.date_display_format), getString(R.string.time_display_format)));

        return dateFmt.format(date);
    }

    /**
     * 格式化日期为字符串。
     *
     * @param date 要格式化的日期。
     * @return
     */
    public CharSequence formatShortDateTime(Date date) {
        if (null == date) {
            return "";
        }

        SimpleDateFormat dateFmt = new SimpleDateFormat(String.format("%s %s",
                getString(R.string.date_display_format), getString(R.string.time_short_display_format)));

        return dateFmt.format(date);
    }

    /**
     * 格式化日期字符串为日期。
     *
     * @param dateStr 要格式化的日期字符串。
     * @return
     * @throws ParseException
     */
    public Date formatDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFmt = new SimpleDateFormat(
                getString(R.string.date_display_format));
        return dateFmt.parse(dateStr);
    }

    /**
     * 隐藏输入软键盘。
     */
    public void hideSoftInput() {
        try {
            View curFocus = getCurrentFocus();
            if (null != curFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(curFocus.getWindowToken(), 0);
            }
        } catch (Exception e) {
            L.e("hideSoftInput", e);
        }
    }

    /**
     * 显示简单的提示。
     *
     * @param resId 提示内容。
     */
    public void showToast(@StringRes int resId) {
        if (null != _toast) {
            _toast.cancel();
        }
        _toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        _toast.show();
    }

    /**
     * 显示简单的错误提示。
     *
     * @param message 提示内容。
     */
    public void showError(String message) {
        if (null != _toast) {
            _toast.cancel();
        }
        _toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        _toast.show();
    }

    /**
     * 显示进度条。
     *
     * @param resId 进度条要显示的内容。
     */
    public void showProgressDialog(@StringRes int resId) {
        if (null == _proDialog) {
            //创建ProgressDialog对象
            _proDialog = new ProgressDialog(this);
            // 设置进度条风格，风格为圆形，旋转的
            _proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // 设置ProgressDialog 的进度条是否不明确
            _proDialog.setIndeterminate(true);
            // 设置ProgressDialog 是否可以按退回按键取消
            _proDialog.setCancelable(false);
        }

        // 设置ProgressDialog 标题
        //progressDialog.setTitle("提示");
        // 设置ProgressDialog 提示信息
        _proDialog.setMessage(getString(resId));
        // 设置ProgressDialog 标题图标
        //progressDialog.setIcon(R.drawable.a);
        //设置ProgressDialog 的一个Button
        //progressDialog.setButton("确定", new SureButtonListener());
        // 让ProgressDialog显示
        _proDialog.show();
    }

    /**
     * 关闭进度条。
     */
    public void closeProgressDialog() {
        if (null != _proDialog) {
            _proDialog.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //当默认统计被禁止时。
        if (!AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
            MobclickAgent.onPageStart(getClass().getSimpleName());
        }
        MobclickAgent.onResume(this);
        L.useResume(getClass().getSimpleName(), getTitle());
    }

    @Override
    protected void onPause() {
        super.onPause();

        //当默认统计被禁止时。
        if (!AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
            MobclickAgent.onPageEnd(getClass().getSimpleName());
        }
        MobclickAgent.onPause(this);
        L.usePause(getClass().getSimpleName(), getTitle());

        if (null != _toast) {
            _toast.cancel();
            _toast = null;
        }
    }
}
