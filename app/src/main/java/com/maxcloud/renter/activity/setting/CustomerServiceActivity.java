package com.maxcloud.renter.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.CustomTitleActivity;
import com.maxcloud.renter.util.L;

public class CustomerServiceActivity extends CustomTitleActivity {
    private ClickListener mOnClick = new ClickListener(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);

        View btnCall = findViewById(R.id.btnCall);
        View btnQQ1 = findViewById(R.id.btnQQ1);
        View btnQQ2 = findViewById(R.id.btnQQ2);

        btnCall.setOnClickListener(mOnClick);
        btnQQ1.setOnClickListener(mOnClick);
        btnQQ2.setOnClickListener(mOnClick);

        btnCall.setTag(getString(R.string.feedback_phone_no));
    }

    public void startQQ(@StringRes int qqNoResId) {
        String url = String.format("mqqwpa://im/chat?chat_type=wpa&uin=%s", getString(qqNoResId));
        Intent qqIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(qqIntent);
    }

    private static class ClickListener implements View.OnClickListener {
        private CustomerServiceActivity _activity;

        public ClickListener(CustomerServiceActivity activity) {
            _activity = activity;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();

            try {
                switch (viewId) {
                    case R.id.btnCall:
                        Object tag = v.getTag();
                        if (null == tag || !(tag instanceof String)) {
                            return;
                        }

                        Uri uri = Uri.parse(String.format("tel:%s", tag));

                        Intent callIntent = new Intent(Intent.ACTION_DIAL, uri);
                        _activity.startActivity(callIntent);
                        break;
                    case R.id.btnQQ1:
                        _activity.startQQ(R.string.feedback_qq1);
                        break;
                    case R.id.btnQQ2:
                        _activity.startQQ(R.string.feedback_qq2);
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
