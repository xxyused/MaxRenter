package com.maxcloud.renter.activity.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.CustomTitleActivity;
import com.maxcloud.renter.activity.QuestionDialog;
import com.maxcloud.renter.activity.user.UserAgreementActivity;
import com.maxcloud.renter.util.AppHelper;
import com.maxcloud.renter.util.FileHelper;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.task.UpdateApkTask;

import java.io.File;

public class AboutActivity extends CustomTitleActivity implements UpdateApkTask.OnReportProgress {
    private ProgressBar mPrbUpdate;

    private ClickListener mOnClick = new ClickListener(this);
    private UpdateApkTask _task;

    @Override
    public void onPostUpdate(Exception e) {
        mPrbUpdate.setVisibility(View.GONE);
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

    /**
     * 检查更新。
     */
    public void checkUpdate() {
        if (mPrbUpdate.getVisibility() == View.VISIBLE) {
            return;
        }

        new CheckUpdateTask(this).execute();
    }

    /**
     * 启动APK下载任务。
     */
    public void startUpdateApk() {
        if (null != _task) {
            _task.cancel(true);
        }

        _task = new UpdateApkTask(this);
        _task.execute();
    }

    /**
     * 显示更新询问对话框。
     */
    public void showUpdateQueries(AppHelper.UpdateInfo updateInfo) {
//        if (!AppHelper.hasNewVersion(updateInfo)) {
//            showToast(R.string.setting_not_new_update);
//            return;
//        }

        String updateDesc = "";
        if (null != updateInfo) {
            updateDesc = updateInfo.getUpdateDesc();
        }

        new QuestionDialog(this, R.string.setting_new_update,
                R.string.setting_new_update_tip, new UpdateNowButton(this),
                new QuestionDialog.DialogButton(R.string.setting_update_no),
                updateDesc).show();
    }

    @Override
    public void onPreUpdate() {
        mPrbUpdate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressUpdate(int progress) {
        mPrbUpdate.setProgress(progress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View btnCheckUpdate = findViewById(R.id.btnCheckUpdate);
        TextView txvVersion = (TextView) findViewById(R.id.txvVersion);
        mPrbUpdate = (ProgressBar) findViewById(R.id.prbUpdate);
        View btnFeedback = findViewById(R.id.btnFeedback);
        View btnContactCustomerService = findViewById(R.id.btnContactCustomerService);
        View btnAgreement = findViewById(R.id.btnAgreement);

        btnCheckUpdate.setOnClickListener(mOnClick);
        btnFeedback.setOnClickListener(mOnClick);
        btnContactCustomerService.setOnClickListener(mOnClick);
        btnAgreement.setOnClickListener(mOnClick);

        txvVersion.setText(AppHelper.getVersion());
    }

    private static class ClickListener implements View.OnClickListener {
        private AboutActivity _activity;

        public ClickListener(AboutActivity activity) {
            _activity = activity;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();

            try {
                switch (viewId) {
                    case R.id.btnCheckUpdate:
                        _activity.checkUpdate();
                        break;
                    case R.id.btnFeedback:
                        _activity.startActivity(new Intent(_activity, FeedbackActivity.class));
                        break;
                    case R.id.btnContactCustomerService:
                        _activity.startActivity(new Intent(_activity, CustomerServiceActivity.class));
                        break;
                    case R.id.btnAgreement:
                        _activity.startActivity(new Intent(_activity, UserAgreementActivity.class));
                        break;
                }
            } catch (Exception e) {
                L.e("onClick", e);

                _activity.showError(e.getMessage());
            }

            L.useClick(v.getContext(), viewId);
        }
    }

    private static class CheckUpdateTask extends AsyncTask<Void, Void, AppHelper.UpdateInfo> {
        private AboutActivity _activity;

        public CheckUpdateTask(AboutActivity activity) {
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
            _activity.showProgressDialog(R.string.setting_checking_update);
        }

        @Override
        protected void onPostExecute(AppHelper.UpdateInfo updateInfo) {
            _activity.closeProgressDialog();
            _activity.showUpdateQueries(updateInfo);
        }
    }

    private static class UpdateNowButton extends QuestionDialog.DialogButton {
        private AboutActivity _activity;

        public UpdateNowButton(AboutActivity activity) {
            super(R.string.setting_update_yes);

            _activity = activity;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            _activity.startUpdateApk();

            super.onClick(dialog, which);
        }
    }
}
