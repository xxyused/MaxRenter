package com.maxcloud.renter.activity.setting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.CustomTitleActivity;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.AliOssHelper;

public class FeedbackActivity extends CustomTitleActivity {
    private EditText mEdtText;
    private EditText mEdtTel;

    private ClickListener mOnClick = new ClickListener(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mEdtText = (EditText) findViewById(R.id.edtText);
        mEdtTel = (EditText) findViewById(R.id.edtTel);
        View btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(mOnClick);

        LoginInfo loginInfo = LoginInfo.get();
        mEdtTel.setText(loginInfo.getPhoneNo());
    }

    private String getFeedbackText() {
        return mEdtText.getText().toString().trim();
    }

    private String getFeedbackTel() {
        return mEdtTel.getText().toString().trim();
    }

    private InputData checkInputData() {
        InputData inputData = new InputData();
        View focusView = null;

        inputData.Text = getFeedbackText();
        inputData.Tel = getFeedbackTel();

        if (TextUtils.isEmpty(inputData.Text)) {
            mEdtText.setError(getString(R.string.feedback_text_empty_error));
            focusView = mEdtText;
        }

        if (TextUtils.isEmpty(inputData.Tel)) {
            mEdtTel.setError(getString(R.string.feedback_tel_empty_error));
            if (null == focusView) {
                focusView = mEdtTel;
            }
        }

        if (null == focusView) {
            return inputData;
        } else {
            focusView.requestFocus();
            return null;
        }
    }

    private static class InputData {
        public String Text;
        public String Tel;
    }

    private static class ClickListener implements View.OnClickListener {
        private FeedbackActivity _activity;

        public ClickListener(FeedbackActivity activity) {
            _activity = activity;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();

            try {
                switch (viewId) {
                    case R.id.btnOk:
                        InputData inputData = _activity.checkInputData();
                        if (null == inputData) {
                            return;
                        }

                        _activity.showProgressDialog(R.string.feedback_submiting);

                        new AsyncTask<InputData, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(InputData... params) {
                                if (null == params || params.length < 1) {
                                    return false;
                                }

                                try {
                                    InputData inputData = params[0];
                                    AliOssHelper.uploadFeedback(inputData.Tel, inputData.Text);

                                    return true;
                                } catch (Exception e) {
                                    L.e("uploadFeedback", e);

                                    return false;
                                }
                            }

                            protected void onPostExecute(Boolean result) {
                                if (result) {
                                    _activity.closeProgressDialog();
                                    _activity.showToast(R.string.feedback_submit_success);

                                    _activity.finish();
                                } else {
                                    _activity.closeProgressDialog();
                                    _activity.showToast(R.string.feedback_submit_fail);
                                }
                            }
                        }.execute(inputData);
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
