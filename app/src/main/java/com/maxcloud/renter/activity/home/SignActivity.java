package com.maxcloud.renter.activity.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.activity.BaseActivity;
import com.maxcloud.renter.entity.user.LoginInfo;
import com.maxcloud.renter.util.FileHelper;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.file.TextFileReader;
import com.maxcloud.renter.util.user.SignAdapter;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignActivity extends BaseActivity {
    public static final String getSignText(String signName) {
        if (signName.equals("aries")) {
            return "白羊座";
        } else if (signName.equals("taurus")) {
            return "金牛座";
        } else if (signName.equals("gemini")) {
            return "双子座";
        } else if (signName.equals("cancer")) {
            return "巨蟹座";
        } else if (signName.equals("leo")) {
            return "狮子座";
        } else if (signName.equals("virgo")) {
            return "处女座";
        } else if (signName.equals("libra")) {
            return "天秤座";
        } else if (signName.equals("scorpio")) {
            return "天蝎座";
        } else if (signName.equals("sagittarius")) {
            return "射手座";
        } else if (signName.equals("capricorn")) {
            return "摩羯座";
        } else if (signName.equals("aquarius")) {
            return "水瓶座";
        } else if (signName.equals("pisces")) {
            return "双鱼座";
        } else {
            return "未知";
        }
    }

    public static final String getSignDate(String signName) {
        if (signName.equals("aries")) {
            return "3.21-4.19";
        } else if (signName.equals("taurus")) {
            return "4.20-5.20";
        } else if (signName.equals("gemini")) {
            return "5.21-6.21";
        } else if (signName.equals("cancer")) {
            return "6.22-7.22";
        } else if (signName.equals("leo")) {
            return "7.23-8.22";
        } else if (signName.equals("virgo")) {
            return "8.23-9.22";
        } else if (signName.equals("libra")) {
            return "9.23-10.23";
        } else if (signName.equals("scorpio")) {
            return "10.24-11.22";
        } else if (signName.equals("sagittarius")) {
            return "11.23-12.21";
        } else if (signName.equals("capricorn")) {
            return "12.22-1.19";
        } else if (signName.equals("aquarius")) {
            return "1.20-2.18";
        } else {
            return "2.19-3.20";
        }
    }

    public static final int getSignImgRes(String signName) {
        if (signName.equals("aries")) {
            return R.mipmap.ic_sign_aries;
        } else if (signName.equals("taurus")) {
            return R.mipmap.ic_sign_taurus;
        } else if (signName.equals("gemini")) {
            return R.mipmap.ic_sign_gemini;
        } else if (signName.equals("cancer")) {
            return R.mipmap.ic_sign_cancer;
        } else if (signName.equals("leo")) {
            return R.mipmap.ic_sign_leo;
        } else if (signName.equals("virgo")) {
            return R.mipmap.ic_sign_virgo;
        } else if (signName.equals("libra")) {
            return R.mipmap.ic_sign_libra;
        } else if (signName.equals("scorpio")) {
            return R.mipmap.ic_sign_scorpio;
        } else if (signName.equals("sagittarius")) {
            return R.mipmap.ic_sign_sagittarius;
        } else if (signName.equals("capricorn")) {
            return R.mipmap.ic_sign_capricorn;
        } else if (signName.equals("aquarius")) {
            return R.mipmap.ic_sign_aquarius;
        } else {
            return R.mipmap.ic_sign_pisces;
        }
    }

    private ImageView mImgSign;
    private TextView mTxvName;
    private RatingBar mRtbComposite;
    private RatingBar mRtbLove;
    private RatingBar mRtbWork;
    private RatingBar mRtbInvestment;
    private TextView mTxvHealth;
    private TextView mTxvNegotiate;
    private TextView mTxvColor;
    private TextView mTxvNumber;
    private TextView mTxvPair;
    private TextView mTxvValidity;
    private TextView mTxvDiscribtion;
    private View mLayoutItem;
    private ImageView mImgDropdown;
    private View mLayoutItems;
    private ListView mLsvItems;

    private SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
    private SignAdapter mSignAdapter;
    private JSONObject mCurSign;
    private Bitmap mBitmap;
    private TranslateAnimation mDropdowneAnim;
    private TranslateAnimation mRetractAnim;
    private OnClickListener mOnClick = new OnClickListener(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        View btnGoBack = findViewById(R.id.btnGoBack);
        mImgSign = (ImageView) findViewById(R.id.imgSign);
        mTxvName = (TextView) findViewById(R.id.txvName);
        mRtbComposite = (RatingBar) findViewById(R.id.rtbComposite);
        mRtbLove = (RatingBar) findViewById(R.id.rtbLove);
        mRtbWork = (RatingBar) findViewById(R.id.rtbWork);
        mRtbInvestment = (RatingBar) findViewById(R.id.rtbInvestment);
        mTxvHealth = (TextView) findViewById(R.id.txvHealth);
        mTxvNegotiate = (TextView) findViewById(R.id.txvNegotiate);
        mTxvColor = (TextView) findViewById(R.id.txvColor);
        mTxvNumber = (TextView) findViewById(R.id.txvNumber);
        mTxvPair = (TextView) findViewById(R.id.txvPair);
        mTxvValidity = (TextView) findViewById(R.id.txvValidity);
        mTxvDiscribtion = (TextView) findViewById(R.id.txvDiscribtion);
        mImgDropdown = (ImageView) findViewById(R.id.imgDropdown);
        mLayoutItems = findViewById(R.id.layoutItems);
        mLsvItems = (ListView) findViewById(R.id.lsvItems);
        mLayoutItem = findViewById(R.id.layoutItem);

        mSignAdapter = new SignAdapter(this);
        mBitmap = createDropDownArray();

        btnGoBack.setOnClickListener(mOnClick);
        mLayoutItem.setOnClickListener(mOnClick);
        mLsvItems.setAdapter(mSignAdapter);
        mLsvItems.setOnItemClickListener(new OnSignClickListener(this));

        mDropdowneAnim = new TranslateAnimation(0f, 100f, 100f, 100f);
        mDropdowneAnim.setDuration(500);

        mRetractAnim = new TranslateAnimation(100f, 0f, 100f, 100f);
        mRetractAnim.setDuration(500);
    }

    /**
     * 读取星座列表。
     */
    public void loadSignList() {
        try {
            if (FileHelper.existsSign()) {
                TextFileReader tfr = null;

                try {
                    tfr = new TextFileReader(new File(FileHelper.getSign()));
                    String line;
                    String signName = LoginInfo.get().getSignName();
                    if(TextUtils.isEmpty(signName)){
                        signName="taurus";
                    }

                    while ((line = tfr.readLine()) != null) {
                        JSONObject jsonItem = new JSONObject(line);
                        String name = jsonItem.optString("name");
                        if (!TextUtils.isEmpty(name)) {
                            SignAdapter.SignItemData itemData = new SignAdapter.SignItemData();
                            itemData.Value = jsonItem;
                            itemData.ImageRes = getSignImgRes(name);
                            itemData.Name = String.format("%s (%s)", getSignText(name), getSignDate(name));

                            mSignAdapter.add(itemData);
                        }

                        if (signName.equals(name)) {
                            mCurSign = jsonItem;
                        }
                    }
                } catch (Exception e) {
                    L.e("loadSignList", e);
                }

                if (null != tfr) {
                    tfr.close();
                }
            }
        } catch (Exception e) {
            L.e("loadSignList", e);
        }

        showConstellation(mCurSign);
    }

    /**
     * 返回星座列表是否显示。
     *
     * @return 如果已经下拉显示，则返回 true ，否则，返回 false 。
     */
    public boolean isDropdown() {
        return mLsvItems.getVisibility() == View.VISIBLE;
    }

    /**
     * 下拉显示星座选择列表。
     *
     * @param isDropdown 是否下拉。
     */
    public void dropdownSignList(boolean isDropdown) {
        if (isDropdown) {
            mLayoutItem.setBackgroundResource(R.drawable.rect_white66_r10t);
            mImgDropdown.setImageBitmap(mBitmap);
            mLsvItems.setVisibility(View.VISIBLE);
        } else {
            mLayoutItem.setBackgroundResource(R.drawable.rect_white33_r10);
            mImgDropdown.setImageResource(R.mipmap.ic_bg_dropdown);
            mLsvItems.setVisibility(View.GONE);
        }
    }

    /**
     * 选择指定索引处的星座项。
     *
     * @param index 指定索引。
     */
    public void selectedSignItem(int index) {
        SignAdapter.SignItemData itemData = mSignAdapter.getItem(index);
        mCurSign = itemData.Value;
        if (null != mCurSign) {
            String signName = showConstellation(mCurSign);

            LoginInfo loginInfo = LoginInfo.get();
            loginInfo.setSignName(signName);
            loginInfo.save();

            dropdownSignList(false);

            L.useClick(this, signName);
        } else {
            L.useClick(this, String.format("selectedSign(%d)", index));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadSignList();
    }

    private Bitmap createDropDownArray() {
        Matrix matrix = new Matrix();
        // 设置旋转角度
        matrix.setRotate(180);

        Bitmap moreArr = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_bg_dropdown);

        int width = moreArr.getWidth();
        int height = moreArr.getHeight();

        Bitmap dropArr = Bitmap.createBitmap(moreArr, 0, 0, width, height, matrix, true);

        moreArr.recycle();
        moreArr = null;

        return dropArr;
    }

    private String showConstellation(JSONObject jsonItem) {
        String signName = "";
        String name = "";
        int composite = 0;
        int love = 0;
        int work = 0;
        int investment = 0;
        String health = "0";
        String negotiate = "0";
        String color = "";
        String number = "0";
        String pair = "";
        String validity = "";
        String discribtion = "";

        try {
            signName = jsonItem.optString("name", "");
            name = jsonItem.optString("name_CH", "");
            composite = jsonItem.optInt("composite", 0);
            love = jsonItem.optInt("love", 0);
            work = jsonItem.optInt("work", 0);
            investment = jsonItem.optInt("investment", 0);
            health = jsonItem.optString("health", "0");
            negotiate = jsonItem.optString("negotiate", "0");
            color = jsonItem.optString("color", "");
            number = jsonItem.optString("number", "0");
            pair = jsonItem.optString("pair", "");
            validity = jsonItem.optString("date", "");
            discribtion = jsonItem.optString("discribtion");

            Date vDate = new Date();
            if (!TextUtils.isEmpty(validity)) {
                try {
                    vDate = dateFmt.parse(validity);
                } catch (Exception e) {
                    vDate = new Date();
                }
            }
            validity = formatDate(vDate).toString();
        } catch (Exception e) {
            L.e("Home_Renter.showWeather", e);
        }

        mImgSign.setImageResource(getSignImgRes(signName));
        mTxvName.setText(String.format("%s (%s)", name, getSignDate(signName)));
        mRtbComposite.setRating(composite);
        mRtbLove.setRating(love);
        mRtbWork.setRating(work);
        mRtbInvestment.setRating(investment);
        mTxvHealth.setText(health);
        mTxvNegotiate.setText(negotiate);
        mTxvColor.setText(color);
        mTxvNumber.setText(number);
        mTxvPair.setText(pair);
        mTxvValidity.setText(validity);
        mTxvDiscribtion.setText(discribtion);

        return signName;
    }

    private static class OnClickListener implements View.OnClickListener {
        private SignActivity _activity;

        public OnClickListener(SignActivity activity) {
            _activity = activity;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            try {
                switch (viewId) {
                    case R.id.btnGoBack:
                        _activity.finish();
                        break;
                    case R.id.layoutItem:
                        _activity.dropdownSignList(!_activity.isDropdown());
                        break;
                }
            } catch (Exception e) {
                L.e("onClick", e);
            }

            L.useClick(v.getContext(), viewId);
        }
    }

    private static class OnSignClickListener implements AdapterView.OnItemClickListener {
        private SignActivity _activity;

        public OnSignClickListener(SignActivity activity) {
            _activity = activity;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                _activity.selectedSignItem(position);

            } catch (Exception e) {
                L.e("signItemClick", e);
            }
        }
    }
}
