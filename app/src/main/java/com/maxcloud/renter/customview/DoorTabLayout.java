package com.maxcloud.renter.customview;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.maxcloud.renter.R;

/**
 * Created by MAX-XXY on 2016/2/16.
 */
public class DoorTabLayout extends LinearLayout implements
        ViewPager.OnPageChangeListener {

    private static final String TAG = DoorTabLayout.class.getSimpleName();

    private ViewPager mViewPager;
    private LinearLayout mPointLayout;
    private PagerAdapter mAdapter;
    private int mPointImageResId = R.drawable.bg_home_door_point;

    // 记录当前选中位置
    private int mPointIndex;

    public int position() {
        return mViewPager.getCurrentItem();
    }

    public void position(int position) {
        int maxPosition = mAdapter.getCount() - 1;

        if (position < 0) {
            position = 0;
        }
        if (position > maxPosition) {
            position = maxPosition;
        }

        mViewPager.setCurrentItem(position, true);
    }

    public int getPageCount() {
        return mViewPager.getChildCount();
    }

    private boolean mIsCanScroll = true;

    public boolean isCanScroll() {
        return mIsCanScroll;
    }

    public void setCanScroll(boolean canScroll) {
        mIsCanScroll = canScroll;
    }

    public DoorView getPageAt(int index) {
        if (getPageCount() <= index) {
            return null;
        }

        return (DoorView) mViewPager.getChildAt(index);
    }

    public void setPointImageResource(int resId) {
        mPointImageResId = resId;

        for (int i = 0; i < mPointLayout.getChildCount(); i++) {
            ImageView pointView = (ImageView) mPointLayout.getChildAt(i);

            pointView.setImageResource(mPointImageResId);
        }
    }

    public DoorTabLayout(Context context) {
        super(context);

        initViewPage();
    }

    public DoorTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        initViewPage();
    }

    public DoorTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initViewPage();
    }

    private void initViewPage() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;

        mViewPager = new ViewPager(getContext()) {
            @Override
            public void scrollTo(int x, int y) {
                if (mIsCanScroll) {
                    super.scrollTo(x, y);
                }
            }
        };
        mViewPager.setLayoutParams(params);
        mViewPager.addOnPageChangeListener(this);

        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(0, 0, 0, 10);

        mPointLayout = new LinearLayout(getContext());
        mPointLayout.setLayoutParams(params);

        setOrientation(VERTICAL);
        super.addView(mViewPager);
        super.addView(mPointLayout);

        mPointIndex = -1;
    }

    /**
     * 添加点
     */
    private void addPointView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
        params.gravity = Gravity.CENTER;
        params.setMargins(5, 0, 5, 0);

        ImageView pointView = new ImageView(getContext());
        pointView.setImageResource(mPointImageResId);
        pointView.setClickable(true);
        pointView.setLayoutParams(params);
        pointView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        mPointLayout.addView(pointView);
    }

    private void reloadPointViewList() {
        int allCount = mAdapter.getCount();
        int curCount = mPointLayout.getChildCount();
        if (curCount != allCount) {
            if (allCount > curCount) {
                for (int i = curCount; i < allCount; i++) {
                    addPointView();
                }
            } else {
                mPointLayout.removeViews(allCount, curCount - allCount);
            }
        }

        setCurrentPointIndex(mViewPager.getCurrentItem());
    }

    private void changePointEnabled(int index, boolean enabled) {
        if (index >= 0 && index < mPointLayout.getChildCount()) {
            View pointView = mPointLayout.getChildAt(index);
            pointView.setEnabled(enabled);
        }
    }

    private void setCurrentPointIndex(int index) {
        if (mPointIndex >= 0) {
            changePointEnabled(mPointIndex, true);
        }
        mPointIndex = index;
        changePointEnabled(mPointIndex, false);
    }

    @Override
    public void removeAllViewsInLayout() {
        int count = getChildCount();
        if (count <= 0) {
            return;
        }

        for (int i = count; i >= 0; i--) {
            View child = getChildAt(i);
            if (child == mPointLayout || child == mViewPager) {
                continue;
            }

            super.removeViewInLayout(child);
        }
    }

    @Override
    public void removeViews(int start, int count) {
        removeViewsInLayout(start, count);

        requestLayout();
        invalidate();
    }

    @Override
    public void removeViewsInLayout(int start, int count) {
        mViewPager.removeViewsInLayout(start, count);

        reloadPointViewList();
    }

    public void setAdapter(PagerAdapter adapter) {
        mAdapter = adapter;
        mViewPager.setAdapter(adapter);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                reloadPointViewList();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();

                mPointLayout.invalidate();
            }
        });

        reloadPointViewList();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentPointIndex(arg0);
    }
}
