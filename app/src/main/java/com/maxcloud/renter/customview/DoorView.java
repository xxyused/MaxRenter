package com.maxcloud.renter.customview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxcloud.renter.R;

/**
 * Created by MAX-XXY on 2016/2/16.
 */
public class DoorView extends FrameLayout {
    private static class AnimationListener implements Animation.AnimationListener {
        private DoorView mDoorView;

        @Override
        public void onAnimationStart(Animation animation) {
            if (null != mDoorView) {
                mDoorView.mBusyingCount++;
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (null != mDoorView) {
                mDoorView.mBusyingCount--;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        public AnimationListener(DoorView doorView) {
            mDoorView = doorView;
        }
    }

    private static final int OFFSET = 1000;  //每个动画的播放时间间隔
    public static final int MIN_TIME = (int) (OFFSET * 1.5);

    private ViewGroup mRootView;
    private ImageView mWave1, mWave2;
    private ImageView mImgOpeningRotate, mImgOpeningAlpha;
    private ImageView mImgOpenedTip;
    private TextView mTxvDoorTip, mTxvDoorName, mTxvRemainderCount;
    private View mLayoutRemainCount;

    private Animation mAnimWave1, mAnimWave2;
    private Animation mAnimOpeningRotate, mAnimOpeningAlpha;
    private Animation mAnimOpeningRotateEnd, mAnimOpeningAlphaEnd;
    private Animation mAnimOpenedTip;

    private int mBusyingCount;

    public DoorView(Context context) {
        super(context);

        init(context);
    }

    public DoorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    public boolean isBusying() {
        return mBusyingCount > 0;
    }

    private String mServerId;

    public String getServerId() {
        return mServerId;
    }

    public void setServerId(String serverId) {
        mServerId = serverId;
    }

    private int mDoorId;

    public int getDoorId() {
        return mDoorId;
    }

    public void setDoorId(int doorId) {
        mDoorId = doorId;
    }

    public CharSequence getDoorName() {
        return mTxvDoorName.getText();
    }

    public void setDoorName(CharSequence doorName) {
        mTxvDoorName.setText(doorName);
    }

    private int mTotalCount = getResources().getInteger(R.integer.default_open_door_count);
    private int mRemainderCount = getResources().getInteger(R.integer.default_open_door_count);

    public int getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    public int getRemainderCount() {
        return mRemainderCount;
    }

    public void setRemainderCount(int count) {
        setRemainderCountByDelayed(count);

        mTxvRemainderCount.setText(String.format("%d/%d", mRemainderCount, mTotalCount));
    }

    public void setRemainderCountByDelayed(int count) {
        mRemainderCount = count;
    }

    public void setShowRemainder(boolean showRemainder) {
        mLayoutRemainCount.setVisibility(showRemainder ? VISIBLE : GONE);
    }

    public void showOpeningAnimation() {
        clearOpeningAnimation();
        clearOpenedAnimation();

        mTxvDoorTip.setText("开门中...");
        mImgOpeningRotate.setVisibility(VISIBLE);
        mImgOpeningAlpha.setVisibility(VISIBLE);

        mImgOpeningRotate.startAnimation(mAnimOpeningRotate);
        mImgOpeningAlpha.startAnimation(mAnimOpeningAlpha);
    }

    public void showWaveAnimation(long delayMillis) {
        new AsyncTask<Long, Integer, Void>() {
            @Override
            protected Void doInBackground(Long... params) {
                long time = params[0];
                if (time > 0) {
                    try {
                        Thread.sleep(time);
                    } catch (Exception e) {
                    }
                }

                publishProgress(1);

                try {
                    Thread.sleep((long) (OFFSET * 1.2));
                } catch (Exception e) {
                }

                publishProgress(2);

                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                int index = values[0];
                switch (index) {
                    case 1:
                        mWave1.startAnimation(mAnimWave1);
                        break;
                    case 2:
                        mWave2.startAnimation(mAnimWave2);
                        break;
                }
            }
        }.execute(delayMillis);
    }

    public void showOpenSuccessAnimation() {
        mImgOpenedTip.setVisibility(VISIBLE);

        clearOpeningAnimation();
        clearOpenedAnimation();

        mImgOpeningAlpha.startAnimation(mAnimOpeningAlphaEnd);
        mImgOpenedTip.startAnimation(mAnimOpenedTip);
        mTxvRemainderCount.setText(String.format("%d/%d", mRemainderCount, mTotalCount));
    }

    public void showOpenFailureAnimation() {
        mImgOpenedTip.setVisibility(GONE);

        clearOpeningAnimation();
        clearOpenedAnimation();

        mImgOpeningAlpha.startAnimation(mAnimOpeningAlphaEnd);
        mTxvRemainderCount.setText(String.format("%d/%d", mRemainderCount, mTotalCount));
    }

    public void clearAllAnimation() {
        clearWaveAnimation();
        clearOpeningAnimation();
        clearOpenedAnimation();

        mImgOpeningRotate.setVisibility(GONE);
        mImgOpeningAlpha.setVisibility(GONE);
        mImgOpenedTip.setVisibility(GONE);
        mTxvDoorTip.setText(getDefaultTipText());
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        int minSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
        int usableSize = minSize * 3 / 4;

        ViewGroup.LayoutParams params;
        for (int i = 0; i < mRootView.getChildCount(); i++) {
            View child = mRootView.getChildAt(i);

            if (child.getId() != R.id.layoutRemainCount) {
                params = child.getLayoutParams();

                params.width = usableSize;
                params.height = usableSize;
                child.setLayoutParams(params);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(Context context) {
        mRootView = (ViewGroup) View.inflate(context, R.layout.view_door, null);

        mLayoutRemainCount = mRootView.findViewById(R.id.layoutRemainCount);
        mTxvRemainderCount = (TextView) mRootView.findViewById(R.id.txvRemainCount);
        mWave1 = (ImageView) mRootView.findViewById(R.id.wave1);
        mWave2 = (ImageView) mRootView.findViewById(R.id.wave2);
        mImgOpeningRotate = (ImageView) mRootView.findViewById(R.id.imgOpeningRotate);
        mImgOpeningAlpha = (ImageView) mRootView.findViewById(R.id.imgOpeningAlpha);
        mTxvDoorTip = (TextView) mRootView.findViewById(R.id.txvDoorTip);
        mTxvDoorName = (TextView) mRootView.findViewById(R.id.txvDoorName);
        mImgOpenedTip = (ImageView) mRootView.findViewById(R.id.imgOpenedTip);

        mAnimWave1 = initScaleAnimationSet();
        mAnimWave2 = initScaleAnimationSet();
        mAnimOpeningRotate = initOpeningRotateAnimationSet();
        mAnimOpeningAlpha = initOpeningAlphaAnimationSet();
        mAnimOpenedTip = initOpenedTipAnimationSet();

        addView(mRootView);
    }

    private String getDefaultTipText() {
        return "点击开门";
    }

    private Animation initScaleAnimationSet() {
        //放大动画。
        ScaleAnimation sa = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(OFFSET * 4);
        sa.setRepeatCount(Animation.INFINITE);// 设置循环

        //透明动画。
        AlphaAnimation aa = new AlphaAnimation(0.6f, 0);
        aa.setStartOffset(OFFSET);
        aa.setDuration(OFFSET * 3);
        aa.setRepeatCount(Animation.INFINITE);//设置循环

        AnimationSet as = new AnimationSet(true);
        as.addAnimation(sa);
        as.addAnimation(aa);
        return as;
    }

    private Animation initOpeningRotateAnimationSet() {
        //参数1：从哪个旋转角度开始
        //参数2：转到什么角度
        //后4个参数用于设置围绕着旋转的圆的圆心在哪里
        //参数3：确定x轴坐标的类型，有ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标
        //参数4：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
        //参数5：确定y轴坐标的类型
        //参数6：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
        RotateAnimation ra = new RotateAnimation(3600, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(MIN_TIME * 10);
        ra.setInterpolator(new LinearInterpolator());

        //透明动画。
        AlphaAnimation aa1 = new AlphaAnimation(0, 1);
        aa1.setDuration((long) (OFFSET * 0.5));

        mAnimOpeningRotateEnd = new AlphaAnimation(1, 0);
        mAnimOpeningRotateEnd.setDuration(OFFSET);
        mAnimOpeningRotateEnd.setAnimationListener(new AnimationListener(this) {
            @Override
            public void onAnimationEnd(Animation animation) {
                mImgOpeningRotate.setVisibility(GONE);

                super.onAnimationEnd(animation);
            }
        });

        AnimationSet as = new AnimationSet(true);
        as.setInterpolator(new LinearInterpolator());
        as.addAnimation(ra);
        as.addAnimation(aa1);

        as.setAnimationListener(new AnimationListener(this) {
            @Override
            public void onAnimationEnd(Animation animation) {
                mImgOpeningRotate.setVisibility(GONE);

                super.onAnimationEnd(animation);
            }
        });

        return as;
    }

    private Animation initOpeningAlphaAnimationSet() {
        //透明动画。
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(MIN_TIME);

        mAnimOpeningAlphaEnd = new AlphaAnimation(1, 0);
        mAnimOpeningAlphaEnd.setDuration((long) (OFFSET * 1.5));
        mAnimOpeningAlphaEnd.setAnimationListener(new AnimationListener(this) {
            @Override
            public void onAnimationEnd(Animation animation) {
                mTxvDoorTip.setText(getDefaultTipText());
                mImgOpeningAlpha.setVisibility(GONE);

                super.onAnimationEnd(animation);
            }
        });

        aa.setAnimationListener(new AnimationListener(this));

        return aa;
    }

    private Animation initOpenedTipAnimationSet() {
        //放大动画。
        ScaleAnimation sa = new ScaleAnimation(1.2f, 1.4f, 1.2f, 1.4f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(OFFSET * 2);

        //透明动画。
        AlphaAnimation aa1 = new AlphaAnimation(0.1f, 1);
        aa1.setDuration((long) (OFFSET * 0.5));
        aa1.setFillAfter(true);

        AlphaAnimation aa2 = new AlphaAnimation(1, 0);
        aa2.setStartOffset(OFFSET);
        aa2.setDuration(OFFSET);

        AnimationSet as = new AnimationSet(true);
        as.addAnimation(sa);
        as.addAnimation(aa1);
        as.addAnimation(aa2);

        as.setAnimationListener(new AnimationListener(this) {
            @Override
            public void onAnimationStart(Animation animation) {
                super.onAnimationStart(animation);

                mTxvDoorTip.setVisibility(INVISIBLE);
                mTxvDoorName.setVisibility(INVISIBLE);
                mTxvDoorTip.setText(getDefaultTipText());
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTxvDoorTip.setVisibility(VISIBLE);
                mTxvDoorName.setVisibility(VISIBLE);
                mImgOpenedTip.setVisibility(GONE);

                super.onAnimationEnd(animation);
            }
        });

        return as;
    }

    private void clearWaveAnimation() {
        mAnimWave1.cancel();
        mAnimWave2.cancel();

        mWave1.clearAnimation();
        mWave2.clearAnimation();
    }

    private void clearOpeningAnimation() {
        mAnimOpeningRotate.cancel();
        mAnimOpeningAlpha.cancel();
        mImgOpeningRotate.clearAnimation();
        mImgOpeningAlpha.clearAnimation();
    }

    private void clearOpenedAnimation() {
        mAnimOpeningAlphaEnd.cancel();
        mAnimOpenedTip.cancel();

        mImgOpeningAlpha.clearAnimation();
        mImgOpenedTip.clearAnimation();
    }
}
