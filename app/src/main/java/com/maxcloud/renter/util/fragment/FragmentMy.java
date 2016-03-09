package com.maxcloud.renter.util.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.BaseFragment;
import com.maxcloud.renter.activity.setting.SettingActivity;
import com.maxcloud.renter.activity.user.ChangePasswordActivity;
import com.maxcloud.renter.activity.user.MyChildActivity;
import com.maxcloud.renter.activity.user.PreviewUserInfoActivity;
import com.maxcloud.renter.entity.user.CircleTransform;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.service.entity.StsInfoResult;
import com.maxcloud.renter.util.FileHelper;
import com.maxcloud.renter.util.L;

/**
 * Created by MAX-XXY on 2016/2/16.
 */
public class FragmentMy extends BaseFragment {
    private View _rootView;
    private ImageView _imgHead;
    private TextView _txvName;
    private TextView _txvAccount;

    private LoadUserInfoTask _task;
    private ClickListener _onClick;
    private Bitmap _portraitImage;

    @Override
    protected String getTitle() {
        return getString(R.string.my_title);
    }

    public FragmentMy() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == _rootView) {
            Context context = container.getContext();
            _onClick = new ClickListener(context);
            _rootView = View.inflate(context, R.layout.fragment_my, null);

            _imgHead = (ImageView) _rootView.findViewById(R.id.imgHead);
            _txvName = (TextView) _rootView.findViewById(R.id.txvName);
            _txvAccount = (TextView) _rootView.findViewById(R.id.txvPhoneNo);

            View btnChgPassword = _rootView.findViewById(R.id.btnChgPassword);
            View btnMyChild = _rootView.findViewById(R.id.btnMyChild);
            View btnSetting = _rootView.findViewById(R.id.btnSetting);

            btnChgPassword.setOnClickListener(_onClick);
            _imgHead.setOnClickListener(_onClick);
            btnMyChild.setOnClickListener(_onClick);
            btnSetting.setOnClickListener(_onClick);
        }

        return _rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        LoginInfo loginInfo = LoginInfo.get();
        if (null != loginInfo) {
            String account = loginInfo.getAccount();
            if ("unknown".equals(account)) {
                _txvName.setText(R.string.my_not_login);
                _txvAccount.setText(null);
            } else {
                _txvName.setText(loginInfo.getName());
                _txvAccount.setText(loginInfo.getAccount());
            }
        } else {
            _txvName.setText(null);
            _txvAccount.setText(null);
        }

        if (null != _task) {
            _task.cancel(true);
        }

        _task = new LoadUserInfoTask(this);
        _task.execute();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (null != _task) {
            _task.cancel(true);
        }

        if (null != _portraitImage) {
            _imgHead.setImageResource(R.mipmap.ic_no_head_img);

            _portraitImage.recycle();
            _portraitImage = null;
        }
    }

    private void updatePortrait(LoginInfo loginInfo) {
        if (null != _portraitImage) {
            _imgHead.setImageResource(R.mipmap.ic_no_head_img);

            _portraitImage.recycle();
            _portraitImage = null;
        }

        if (null != loginInfo) {
            String account = loginInfo.getAccount();
            if (!"unknown".equals(account) && FileHelper.existsUserPortrait()) {
                CircleTransform transform = new CircleTransform();
                Bitmap image = BitmapFactory.decodeFile(FileHelper.getUserPortrait());
                _imgHead.setImageBitmap(transform.transform(image));
                image.recycle();
            }
        }
    }

    private static class LoadUserInfoTask extends AsyncTask<Void, Void, LoginInfo> {
        private FragmentMy _fragment;

        public LoadUserInfoTask(FragmentMy fragment) {
            _fragment = fragment;
        }

        @Override
        protected LoginInfo doInBackground(Void... params) {
            LoginInfo loginInfo = LoginInfo.get();
            try {
                String account = loginInfo.getAccount();
                MaxService service = MaxService.get();
                StsInfoResult result = service.imageStsByGet(account, account, loginInfo.getIdCardNo(), MaxService.IMAGE_TYPE_PORTRAIT);
                String lastTime = result.getLastTime();

                if (!FileHelper.existsUserPortrait() || !lastTime.equals(loginInfo.getPortraitStamp())) {
                    if (FileHelper.downloadFile(result.getPath(), FileHelper.getUserPortrait())) {
                        loginInfo.setPortraitStamp(lastTime);

                        loginInfo.save();
                    }
                }
            } catch (Exception e) {
                L.e("downloadPortrait", e);
            }

            return loginInfo;
        }

        @Override
        protected void onPostExecute(LoginInfo loginInfo) {
            _fragment.updatePortrait(loginInfo);
        }
    }

    private static class ClickListener implements View.OnClickListener {
        private Context _context;

        public ClickListener(Context context) {
            _context = context;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            try {
                switch (viewId) {
                    case R.id.btnChgPassword:
                        _context.startActivity(new Intent(_context, ChangePasswordActivity.class));
                        break;
                    case R.id.imgHead:
                        _context.startActivity(new Intent(_context, PreviewUserInfoActivity.class));
                        break;
                    case R.id.btnMyChild:
                        _context.startActivity(new Intent(_context, MyChildActivity.class));
                        break;
                    case R.id.btnSetting:
                        _context.startActivity(new Intent(_context, SettingActivity.class));
                        break;
                }
            } catch (Exception e) {
                L.e("onClick", e);
            }

            L.useClick(v.getContext(), viewId);
        }
    }
}
