package com.maxcloud.renter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;

import com.maxcloud.renter.R;
import com.maxcloud.renter.customview.ScrollViewPager;
import com.maxcloud.renter.util.fragment.FragmentHome;
import com.maxcloud.renter.util.fragment.FragmentMy;
import com.maxcloud.renter.util.fragment.FragmentPayment;
import com.maxcloud.renter.util.fragment.FragmentStayTuned;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public class MainActivity extends FragmentActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ScrollViewPager mViewPager;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);

                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ScrollViewPager) findViewById(R.id.container);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.addView(new FragmentHome(this));

        //tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.bg_tab_home_icon).setText(getString(R.string.home_title)));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.bg_tab_payment_icon).setText(getString(R.string.payment_title)));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.bg_tab_my_icon).setText(getString(R.string.my_title)));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //开启友盟消息服务。
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
        mPushAgent.enable();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //当默认统计被禁止时。
        if (!AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
            MobclickAgent.onPageStart(getClass().getSimpleName());
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //当默认统计被禁止时。
        if (!AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
            MobclickAgent.onPageEnd(getClass().getSimpleName());
        }
        MobclickAgent.onPause(this);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private FragmentHome _fragmentHome;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (null == _fragmentHome) {
                        _fragmentHome = new FragmentHome();
                    }
                    return _fragmentHome;
                case 1:
                    return new FragmentPayment();
                case 2:
                    return new FragmentMy();
                default:
                    return new FragmentStayTuned();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "主页";
                case 1:
                    return "费用";
                case 2:
                    return "我";
            }
            return null;
        }
    }
}
