package com.maxcloud.renter.util.fragment;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.util.L;

/**
 * Created by MAX-XXY on 2016/2/17.
 */
public class SignManage {
    private static final String OSS_PATH = "http://config-cloud.oss-cn-shenzhen.aliyuncs.com";
    private static final String OSS_SIGN_PATH = String.format("%s/ConstellationInfo/new.txt", OSS_PATH);

    private TextView mTxvSignName;
    private TextView mTxvSignLuck;
    private RatingBar mRtbLuck;

    public SignManage() {

    }

    public void setView(View view) {
        mTxvSignName = (TextView) view.findViewById(R.id.txvSignName);
        mTxvSignLuck = (TextView) view.findViewById(R.id.txvSignLuck);
        mRtbLuck = (RatingBar) view.findViewById(R.id.rtbLuck);
    }

    private void loadSign() {
        try {
            //读取星座
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... params) {
//                    try {
//                        String signPath = SDCardUtils.formatFilePath(mActivity, "data", SIGN_FILE);
//
//                        URL url = new URL(OSS_SIGN_PATH);
//                        HttpHelper.downloadText(url, signPath);
//                    } catch (Exception e) {
//                        L.e("SignManage.getSign", e);
//                    }
//
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Void result) {
//                    loadSignByLocalFile();
//                }
//            }.execute();
        } catch (Exception e) {
            L.e("reloadWeather", e);
        }
    }

    private void loadSignByLocalFile() {
//        TextFileReader signRd = null;
//        try {
//            String filePath = SDCardUtils.formatFilePath(mActivity, "data", SIGN_FILE);
//            File signFile = new File(filePath);
//            if (signFile.exists()) {
//                String signName = LoginHelper.getConstellation();
//
//                curConstellation = null;
//                signRd = new TextFileReader(new File(filePath));
//
//                String line;
//                while (null != (line = signRd.readLine())) {
//                    JSONObject jsonItem = new JSONObject(line);
//
//                    if (signName.equals(jsonItem.optString("name"))) {
//                        curConstellation = jsonItem;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            L.e("Home_Renter.loadSignByLocalFile", e);
//        }
//
//        if (null != signRd) {
//            signRd.close();
//        }

        showSignToUi();
    }

    private void showSignToUi() {
//        String signName = LoginHelper.getConstellation();
//        String name = ConstellationDialog.getSignText(signName);
//        String discribtion = "";
//        int composite = 0;
//
//        if (null != curConstellation) {
//            try {
//                name = curConstellation.optString("name_CH", ConstellationDialog.getSignText(signName));
//                discribtion = curConstellation.optString("discribtion", "");
//                composite = curConstellation.optInt("composite", 0);
//            } catch (Exception e) {
//               L.e(getClass(), "showSignToUi", e);
//            }
//        }
//
//        mTxvSignName.setText(String.format("%s运势：", name));
//        mTxvSignLuck.setText(discribtion);
//        mRtbLuck.setRating(composite);
    }
}
