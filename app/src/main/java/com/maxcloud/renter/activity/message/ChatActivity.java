package com.maxcloud.renter.activity.message;

import android.os.Bundle;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.CustomTitleActivity;

public class ChatActivity extends CustomTitleActivity {
    public static final String BUNDLE_ACCOUNT = "Account";
    public static final String BUNDLE_NAME = "Name";

    private String _otherAccount;
    private String _otherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(BUNDLE_ACCOUNT)) {
            _otherAccount = bundle.getString(BUNDLE_ACCOUNT);
        }
        if (bundle.containsKey(BUNDLE_NAME)) {
            _otherName = bundle.getString(BUNDLE_NAME);
        }

        setTitle(_otherName);
    }
}
