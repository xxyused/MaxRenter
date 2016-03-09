package com.maxcloud.renter.util.user;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.service.entity.ChildBuildingResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChildBuildingAdapter extends BaseAdapter {
    private class ChildBuildingView {
        public TextView TxvName;
    }

    private Context mContext;
    private List<ChildBuildingResult> mDatas;

    public ChildBuildingAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<>();
    }

    public void add(ChildBuildingResult itemData) {
        mDatas.add(itemData);
    }

    public void addAll(Collection<ChildBuildingResult> collection) {
        mDatas.addAll(collection);
    }

    public void clear() {
        mDatas.clear();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public ChildBuildingResult getItem(int position) {
        if (position < 0 || position >= mDatas.size()) {
            return null;
        }

        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChildBuildingView itemView = null;
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.item_child_building_view, null);

            itemView = new ChildBuildingView();
            itemView.TxvName = (TextView) convertView.findViewById(R.id.txvName);

            convertView.setTag(itemView);
        } else {
            itemView = (ChildBuildingView) convertView.getTag();
        }

        ChildBuildingResult itemData = getItem(position);
        itemView.TxvName.setText(itemData.getName());

        return convertView;
    }

}
