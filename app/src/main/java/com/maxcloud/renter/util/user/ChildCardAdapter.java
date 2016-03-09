package com.maxcloud.renter.util.user;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.service.entity.ChildBuildingResult;
import com.maxcloud.renter.service.entity.ChildCardResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChildCardAdapter extends BaseAdapter {
    private class ChildBuildingView {
        private ChildBuildingAdapter _adapter;

        public TextView TxvCardType;
        public TextView TxvCardNo;
        private ListView LsvItems;

        public ChildBuildingView(Context context) {
            _adapter = new ChildBuildingAdapter(context);
        }

        public void setLsvItems(ListView lsvItems) {
            LsvItems = lsvItems;
            LsvItems.setAdapter(_adapter);
        }

        public void addAllCard(Collection<ChildBuildingResult> childBuildings) {
            _adapter.clear();
            _adapter.addAll(childBuildings);
            _adapter.notifyDataSetChanged();
        }
    }

    private Context mContext;
    private List<ChildCardResult> mDatas;

    public ChildCardAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<>();
    }

    public void add(ChildCardResult itemData) {
        mDatas.add(itemData);
    }

    public void addAll(Collection<ChildCardResult> collection) {
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
    public ChildCardResult getItem(int position) {
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
            convertView = View.inflate(mContext, R.layout.item_child_card_view, null);

            itemView = new ChildBuildingView(mContext);
            itemView.TxvCardType = (TextView) convertView.findViewById(R.id.txvCardType);
            itemView.TxvCardNo = (TextView) convertView.findViewById(R.id.txvCardNo);
            itemView.setLsvItems((ListView) convertView.findViewById(R.id.lsvItems));

            convertView.setTag(itemView);
        } else {
            itemView = (ChildBuildingView) convertView.getTag();
        }

        ChildCardResult itemData = getItem(position);
        itemView.TxvCardType.setText(itemData.getCardType() == 3 ? "身份证" : "IC卡");
        itemView.TxvCardNo.setText(itemData.getCardNo());
        itemView.addAllCard(itemData.getBuildings());

        return convertView;
    }

}
