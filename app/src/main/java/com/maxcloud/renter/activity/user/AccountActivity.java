package com.maxcloud.renter.activity.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.CustomTitleActivity;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.PhoneNoHelper;
import com.maxcloud.renter.util.watcher.PhoneNoWatcher;

/**
 * 描    述：账号管理Activity，实现账号管理基本功能。
 * 作    者：向晓阳
 * 时    间：2016/2/22
 * 版    权：迈斯云门禁网络科技有限公司
 */
public abstract class AccountActivity extends CustomTitleActivity {
    private static final int WHAT_COUNT_DOWN = 1;
    private static final int WHAT_CANCEL_COUNT_DOWN = 2;

    protected EditText mEdtPhoneNo;
    protected TextView _txvPasswordTitle;
    protected EditText mEdtPassword;
    protected EditText mEdtSecurityCode;
    protected Button mBtnGetSecurityCode;
    protected TextView mTxvSendTip;
    protected Button mBtnOk;

    protected ClickListener mOnClick = new ClickListener(this);

    private boolean mIsGetSecurityCode = false;
    private int mRemainderTime = 90;
    private MessageHandler _handler = new MessageHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mEdtPhoneNo = (EditText) findViewById(R.id.edtPhoneNo);
        _txvPasswordTitle = (TextView) findViewById(R.id.txvPasswordTitle);
        mEdtPassword = (EditText) findViewById(R.id.edtPassword);
        mEdtSecurityCode = (EditText) findViewById(R.id.edtSecurityCode);
        mTxvSendTip = (TextView) findViewById(R.id.txvSendTip);

        mBtnGetSecurityCode = (Button) findViewById(R.id.btnGetSecurityCode);
        mBtnOk = (Button) findViewById(R.id.btnOk);

        mEdtPhoneNo.addTextChangedListener(new PhoneNoWatcher(mEdtPhoneNo));
        mBtnGetSecurityCode.setOnClickListener(mOnClick);
        mBtnOk.setOnClickListener(mOnClick);
    }

    protected abstract void submit();

    protected InputData checkInputData(boolean checkSecurityCode) {
        InputData inputData = new InputData();
        View focusView = null;

        inputData.PhoneNo = PhoneNoHelper.parsePhoneNo(mEdtPhoneNo.getText());
        inputData.Password = mEdtPassword.getText().toString();

        if (TextUtils.isEmpty(inputData.PhoneNo)) {
            mEdtPhoneNo.setError(getString(R.string.phone_no_empty));
            focusView = mEdtPhoneNo;
        } else {
            if (!PhoneNoHelper.IsValidMobileNo(inputData.PhoneNo)) {
                mEdtPhoneNo.setError(getString(R.string.phone_no_invalid));
                focusView = mEdtPhoneNo;
            }
        }

        if (TextUtils.isEmpty(inputData.Password)) {
            mEdtPassword.setError(getString(R.string.password_empty));
            if (null == focusView) {
                focusView = mEdtPassword;
            }
        }

        if (checkSecurityCode) {
            String codeStr = mEdtSecurityCode.getText().toString();

            if (TextUtils.isEmpty(codeStr)) {
                mEdtSecurityCode.setError(getString(R.string.security_code_empty));
                if (null == focusView) {
                    focusView = mEdtSecurityCode;
                }
            } else {
                try {
                    inputData.SecurityCode = Integer.valueOf(codeStr);
                    if (inputData.SecurityCode <= 0) {
                        mEdtSecurityCode.setError(getString(R.string.security_code_invalid));
                        if (null == focusView) {
                            focusView = mEdtSecurityCode;
                        }
                    }
                } catch (Exception e) {
                    mEdtSecurityCode.setError(getString(R.string.security_code_invalid));
                    if (null == focusView) {
                        focusView = mEdtSecurityCode;
                    }
                }
            }
        }

        if (null != focusView) {
            focusView.requestFocus();
            return null;
        } else {
            return inputData;
        }
    }

    protected void cancelListenerCode() {
        if (!mIsGetSecurityCode) {
            return;
        }

        _handler.removeMessages(WHAT_COUNT_DOWN);

        mIsGetSecurityCode = false;

        mBtnGetSecurityCode.setText("重新获取");
        mBtnGetSecurityCode.setEnabled(true);
    }

    private void beginListenerCode() {
        mBtnGetSecurityCode.setEnabled(false);
        mIsGetSecurityCode = true;
        mRemainderTime = 90;
        mTxvSendTip.setVisibility(View.VISIBLE);
        _handler.sendEmptyMessage(WHAT_COUNT_DOWN);
    }

    private void updateRemainderTime() {
        if (mIsGetSecurityCode) {
            mBtnGetSecurityCode.setText(String.format("已发送(%d)", mRemainderTime));
            mRemainderTime--;
            if (mRemainderTime > 0) {
                _handler.sendEmptyMessageDelayed(WHAT_COUNT_DOWN, 1000);
            } else {
                cancelListenerCode();
            }
        } else {
            cancelListenerCode();
        }
    }

    private void getSecurityCode() {
        if (mIsGetSecurityCode) {
            return;
        }

        InputData inputData = checkInputData(false);
        if (null != inputData) {
            new SendSecurityCodeTask(this).execute(inputData.PhoneNo);
        }
    }

    protected static class InputData {
        public String PhoneNo;
        public String Password;
        public int SecurityCode;
    }

    private static class ClickListener implements View.OnClickListener {
        private AccountActivity _activity;

        public ClickListener(AccountActivity activity) {
            _activity = activity;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();

            try {
                switch (viewId) {
                    case R.id.btnGetSecurityCode:
                        _activity.getSecurityCode();
                        break;
                    case R.id.txvAgreement:
                        _activity.startActivity(new Intent(_activity, UserAgreementActivity.class));
                        break;
                    case R.id.btnOk:
                        _activity.submit();
                        break;
                }
            } catch (Exception e) {
                L.e("onClick", e);

                _activity.showError(e.getMessage());
            }

            L.useClick(v.getContext(), viewId);
        }
    }

    private static class MessageHandler extends Handler {
        private AccountActivity _context;

        public MessageHandler(AccountActivity context) {
            _context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_COUNT_DOWN:
                    _context.updateRemainderTime();
                    break;
                case WHAT_CANCEL_COUNT_DOWN:
                    _context.cancelListenerCode();
                    break;
            }
        }
    }

    private static class SendSecurityCodeTask extends AsyncTask<String, Void, String> {
        private AccountActivity _activity;

        public SendSecurityCodeTask(AccountActivity activity) {
            _activity = activity;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                MaxService service = MaxService.get();
                service.sendSecurityCode(params[0], MaxService.CHECK_TYPE_REGISTERED);
            } catch (Exception e) {
                L.e("sendSecurityCode", e);

                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            _activity.showProgressDialog(R.string.account_sending_security_code);
            _activity.mTxvSendTip.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(String error) {
            _activity.closeProgressDialog();

            if (TextUtils.isEmpty(error)) {
                _activity.showError(error);
            } else {
                _activity.beginListenerCode();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            _activity.closeProgressDialog();
        }
    }
}
