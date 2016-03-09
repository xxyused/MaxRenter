package com.maxcloud.renter.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.BaseActivity;
import com.maxcloud.renter.activity.MainActivity;
import com.maxcloud.renter.activity.QuestionDialog;
import com.maxcloud.renter.activity.user.LoginActivity;
import com.maxcloud.renter.activity.user.RegisterAccountActivity;
import com.maxcloud.renter.entity.build.DoorInfo;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.service.entity.LoginResult;
import com.maxcloud.renter.util.AppHelper;
import com.maxcloud.renter.util.FileHelper;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.task.LoginTask;
import com.maxcloud.renter.util.task.UpdateApkTask;

import java.io.File;
import java.util.List;

public class WelcomeActivity extends BaseActivity implements UpdateApkTask.OnReportProgress, LoginTask.OnLoginProgress {
    private TextView _txvTip;
    private ProgressBar _prbUpdate;
    private View _btnRegister;
    private View _btnLogin;

    private OnClickListener _onClick = new OnClickListener(this);
    private CheckUpdateTask _checkTask;
    private UpdateApkTask _updateTask;
    private LoginTask _loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        _txvTip = (TextView) findViewById(R.id.txvTip);
        _prbUpdate = (ProgressBar) findViewById(R.id.prbUpdate);
        _btnRegister = findViewById(R.id.btnRegister);
        _btnLogin = findViewById(R.id.btnLogin);

        _btnRegister.setOnClickListener(_onClick);
        _btnLogin.setOnClickListener(_onClick);

        if (null != _checkTask) {
            _checkTask.cancel(true);
        }

        _checkTask = new CheckUpdateTask(this);
        _checkTask.execute();
    }

    /**
     * 更新提示信息。
     *
     * @param resId 提示信息资料ID。
     */
    public void updateTip(@StringRes int resId) {
        if (View.VISIBLE != _txvTip.getVisibility()) {
            _txvTip.setVisibility(View.VISIBLE);
            _prbUpdate.setVisibility(View.INVISIBLE);
            _btnRegister.setVisibility(View.GONE);
            _btnLogin.setVisibility(View.GONE);
        }

        _txvTip.setText(resId);
    }

    /**
     * 显示注册，登录按钮。
     */
    public void showRegisterLoginButton() {
        _txvTip.setVisibility(View.GONE);
        _prbUpdate.setVisibility(View.GONE);
        _btnRegister.setVisibility(View.VISIBLE);
        _btnLogin.setVisibility(View.VISIBLE);
    }

    /**
     * 显示更新询问对话框。
     */
    public void showUpdateQueries(AppHelper.UpdateInfo updateInfo) {
        //只有强制升级时，才提示升级。
        if (AppHelper.hasNewMinVersion(updateInfo)) {
            if (null == updateInfo || TextUtils.isEmpty(updateInfo.getUpdateDesc())) {
                new QuestionDialog(this, R.string.welcome_new_update,
                        R.string.welcome_new_update, new UpdateNowButton(this),
                        new ExitSysButton(this)).show();
            } else {
                new QuestionDialog(this, R.string.welcome_new_update,
                        R.string.welcome_new_update_tip, new UpdateNowButton(this),
                        new ExitSysButton(this), updateInfo.getUpdateDesc()).show();
            }
        } else {
            if (null != _loginTask) {
                _loginTask.cancel(true);
                _loginTask = null;
            }
            if (MaxService.existAccount()) {
                _loginTask = new LoginTask(this);
                _loginTask.execute();
            } else {
                showRegisterLoginButton();
            }
        }
    }

    /**
     * 启动APK下载任务。
     */
    public void startUpdateApk() {
        if (null != _updateTask) {
            _updateTask.cancel(true);
        }

        _updateTask = new UpdateApkTask(this);
        _updateTask.execute();
    }

    @Override
    public void onPreUpdate() {
        updateTip(R.string.welcome_updating);
        _prbUpdate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressUpdate(int progress) {
        _prbUpdate.setProgress(progress);
    }

    @Override
    public void onPostUpdate(Exception e) {
        _prbUpdate.setVisibility(View.GONE);
        if (null == e) {
            File apkFile = new File(FileHelper.getUpdateApk());
            if (apkFile.exists()) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");

                startActivity(intent);
            }
        } else {
            showError(e.getMessage());
        }
    }

    @Override
    public void onPreLogin() {
        updateTip(R.string.welcome_logging);
    }

    @Override
    public void onPostLogin(Exception e) {
        if (null == e) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            L.e("autoLogin", e);
            showError(getString(R.string.welcome_login_fail, e.getMessage()));
            showRegisterLoginButton();
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (null != _checkTask) {
//            _checkTask.cancel(true);
//        }
//
//        _checkTask = new CheckUpdateTask(this);
//        _checkTask.execute();
//    }

    private static class CheckUpdateTask extends AsyncTask<Void, Void, AppHelper.UpdateInfo> {
        private WelcomeActivity _activity;

        public CheckUpdateTask(WelcomeActivity activity) {
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
            _activity.updateTip(R.string.welcome_check_version);
        }

        @Override
        protected void onPostExecute(AppHelper.UpdateInfo updateInfo) {
            _activity.showUpdateQueries(updateInfo);
        }
    }

    private static class UpdateNowButton extends QuestionDialog.DialogButton {
        private WelcomeActivity _activity;

        public UpdateNowButton(WelcomeActivity activity) {
            super(R.string.welcome_update_yes);

            _activity = activity;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            _activity.startUpdateApk();

            super.onClick(dialog, which);
        }
    }

    private static class ExitSysButton extends QuestionDialog.DialogButton {
        private WelcomeActivity _activity;

        public ExitSysButton(WelcomeActivity activity) {
            super(R.string.welcome_update_no);

            _activity = activity;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            super.onClick(dialog, which);

            _activity.finish();
        }
    }

    private static class AutoLoginTask extends AsyncTask<Void, Integer, LoginResult> {
        private WelcomeActivity _activity;

        public AutoLoginTask(WelcomeActivity activity) {
            _activity = activity;
        }

        @Override
        protected LoginResult doInBackground(Void... params) {
            //自动登录。
            try {
                MaxService service = MaxService.get();
                LoginResult loginResult = service.login();

                return loginResult;

            } catch (Exception e) {
                L.e("autoLogin", e);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            _activity.updateTip(R.string.welcome_logging);
        }

        @Override
        protected void onPostExecute(LoginResult loginResult) {
            if (null == loginResult) {
                _activity.showRegisterLoginButton();
            } else {
                LoginInfo loginInfo = LoginInfo.get();

                loginInfo.setPhoneNo(loginResult.getPhone());
                loginInfo.setName(loginResult.getName());
                loginInfo.setIdCardNo(loginResult.getCertificate());
                loginInfo.clearDoor();
                List<List<String>> doorInfos = loginResult.getDoors();
                if (null != doorInfos) {
                    for (List<String> items : doorInfos) {
                        DoorInfo doorInfo = new DoorInfo();
                        doorInfo.setServerId(items.get(0));
                        doorInfo.setId(Integer.valueOf(items.get(1)));
                        doorInfo.setName(items.get(2));

                        loginInfo.addDoor(doorInfo);
                    }
                }

                loginInfo.save();

                _activity.startActivity(new Intent(_activity, MainActivity.class));
            }
        }
    }

    private static class OnClickListener implements View.OnClickListener {
        private WelcomeActivity _activity;

        public OnClickListener(WelcomeActivity activity) {
            _activity = activity;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            switch (viewId) {
                case R.id.btnRegister:
                    _activity.startActivity(new Intent(_activity, RegisterAccountActivity.class));
                    break;
                case R.id.btnLogin:
                    _activity.startActivity(new Intent(_activity, LoginActivity.class));
                    break;
            }
        }
    }
}
