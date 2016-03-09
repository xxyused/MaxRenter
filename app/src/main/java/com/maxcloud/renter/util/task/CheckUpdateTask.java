package com.maxcloud.renter.util.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.QuestionDialog;
import com.maxcloud.renter.util.AppHelper;
import com.maxcloud.renter.util.L;

/**
 * 描    述：检查更新。
 * 作    者：向晓阳
 * 时    间：2016/3/5
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class CheckUpdateTask extends AsyncTask<Void, Void, AppHelper.UpdateInfo> {
    private Activity _activity;
    ProgressDialog _proDialog;

    public CheckUpdateTask(Activity activity) {
        _activity = activity;
    }

    @Override
    protected AppHelper.UpdateInfo doInBackground(Void... params) {
        //检查新版本。
        try {
            AppHelper.UpdateInfo updateInfo = AppHelper.getUpdateInfo();

            return updateInfo;
        } catch (Exception e) {
            L.e("getUpdateInfo", e);
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        //创建ProgressDialog对象
        _proDialog = new ProgressDialog(_activity);
        // 设置进度条风格，风格为圆形，旋转的
        _proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置ProgressDialog 的进度条是否不明确
        _proDialog.setIndeterminate(true);
        // 设置ProgressDialog 是否可以按退回按键取消
        _proDialog.setCancelable(false);

        // 设置ProgressDialog 标题
        //progressDialog.setTitle("提示");
        // 设置ProgressDialog 提示信息
        //_proDialog.setMessage(getString(resId));
        // 设置ProgressDialog 标题图标
        //progressDialog.setIcon(R.drawable.a);
        //设置ProgressDialog 的一个Button
        //progressDialog.setButton("确定", new SureButtonListener());
        // 让ProgressDialog显示
        _proDialog.show();
    }

    @Override
    protected void onPostExecute(AppHelper.UpdateInfo updateInfo) {
        _proDialog.cancel();

        if (AppHelper.hasNewMinVersion(updateInfo)) {
            new QuestionDialog(_activity, R.string.welcome_new_update,
                    R.string.welcome_new_update, null).show();
        }
    }
}
