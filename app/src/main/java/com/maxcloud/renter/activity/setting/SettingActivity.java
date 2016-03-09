package com.maxcloud.renter.activity.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.CustomTitleActivity;
import com.maxcloud.renter.activity.QuestionDialog;
import com.maxcloud.renter.activity.build.ActiveCardActivity;
import com.maxcloud.renter.activity.home.WelcomeActivity;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.util.FileHelper;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.OSHelper;

public class SettingActivity extends CustomTitleActivity {
    private ClickListener mOnClick = new ClickListener(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        View btnBindCard = findViewById(R.id.btnBindCard);
        View btnCreateShortcut = findViewById(R.id.btnCreateShortcut);
        View btnAbout = findViewById(R.id.btnAbout);
        View btnLogout = findViewById(R.id.btnLogout);

        btnBindCard.setOnClickListener(mOnClick);
        btnCreateShortcut.setOnClickListener(mOnClick);
        btnAbout.setOnClickListener(mOnClick);
        btnLogout.setOnClickListener(mOnClick);
    }

    private static class ClickListener implements View.OnClickListener {
        private SettingActivity _activity;

        public ClickListener(SettingActivity activity) {
            _activity = activity;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();

            try {
                switch (viewId) {
                    case R.id.btnBindCard:
                        _activity.startActivity(new Intent(_activity, ActiveCardActivity.class));
                        break;
                    case R.id.btnCreateShortcut:
                        final QuestionDialog dialog1 = new QuestionDialog(_activity, R.string.setting_create_shortcut,
                                R.string.setting_create_shortcut_message, new CreateShortcutButton(_activity),
                                new QuestionDialog.DialogButton(R.string.setting_create_shortcut_no));

                        dialog1.show();
                        break;
                    case R.id.btnAbout:
                        _activity.startActivity(new Intent(_activity, AboutActivity.class));
                        break;
                    case R.id.btnLogout:
                        final QuestionDialog dialog2 = new QuestionDialog(_activity, R.string.setting_logout,
                                R.string.setting_logout_message, new LogoutButton(_activity),
                                new QuestionDialog.DialogButton(R.string.setting_logout_no));
                        dialog2.show();
                        break;
                }
            } catch (Exception e) {
                L.e("onClick", e);

                _activity.showError(e.getMessage());
            }

            L.useClick(v.getContext(), viewId);
        }
    }

    private static class CreateShortcutButton extends QuestionDialog.DialogButton {
        private SettingActivity _activity;

        public CreateShortcutButton(SettingActivity activity) {
            super(R.string.setting_create_shortcut_yes);

            _activity = activity;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                OSHelper.addShortcut(_activity);

                _activity.showToast(R.string.setting_create_shortcut_success);
            } catch (Exception e) {
                L.e("addShortcut", e);
            }

            super.onClick(dialog, which);
        }
    }

    private static class LogoutButton extends QuestionDialog.DialogButton {
        private SettingActivity _activity;

        public LogoutButton(SettingActivity activity) {
            super(R.string.setting_logout);

            _activity = activity;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                MaxService service = MaxService.get();
                service.logout();

                FileHelper.clearAllData();
                LoginInfo.clear();
//                                    ConnectHelper.logout();
//
//                                    try {
//                                        BuildHouseHelper.clearAllBuild();
//                                        LoginHelper.clearLastLogin();
//                                    } catch (Exception e) {
//                                        L.e(TAG, e);
//                                    }
//
//                                    mActivity.finish();
                Intent intent = new Intent(_activity, WelcomeActivity.class);
                //添加下面标记是防止显示登录窗体后，用户按返回，又回到主界面。
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                _activity.startActivity(intent);
            } catch (Exception e) {
                L.e("addShortcut", e);
            }

            super.onClick(dialog, which);
        }
    }
}
