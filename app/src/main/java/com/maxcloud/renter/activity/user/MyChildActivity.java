package com.maxcloud.renter.activity.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.CustomTitleActivity;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.service.MaxService;
import com.maxcloud.renter.service.entity.ChildInfoResult;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.user.ChildListAdapter;

import java.util.List;

public class MyChildActivity extends CustomTitleActivity {
    private ListView _lsvItems;

    private ChildListAdapter _adapter;
    private LoadChildTask _task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_child);

        _lsvItems = (ListView) findViewById(R.id.lsvItems);

        _adapter = new ChildListAdapter(this);
        _lsvItems.setAdapter(_adapter);

        if (null != _task) {
            _task.cancel(true);
        }
        _task = new LoadChildTask(this);
        _task.execute(LoginInfo.get().getAccount());
    }

    public void addChild(ChildInfoResult childInfo) {
        _adapter.add(childInfo);
    }

    public void clearChild() {
        _adapter.clear();
    }

    public void notifyChildChanged() {
        _adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != _task) {
            _task.cancel(true);
        }
    }

    private static class LoadChildTask extends AsyncTask<String, Void, String> {
        private MyChildActivity _activity;

        public LoadChildTask(MyChildActivity activity) {
            _activity = activity;
        }

        @Override
        protected String doInBackground(String... params) {
            String account = params[0];

            try {
                MaxService service = MaxService.get();
                List<ChildInfoResult> childResults = service.getAllChild(account);
                _activity.clearChild();

                for (ChildInfoResult childInfo : childResults) {
                    _activity.addChild(childInfo);
                }
                childResults.clear();
            } catch (Exception e) {
                L.e("getBuildByIdCardNo", e);

                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            _activity.showProgressDialog(R.string.active_card_loading_build);
        }

        @Override
        protected void onPostExecute(String error) {
            _activity.closeProgressDialog();
            if (!TextUtils.isEmpty(error)) {
                _activity.showError(error);
            } else {
                _activity.notifyChildChanged();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            _activity.closeProgressDialog();
        }
    }
}
