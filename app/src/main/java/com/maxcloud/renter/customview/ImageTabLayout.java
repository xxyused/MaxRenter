package com.maxcloud.renter.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import com.maxcloud.renter.R;

/**
 * Created by MAX-XXY on 2016/2/15.
 */
public class ImageTabLayout extends TabLayout {
    public ImageTabLayout(Context context) {
        super(context);
    }

    public ImageTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Create and return a new {@link Tab}. You need to manually add this using
     * {@link #addTab(Tab)} or a related method.
     *
     * @return A new Tab
     * @see #addTab(Tab)
     */
    @NonNull
    @Override
    public Tab newTab() {
        return super.newTab().setIcon(R.drawable.bg_tab_home_icon);
    }
}
