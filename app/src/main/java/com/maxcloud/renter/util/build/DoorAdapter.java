package com.maxcloud.renter.util.build;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.maxcloud.renter.R;
import com.maxcloud.renter.customview.DoorView;
import com.maxcloud.renter.entity.build.DoorInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAX-XXY on 2016/2/17.
 */
public class DoorAdapter extends PagerAdapter {

    public interface OnItemClickListener {
        void onClick(DoorView doorView, boolean isOpenDoor);
    }

    private class DoorItemView {
        private DoorView mView;
        private View mBtnDoor;
        private View mLayoutRemainCount;

        private View.OnClickListener mOnClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null == mListener) {
                    return;
                }

                switch (v.getId()) {
                    case R.id.btnDoor:
                        mListener.onClick(mView, true);
                        break;
                    case R.id.layoutRemainCount:
                        mListener.onClick(mView, false);
                        break;
                }
            }
        };

        public DoorItemView(DoorView view) {
            mView = view;

            mBtnDoor = view.findViewById(R.id.btnDoor);
            mLayoutRemainCount = view.findViewById(R.id.layoutRemainCount);
        }

        public void reset(DoorInfo doorItem) {
            int openCountRest = doorItem.getOpenCountRest();
            mView.setServerId(doorItem.getServerId());
            mView.setDoorId(doorItem.getId());
            mView.setDoorName(doorItem.getName());
            mView.setTotalCount(doorItem.getOpenCountTotal());
            mView.setRemainderCount(openCountRest);
            mView.setShowRemainder(openCountRest >= 0);
        }

        public View create(ViewGroup parent) {
            parent.addView(mView);
            mView.showWaveAnimation(0);

            if (null != mBtnDoor) {
                mBtnDoor.setOnClickListener(mOnClick);
            }
            if (null != mLayoutRemainCount) {
                mLayoutRemainCount.setOnClickListener(mOnClick);
            }

            return mView;
        }

        public void delete() {
            mView.clearAllAnimation();

            if (null != mBtnDoor) {
                mBtnDoor.setOnClickListener(null);
            }
            if (null != mLayoutRemainCount) {
                mLayoutRemainCount.setOnClickListener(null);
            }

            ViewParent parent = mView.getParent();
            if (null != parent) {
                ((ViewGroup) parent).removeView(mView);
            }
        }
    }

    private List<DoorItemView> mViews;
    private OnItemClickListener mListener;
    //所有门对象，主要用于比较两个门列表是否有变化。
    private String mAllDoorCode = "";

    public String getAllDoorCode() {
        return mAllDoorCode;
    }

    public DoorAdapter() {
        mViews = new ArrayList<>();
    }

    public void clear() {
        for (DoorItemView itemView : mViews) {
            itemView.delete();
        }
        mViews.clear();
        mAllDoorCode = "";
    }

    public void add(DoorView view, DoorInfo doorItem) {
        DoorItemView itemView = new DoorItemView(view);
        itemView.reset(doorItem);

        mViews.add(itemView);
        mAllDoorCode += String.format("%s_%d_%s_%d_%d", doorItem.getServerId(), doorItem.getId(), doorItem.getName(), doorItem.getOpenCountTotal(), doorItem.getOpenCountRest());
    }

    public void setDoorClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * 获得当前界面数
     */
    @Override
    public int getCount() {
        return mViews.size();
    }

    /**
     * 获取对象的位置是否改变。
     */
    @Override
    public int getItemPosition(Object object) {
        //if (mViews.contains(object)) {
        //    return super.getItemPosition(object);
        //} else {
        return PagerAdapter.POSITION_NONE;
        //}
    }

    /**
     * 初始化position位置的界面
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        DoorItemView itemView = mViews.get(position);

        return itemView.create(container);
    }

    /**
     * 判断是否由对象生成界面
     */
    @Override
    public boolean isViewFromObject(View view, Object arg1) {
        return (view == arg1);
    }

    /**
     * 销毁position位置的界面
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position >= 0 && position < mViews.size()) {
            DoorItemView itemView = mViews.get(position);

            itemView.delete();
        }
    }
}
