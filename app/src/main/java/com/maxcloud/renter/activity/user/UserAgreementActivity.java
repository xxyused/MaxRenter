package com.maxcloud.renter.activity.user;

import android.os.Bundle;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.CustomTitleActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class UserAgreementActivity extends CustomTitleActivity {
    private final static int BUFFER_SIZE = 4096;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);

        TextView txvText = (TextView) findViewById(R.id.txvText);

        try {
            InputStream stream = getResources().openRawResource(R.raw.agreement);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int count = -1;
            while ((count = stream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                outStream.write(buffer, 0, count);
            }

            buffer = null;
            txvText.setText(new String(outStream.toByteArray()));
        } catch (Exception e) {

        }
    }
}
