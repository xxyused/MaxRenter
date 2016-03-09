package com.maxcloud.renter.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.maxcloud.renter.R;

/**
 * Created by MAX-XXY on 2016/2/15.
 */
public class ScrollViewPager extends ViewPager {
    private boolean _isCanScroll = false;

    public ScrollViewPager(Context context) {
        super(context);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        resetAttrs(context, attrs);
    }

    public boolean isCanScroll() {
        return _isCanScroll;
    }

    public void setCanScroll(boolean isCanScroll) {
        _isCanScroll = isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (_isCanScroll) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (_isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }

    private void resetAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollViewPager);

        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ScrollViewPager_canScroll:
                    _isCanScroll = typedArray.getBoolean(attr, true);
                    break;
            }
        }

        try {
            typedArray.recycle();
        } catch (Exception e) {
        }
    }
}
