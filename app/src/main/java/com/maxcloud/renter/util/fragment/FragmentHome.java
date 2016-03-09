package com.maxcloud.renter.util.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.maxcloud.renter.MainApplication;
import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.BaseFragment;
import com.maxcloud.renter.activity.build.ActiveCardActivity;
import com.maxcloud.renter.activity.home.SignActivity;
import com.maxcloud.renter.activity.message.MyMessageActivity;
import com.maxcloud.renter.customview.DoorTabLayout;
import com.maxcloud.renter.customview.DoorView;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.util.FileHelper;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.build.DoorAdapter;
import com.maxcloud.renter.util.build.DoorManage;
import com.maxcloud.renter.util.file.TextFileReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描    述：主页。
 * 作    者：向晓阳
 * 时    间：2016/2/16
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class FragmentHome extends BaseFragment {
    private static final int LOC_RETRY_COUNT = 10;
    private static final String DEFAULT_CITY_CODE = "CN101280601";
    private static final String DEFAULT_CITY_NAME = "深圳";

    private TextView txvCity;
    private View prbCity;
    private TextView mTxvSignName;
    private TextView mTxvSignLuck;
    private RatingBar mRtbLuck;
    private View _rootView;
    private View mBtnBindCard;
    private DoorTabLayout mDoorsView;
    private ImageView mBtnMessage;
    private TextView mTxvMsgCount;

    private WeatherManage _weatherManage;
    private DoorManage _doorManage;
    private List<Map<String, String>> items;
    private SimpleAdapter adapter;
    private int _locRemainCount = 0;
    private DownloadTask _downloadTask;

    private LocationClient _locClient = null;
    private LocationListener _locListener = new LocationListener(this);
    private ClickListener _onClick = new ClickListener(this);

    @Override
    protected String getTitle() {
        return getString(R.string.home_title);
    }

    public FragmentHome() {
        super();

        items = new ArrayList<>();
        _doorManage = new DoorManage(new DoorAdapter.OnItemClickListener() {
            @Override
            public void onClick(DoorView doorView, boolean isOpenDoor) {
                if (isOpenDoor) {
                    _doorManage.openDoor(doorView);
                } else {
                    //int remainCount = mODConfig.getRemainCount(doorView.getServerId(), doorView.getDoorId());
                    //String describe = String.format("每个门每天只能使用手机开门%d次，当前门还剩余%d次！", getInteger(R.integer.default_open_door_count), remainCount);
                    String describe = String.format("每个门每天只能使用手机开门%d次，当前门还剩余%d次！", doorView.getTotalCount(), doorView.getRemainderCount());

                    Toast.makeText(getContext(), describe, Toast.LENGTH_LONG).show();
                    //new OkButtonDialog(mActivity, "开门次数", describe, "我知道了").show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == _rootView) {
            _rootView = View.inflate(getContext(), R.layout.fragment_home, null);

            txvCity = (TextView) _rootView.findViewById(R.id.txvCity);
            prbCity = _rootView.findViewById(R.id.prbCity);
            View layoutCity = _rootView.findViewById(R.id.layoutCity);
            View layoutConstellation = _rootView.findViewById(R.id.layoutConstellation);

            mTxvSignName = (TextView) _rootView.findViewById(R.id.txvSignName);
            mTxvSignLuck = (TextView) _rootView.findViewById(R.id.txvSignLuck);
            mRtbLuck = (RatingBar) _rootView.findViewById(R.id.rtbLuck);
            mBtnBindCard = _rootView.findViewById(R.id.btnBindCard);
            mDoorsView = (DoorTabLayout) _rootView.findViewById(R.id.vpgOpenDoor);

            mBtnMessage = (ImageView) _rootView.findViewById(R.id.btnMessage);
            mTxvMsgCount = (TextView) _rootView.findViewById(R.id.txvMsgCount);

            layoutCity.setOnClickListener(_onClick);
            layoutConstellation.setOnClickListener(_onClick);
            mBtnBindCard.setOnClickListener(_onClick);
            mBtnMessage.setOnClickListener(_onClick);
            mTxvSignLuck.requestFocus();

            _weatherManage = new WeatherManage(_rootView);
            _doorManage.reloadDoor(getContext(), false);
            _doorManage.setView(mDoorsView, mBtnBindCard);
        }

        return _rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        LoginInfo loginInfo = LoginInfo.get();
        if (!TextUtils.isEmpty(loginInfo.getCityName())) {
            txvCity.setText(loginInfo.getCityName());
            txvCity.setVisibility(View.VISIBLE);
            prbCity.setVisibility(View.GONE);
        } else {
            startLocationClient();
        }

        reloadWeather();
        reloadSign();
        _doorManage.reloadDoor(getContext(), true);
        if (null != mTxvSignLuck) {
            mTxvSignLuck.requestFocus();
        }

        downloadData(true, true, true);
    }

    @Override
    public void onDestroy() {
        stopLocationClient();

        super.onDestroy();
    }

    /**
     * 下载数据。
     *
     * @param cityList 是否下载城市列表。
     * @param weather  是否下载天气。
     * @param sign     是否下载星座。
     */
    public void downloadData(boolean cityList, boolean weather, boolean sign) {
        if (null != _downloadTask) {
            _downloadTask.cancel(true);
        }

        _downloadTask = new DownloadTask(this);
        _downloadTask.execute(cityList, weather, sign);
    }

    /**
     * 加载城市列表数据。
     */
    public void reloadCityList() {
        if (!FileHelper.existsCityList()) {
            return;
        }

        TextFileReader cityRd = null;

        try {
            cityRd = new TextFileReader(new File(FileHelper.getCityList()));
            String line;

            items.clear();

            while (null != (line = cityRd.readLine())) {
                try {
                    JSONObject jsonItem = new JSONObject(line);
                    String code = jsonItem.optString("city_code");
                    String name = jsonItem.optString("name_ch");

                    Map<String, String> item = new HashMap<>();
                    item.put("code", code);
                    item.put("name", name);

                    items.add(item);
                } catch (JSONException je) {
                    L.e("readCityLine", je);
                }
            }
        } catch (Exception e) {
            L.e("loadCityList", e);
        } finally {
            if (null != cityRd) {
                cityRd.close();
            }
        }
    }

    /**
     * 根据城市名称，查找城市编码。
     *
     * @param cityName 城市名称。
     * @return 城市编码。
     */
    public String findCityCode(String cityName) {
        if (TextUtils.isEmpty(cityName)) {
            return null;
        }

        for (Map<String, String> entry : items) {
            if (cityName.equals(entry.get("name"))) {
                return entry.get("code");
            }
        }

        return null;
    }

    /**
     * 加载天气数据。
     */
    public void reloadWeather() {
        try {
            if (null != _weatherManage) {
                _weatherManage.loadLocalWeather();
            }
        } catch (IOException e) {
            L.e("reloadWeather", e);
        }
    }

    /**
     * 加载星座数据。
     */
    public void reloadSign() {
        if(!FileHelper.existsSign()){
            return;
        }
        TextFileReader tfr = null;
        String signName = LoginInfo.get().getSignName();
        if(TextUtils.isEmpty(signName)){
            signName="taurus";
        }
        String signText = SignActivity.getSignText(signName);
        String discribtion = "";
        int composite = 0;

        try {
            tfr = new TextFileReader(new File(FileHelper.getSign()));
            String line;

            while ((line = tfr.readLine()) != null) {
                JSONObject jsonItem = new JSONObject(line);
                String name = jsonItem.optString("name");
                if (signName.equals(name)) {
                    try {
                        name = jsonItem.optString("name_CH", SignActivity.getSignText(signName));
                        discribtion = jsonItem.optString("discribtion", "");
                        composite = jsonItem.optInt("composite", 0);
                    } catch (Exception e) {
                        L.e("showSignToUi", e);
                    }
                }
            }
        } catch (Exception e) {
            L.e("loadSignList", e);
        }

        if (null != tfr) {
            tfr.close();
        }

        mTxvSignName.setText(String.format("%s运势：", signText));
        mTxvSignLuck.setText(discribtion);
        mRtbLuck.setRating(composite);
    }

    /**
     * 显示城市选择对话框。
     */
    public void showCityListDialog() {
        if (!FileHelper.existsCityList()) {
            Toast.makeText(getContext(), "正在更新城市列表，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }

        if (null == adapter) {
            adapter = new SimpleAdapter(getContext(), items, R.layout.item_list, new String[]{"name"}, new int[]{R.id.item});
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //builder.setTitle("请选择城市");
//        builder.setPositiveButton("刷新列表", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                setDialogAutoClose(dialog, false);
//
//                new AsyncTask<DialogInterface, Void, DialogInterface>() {
//                    @Override
//                    protected DialogInterface doInBackground(DialogInterface... params) {
//                        try {
//                            downloadCityList(null);
//                        } catch (Exception e) {
//                            L.e("refreshClick", e);
//                        }
//
//                        return params[0];
//                    }
//
//                    @Override
//                    protected void onPostExecute(DialogInterface dialog) {
//                        adapter.notifyDataSetChanged();
//
//                        setDialogAutoClose(dialog, true);
//                    }
//                }.execute(dialog);
//            }
//        });
//        builder.setNeutralButton("自动定位", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                setDialogAutoClose(dialog, true);
//
//                startLocationClient();
//            }
//        });
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                stopLocationClient();

                Map<String, String> item = items.get(which);
                changedCity(item.get("code"), item.get("name"));
            }
        });

        builder.show();
    }

    /**
     * 修改城市。
     *
     * @param cityCode 城市编码。
     * @param cityName 城市名称。
     */
    public void changedCity(String cityCode, String cityName) {
        LoginInfo loginInfo = LoginInfo.get();
        loginInfo.setCityCode(cityCode);
        loginInfo.setCityName(cityName);
        loginInfo.save();

        txvCity.setText(cityName);
        stopLocationClient();

        downloadData(false, true, false);
    }

    /**
     * 启动城市定位。
     */
    public void startLocationClient() {
        if (null == _locClient) {
            LoginInfo loginInfo = LoginInfo.get();
            loginInfo.setCityName(null);
            loginInfo.setCityCode(null);

            loginInfo.save();

            txvCity.setVisibility(View.GONE);
            prbCity.setVisibility(View.VISIBLE);

            _locRemainCount = LOC_RETRY_COUNT;

            _locClient = MainApplication.getLocationClient(false, 1000, true);
            _locClient.registerLocationListener(_locListener);
            _locClient.start();
        }
    }

    private void stopLocationClient() {
        if (null != _locClient) {
            _locClient.unRegisterLocationListener(_locListener);
            _locClient.stop();
            _locClient = null;
        }

        txvCity.setVisibility(View.VISIBLE);
        prbCity.setVisibility(View.GONE);
    }

    private void setDialogAutoClose(DialogInterface dialog, boolean autoClose) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            //设置mShowing值，欺骗android系统
            field.set(dialog, autoClose);
        } catch (Exception e) {

        }
    }

    private static class LocationListener implements BDLocationListener {
        private FragmentHome _fragment;

        public LocationListener(FragmentHome fragment) {
            _fragment = fragment;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            _fragment._locRemainCount--;

            String cityName = location.getCity();
            if (TextUtils.isEmpty(cityName) && _fragment._locRemainCount <= 0) {
                _fragment.stopLocationClient();
                Toast.makeText(_fragment.getContext(), "定位当前城市失败", Toast.LENGTH_LONG).show();

                cityName = DEFAULT_CITY_NAME;
            }

            if (!TextUtils.isEmpty(cityName)) {
                if (cityName.endsWith("市")) {
                    cityName = cityName.substring(0, cityName.length() - 1);
                }

                String cityCode = _fragment.findCityCode(cityName);
                _fragment.changedCity(cityCode, cityName);


                Log.i("Baidu", String.format("%d(%s)", location.getLocType(), location.getCity()));
            }
        }
    }

    private static class ClickListener implements View.OnClickListener {
        private FragmentHome _fragment;

        public ClickListener(FragmentHome fragment) {
            _fragment = fragment;
        }

        @Override
        public void onClick(View v) {

            int viewId = v.getId();
            try {
                switch (viewId) {
                    case R.id.layoutCity:
                        _fragment.showCityListDialog();
                        break;
                    case R.id.layoutConstellation:
                        _fragment.startActivity(new Intent(_fragment.getContext(), SignActivity.class));
                        break;
                    case R.id.btnMessage:
                        _fragment.startActivity(new Intent(_fragment.getContext(), MyMessageActivity.class));
                        break;
                    case R.id.btnBindCard:
                        _fragment.startActivity(new Intent(_fragment.getContext(), ActiveCardActivity.class));
                        break;
                }
            } catch (Exception e) {
                L.e("onClick", e);
            }

            L.useClick(v.getContext(), viewId);
        }
    }

    private static class DownloadTask extends AsyncTask<Boolean, Integer, String> {
        private FragmentHome _fragment;

        public DownloadTask(FragmentHome fragment) {
            _fragment = fragment;
        }

        @Override
        protected String doInBackground(Boolean... params) {
            boolean downloadCityList = params[0];
            boolean downloadWeather = params[1];
            boolean downloadSign = params[2];
            try {
                LoginInfo loginInfo = LoginInfo.get();

                if (downloadCityList) {
                    //下载城市列表。
                    FileHelper.downloadCityList();

                    //通知城市列表下载完成。
                    publishProgress(1);
                }

                if (downloadWeather) {
                    //下载天气数据。
                    String cityCode = loginInfo.getCityCode();
                    if (TextUtils.isEmpty(cityCode)) {
                        cityCode = DEFAULT_CITY_CODE;
                    }
                    FileHelper.downloadWeather(cityCode);

                    //通知天气数据下载完成。
                    publishProgress(2);
                }

                if (downloadSign) {
                    //下载星座数据。
                    FileHelper.downloadSign();

                    //通知星座数据下载完成。
                    publishProgress(3);
                }

                return null;
            } catch (Exception e) {
                L.e("loadCityList", e);

                return e.getMessage();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            switch (values[0]) {
                case 1:
                    _fragment.reloadCityList();
                    break;
                case 2:
                    _fragment.reloadWeather();
                    break;
                case 3:
                    _fragment.reloadSign();
                    break;
            }
        }

        @Override
        protected void onPostExecute(String cityName) {
//            _fragment.txvCity.setText(cityName);
//            _fragment.txvCity.setVisibility(View.VISIBLE);
//            _fragment.prbCity.setVisibility(View.GONE);
        }
    }
}
