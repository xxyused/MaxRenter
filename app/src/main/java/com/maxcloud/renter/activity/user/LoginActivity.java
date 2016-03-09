package com.maxcloud.renter.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.BaseActivity;
import com.maxcloud.renter.activity.MainActivity;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.PhoneNoHelper;
import com.maxcloud.renter.util.task.LoginTask;
import com.maxcloud.renter.util.watcher.PhoneNoWatcher;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoginTask.OnLoginProgress {
    // UI references.
    private EditText mEdtAccount;
    private EditText mEdtPassword;

    private ClickListener _onClick = new ClickListener(this);
    //UI data
    private LoginTask _loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEdtAccount = (EditText) findViewById(R.id.edtAccount);
        mEdtPassword = (EditText) findViewById(R.id.edtPassword);

        mEdtAccount.addTextChangedListener(new PhoneNoWatcher(mEdtAccount));
        mEdtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.btnLogin || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        View mBtnLogin = findViewById(R.id.btnLogin);
        View mBtnForgetPasd = findViewById(R.id.btnForgetPasd);

        mBtnLogin.setOnClickListener(_onClick);
        mBtnForgetPasd.setOnClickListener(_onClick);
    }

    @Override
    public void onPreLogin() {
        showProgressDialog(R.string.login_logging);
    }

    @Override
    public void onPostLogin(Exception e) {
        if (null == e) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            L.e("login", e);

            closeProgressDialog();

            showError(getString(R.string.login_login_error, e.getMessage()));
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        // Reset errors.
        mEdtAccount.setError(null);
        mEdtPassword.setError(null);

        // Store values at the time of the login attempt.
        String account = PhoneNoHelper.parsePhoneNo(mEdtAccount.getText());
        String password = mEdtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mEdtPassword.setError(getString(R.string.error_invalid_password));
            focusView = mEdtPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(account)) {
            mEdtAccount.setError(getString(R.string.error_field_required));
            focusView = mEdtAccount;
            cancel = true;
        } else if (!isEmailValid(account)) {
            mEdtAccount.setError(getString(R.string.error_invalid_email));
            focusView = mEdtAccount;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
//            DoorInfo doorInfo = new DoorInfo();
//            doorInfo.setServerId("");
//            doorInfo.setId(1);
//            doorInfo.setName("门1");
//
//            LoginResult loginInfo = LoginResult.get();
//            loginInfo.setPhoneNo(account);
//            loginInfo.clearDoor();
//            loginInfo.addDoor(doorInfo);
//            loginInfo.save();
//
//            //设置统计账号
//            MobclickAgent.onProfileSignIn(account);
//
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//
//            startActivity(intent);

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
            try {
                if (null != _loginTask) {
                    _loginTask.cancel(true);
                }

                _loginTask = new LoginTask(this);
                _loginTask.execute(account, password);
            } catch (Exception e) {
                L.e("attemptLogin", e);

                closeProgressDialog();
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        //return password.length() > 4;
        return true;
    }

    private static class ClickListener implements View.OnClickListener {
        private LoginActivity _activity;

        public ClickListener(LoginActivity activity) {
            _activity = activity;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();

            try {
                switch (viewId) {
                    case R.id.btnLogin:
                        _activity.attemptLogin();
                        break;
                    case R.id.btnForgetPasd:
                        _activity.startActivity(new Intent(_activity, ResetPasswordActivity.class));
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

