package com.maxcloud.renter.util.task;

import android.os.AsyncTask;

import com.maxcloud.renter.entity.build.DoorInfo;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.service.entity.LoginResult;

import java.util.List;

/**
 * 描    述：登录任务类。
 * 作    者：向晓阳
 * 时    间：2016/3/7
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class LoginTask extends AsyncTask<String, Void, Exception> {
    private OnLoginProgress _loginProg;

    public LoginTask(OnLoginProgress activity) {
        _loginProg = activity;
    }

    @Override
    protected Exception doInBackground(String... params) {
        try {
            LoginResult loginResult;
            MaxService service = MaxService.get();
            if (null != params && params.length == 2) {
                String account = params[0];
                String password = params[1];

                loginResult = service.login(account, password);
            } else {
                loginResult = service.login();
            }
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
        } catch (Exception e) {
            return e;
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        _loginProg.onPreLogin();
    }

    @Override
    protected void onPostExecute(Exception e) {
        _loginProg.onPostLogin(e);
    }

    public interface OnLoginProgress {
        void onPreLogin();

        void onPostLogin(Exception e);
    }
}
