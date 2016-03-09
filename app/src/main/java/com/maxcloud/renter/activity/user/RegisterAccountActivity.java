package com.maxcloud.renter.activity.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.maxcloud.renter.R;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.util.L;

public class RegisterAccountActivity extends AccountActivity {
    private RegisterAccountTask _task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View layoutAgreement = findViewById(R.id.layoutAgreement);
        CheckBox chkAgreement = (CheckBox) findViewById(R.id.chkAgreement);
        View txvAgreement = findViewById(R.id.txvAgreement);

        txvAgreement.setOnClickListener(mOnClick);
        chkAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setEnabled(isChecked);
            }
        });

        layoutAgreement.setVisibility(View.VISIBLE);
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

            _task = new RegisterAccountTask(this);
            _task.execute(inputData);
        }
    }

    private static class RegisterAccountTask extends AsyncTask<InputData, Void, String> {
        private RegisterAccountActivity _activity;

        public RegisterAccountTask(RegisterAccountActivity activity) {
            _activity = activity;
        }

        @Override
        protected String doInBackground(InputData... params) {
            try {
                InputData inputData = params[0];
                MaxService service = MaxService.get();

                service.register(inputData.PhoneNo, inputData.Password, inputData.SecurityCode);
            } catch (Exception e) {
                L.e("register", e);

                return e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            _activity.showProgressDialog(R.string.register_password_submiting);
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
