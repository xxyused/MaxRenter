package com.maxcloud.renter.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.CustomTitleActivity;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.service.entity.StsInfoResult;
import com.maxcloud.renter.service.entity.UserInfoResult;
import com.maxcloud.renter.util.FileHelper;
import com.maxcloud.renter.util.L;

public class PreviewUserInfoActivity extends CustomTitleActivity {
    private ImageView _imgIdCard;
    private EditText _edtName;
    private EditText _edtIdNo;
    private EditText _edtAddress;
    private View _btnOk;

    private LoadUserInfoTask _task;
    private ClickListener _onClick = new ClickListener(this);
    private Bitmap _idCardImage;

    @Override
    public void showTitleProgress() {
        super.showTitleProgress();

        _btnOk.setEnabled(false);
    }

    @Override
    public void hideTitleProgress() {
        super.hideTitleProgress();

        _btnOk.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        _imgIdCard = (ImageView) findViewById(R.id.imgIdCard);
        _edtName = (EditText) findViewById(R.id.edtName);
        _edtIdNo = (EditText) findViewById(R.id.edtIdNo);
        _edtAddress = (EditText) findViewById(R.id.edtAddress);
        _btnOk = findViewById(R.id.btnOk);

        _btnOk.setOnClickListener(_onClick);

        if (null != _task) {
            _task.cancel(true);
        }

        _task = new LoadUserInfoTask(this);
        _task.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != _task) {
            _task.cancel(true);
        }
        if (null != _idCardImage) {
            _imgIdCard.setImageBitmap(null);
            _idCardImage.recycle();
            _idCardImage = null;
        }
    }

    public void updateUserInfo(PreviewData data, String error) {
        if (null != _idCardImage) {
            _imgIdCard.setImageBitmap(null);
            _idCardImage.recycle();
            _idCardImage = null;
        }

        if (FileHelper.existsUserIdCard()) {
            _idCardImage = BitmapFactory.decodeFile(FileHelper.getUserIdCard());
            _imgIdCard.setImageBitmap(_idCardImage);
        }
        if (null != data) {
            _edtName.setText(data.Name);
            _edtIdNo.setText(data.IdCardNo);
            _edtAddress.setText(data.Address);
        } else {
            _edtName.setText(null);
            _edtIdNo.setText(null);
            _edtAddress.setText(null);
        }

        if (!TextUtils.isEmpty(error)) {
            showError(getString(R.string.user_info_load_error, error));
        }
    }

    private static class PreviewData {
        public String Name;
        public String IdCardNo;
        public String Address;
        public String Error;
    }

    private static class LoadUserInfoTask extends AsyncTask<Void, Void, PreviewData> {
        private PreviewUserInfoActivity _activity;

        public LoadUserInfoTask(PreviewUserInfoActivity fragment) {
            _activity = fragment;
        }

        @Override
        protected PreviewData doInBackground(Void... params) {
            PreviewData data = new PreviewData();

            try {
                LoginInfo loginInfo = LoginInfo.get();
                String account = loginInfo.getAccount();
                MaxService service = MaxService.get();
                UserInfoResult result = service.getUserInfo(LoginInfo.get().getAccount());
                String idCardNo = result.getIdCardNo();

                data.Name = result.getName();
                data.IdCardNo = idCardNo;
                data.Address = result.getAddress();

                StsInfoResult stsResult = service.imageStsByGet(account, account, idCardNo, MaxService.IMAGE_TYPE_ID_CARD);
                String lastTime = stsResult.getLastTime();
                String idCardUri = stsResult.getPath();

                try {
                    if (!FileHelper.existsUserIdCard() || !lastTime.equals(loginInfo.getIdCardStamp())) {
                        if (FileHelper.downloadFile(idCardUri, FileHelper.getUserIdCard())) {
                            loginInfo.setIdCardStamp(lastTime);

                            loginInfo.save();
                        }
                    }
                } catch (Exception e) {
                    L.e("downloadIdCard", e);

                    data.Error = _activity.getString(R.string.user_info_preview_download_id_card_error);
                }
            } catch (Exception e) {
                L.e("getUserInfo", e);

                data.Error = e.getMessage();
            }

            return data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            _activity.showTitleProgress();
        }

        @Override
        protected void onPostExecute(PreviewData userInfo) {
            super.onPostExecute(userInfo);

            _activity.updateUserInfo(userInfo, userInfo.Error);

            _activity.hideTitleProgress();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            _activity.closeProgressDialog();
        }
    }

    private static class ClickListener implements View.OnClickListener {
        private PreviewUserInfoActivity _activity;

        public ClickListener(PreviewUserInfoActivity activity) {
            _activity = activity;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();

            try {
                switch (viewId) {
                    case R.id.btnOk:
                        _activity.startActivity(new Intent(_activity, EditUserInfoActivity.class));
                        break;
                }
            } catch (Exception e) {
                L.e("onClick", e);

                _activity.showError(e.getMessage());
            }

            L.useClick(v.getContext(), viewId);
        }
    }
}
