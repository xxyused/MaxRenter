package com.maxcloud.renter.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.util.L;

/**
 * 描    述：Activity基础类，封闭显示进度条、关闭进度条、显示Toast、隐藏输入软键盘、友盟统计和自定标题。
 * 作    者：向晓阳
 * 时    间：2016/2/18
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class CustomTitleActivity extends BaseActivity {
    private TextView _titleText;
    private ProgressBar _progress;

    /**
     * Set the activity content from a layout resource.  The resource will be
     * inflated, adding all top-level views to the activity.
     *
     * @param layoutResID Resource ID to be inflated.
     * @see #setContentView(View)
     * @see #setContentView(View, ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(int layoutResID) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.setContentView(layoutResID);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);

        _titleText = (TextView) findViewById(R.id.titleText);
        _progress = (ProgressBar) findViewById(R.id.progress);
        _titleText.setText(getTitle());
    }

    /**
     * Set the activity content to an explicit view.  This view is placed
     * directly into the activity's view hierarchy.  It can itself be a complex
     * view hierarchy.  When calling this method, the layout parameters of the
     * specified view are ignored.  Both the width and the height of the view are
     * set by default to {@link ViewGroup.LayoutParams#MATCH_PARENT}. To use
     * your own layout parameters, invoke
     * {@link #setContentView(View, ViewGroup.LayoutParams)}
     * instead.
     *
     * @param view The desired content to display.
     * @see #setContentView(int)
     * @see #setContentView(View, ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(View view) {
        this.setContentView(view, null);
    }

    /**
     * Set the activity content to an explicit view.  This view is placed
     * directly into the activity's view hierarchy.  It can itself be a complex
     * view hierarchy.
     *
     * @param view   The desired content to display.
     * @param params Layout parameters for the view.
     * @see #setContentView(View)
     * @see #setContentView(int)
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        if (null == params) {
            super.setContentView(view);
        } else {
            super.setContentView(view, params);
        }
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);

        _titleText = (TextView) findViewById(R.id.titleText);
        _titleText.setText(getTitle());
    }

    /**
     * 隐藏输入软键盘。
     */
    public void hideSoftInput() {
        try {
            View curFocus = getCurrentFocus();
            if (null != curFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(curFocus.getWindowToken(), 0);
            }
        } catch (Exception e) {
            L.e("hideSoftInput", e);
        }
    }

    /**
     * 显示标题进度条。
     */
    public void showTitleProgress() {
        if (null != _progress) {
            _progress.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏标题进度条。
     */
    public void hideTitleProgress() {
        if (null != _progress) {
            _progress.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);

        if (null != _titleText) {
            _titleText.setText(title);
            if (color != 0) {
                _titleText.setTextColor(color);
            }
        }
    }
}
