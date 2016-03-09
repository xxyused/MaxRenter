package com.maxcloud.renter.activity.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.maxcloud.renter.R;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.util.L;

public class ResetPasswordActivity extends AccountActivity {
    private ResetPasswordTask _task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEdtPhoneNo.setText(LoginInfo.get().getPhoneNo());
        mEdtPhoneNo.setEnabled(false);
        mEdtPassword.requestFocus();

        _txvPasswordTitle.setText(R.string.new_password);
        mEdtPassword.setHint(R.string.new_password_hint);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (null != _task) {
            _task.cancel(true);
        }
    }

    @Override
    protected void submit() {
        InputData inputData = checkInputData(true);
        if (null != inputData) {
            cancelListenerCode();

            if (null != _task) {
                _task.cancel(true);
            }

            _task = new ResetPasswordTask(this);
            _task.execute(inputData);
        }
    }

    private static class ResetPasswordTask extends AsyncTask<InputData, Void, String> {
        private ResetPasswordActivity _activity;

        public ResetPasswordTask(ResetPasswordActivity activity) {
            _activity = activity;
        }

        @Override
        protected String doInBackground(InputData... params) {
            try {
                InputData inputData = params[0];
                MaxService service = MaxService.get();

                service.setPassword(inputData.PhoneNo, inputData.Password, inputData.SecurityCode);
            } catch (Exception e) {
                L.e("resetPassword", e);

                return e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            _activity.showProgressDialog(R.string.reset_password_submiting);
        }

        @Override
        protected void onPostExecute(String error) {
            _activity.closeProgressDialog();
            if (TextUtils.isEmpty(error)) {
                _activity.finish();
            } else {
                _activity.showError(error);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            _activity.closeProgressDialog();
        }
    }
}
