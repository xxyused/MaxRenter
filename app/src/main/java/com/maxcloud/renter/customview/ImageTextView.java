package com.maxcloud.renter.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.maxcloud.renter.R;

/**
 * Created by MAX-XXY on 2016/2/15.
 */
public class ImageTextView extends TextView {

    private Drawable mLeft, mTop, mRight, mBottom;

    private Rect mIconBounds;

    public Rect getIconBounds() {
        return mIconBounds;
    }

    public ImageTextView setIconBounds(Rect bounds) {
        mIconBounds = bounds;

        updateDrawableBounds();
        super.setCompoundDrawables(mLeft, mTop, mRight, mBottom);

        return this;
    }

    public ImageTextView setIconBounds(int left, int top, int right, int bottom) {
        return setIconBounds(new Rect(left, top, right, bottom));
    }

    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        resetAttrs(context, attrs);
    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        resetAttrs(context, attrs);
    }

    private void resetAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ImageTextView);

        boolean boundIsChg = false;
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ImageTextView_iconBound_left:
                    if (null == mIconBounds) {
                        mIconBounds = new Rect();
                    }
                    mIconBounds.left = typedArray.getDimensionPixelSize(attr, 0);
                    boundIsChg = true;
                    break;
                case R.styleable.ImageTextView_iconBound_top:
                    if (null == mIconBounds) {
                        mIconBounds = new Rect();
                    }
                    mIconBounds.top = typedArray.getDimensionPixelSize(attr, 0);
                    boundIsChg = true;
                    break;
                case R.styleable.ImageTextView_iconBound_right:
                    if (null == mIconBounds) {
                        mIconBounds = new Rect();
                    }
                    mIconBounds.right = typedArray.getDimensionPixelSize(attr, 0);
                    boundIsChg = true;
                    break;
                case R.styleable.ImageTextView_iconBound_bottom:
                    if (null == mIconBounds) {
                        mIconBounds = new Rect();
                    }
                    mIconBounds.bottom = typedArray.getDimensionPixelSize(attr, 0);
                    boundIsChg = true;
                    break;
            }
        }

        try {
            typedArray.recycle();
        } catch (Exception e) {
        }

        if (boundIsChg) {
            setCompoundDrawables(mLeft, mTop, mRight, mBottom);
        }
    }

    private Rect calcDrawableBounds(Drawable dra) {
        int defWidth = dra.getIntrinsicWidth();
        int defHeight = dra.getIntrinsicHeight();
        double defScale = (double) defWidth / (double) defHeight;

        if (null == mIconBounds) {
            return new Rect(0, 0, defWidth, defHeight);
        } else {
            int width = mIconBounds.width();
            int height = mIconBounds.height();
            if (width <= 0) {
                width = defWidth;
            }
            if (height <= 0) {
                height = defHeight;
            }
            double scale = (double) width / (double) height;

            if (defScale < scale) {
                width = (int) (height * defScale);
            } else {
                height = (int) (width / defScale);
            }
            int left = mIconBounds.left + (mIconBounds.width() - width) / 2;
            int top = mIconBounds.top + (mIconBounds.height() - height) / 2;
            Rect bounds = new Rect(left, top, left + width, top + height);
            // if (bounds.right <= 0) {
            // bounds.right = (defWidth + bounds.left);
            // }
            // if (bounds.bottom <= 0) {
            // bounds.bottom = (defHeight + bounds.top);
            // }

            return bounds;
        }
    }

    private void updateDrawableBounds() {
        if (null != mLeft) {
            mLeft.setBounds(calcDrawableBounds(mLeft));
        }
        if (null != mTop) {
            mTop.setBounds(calcDrawableBounds(mTop));
        }
        if (null != mRight) {
            mRight.setBounds(calcDrawableBounds(mRight));
        }
        if (null != mBottom) {
            mBottom.setBounds(calcDrawableBounds(mBottom));
        }
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;

        updateDrawableBounds();
        super.setCompoundDrawables(left, top, right, bottom);
    }
}
