package com.maxcloud.renter.util.user;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.service.entity.ChildCardResult;
import com.maxcloud.renter.service.entity.ChildInfoResult;
import com.maxcloud.renter.util.OSHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChildListAdapter extends BaseAdapter {
    private class ChildInfoView {
        private ChildCardAdapter _adapter;

        public TextView TxvIdCardNo;
        public TextView TxvName;
        private ListView LsvItems;

        public ChildInfoView(Context context) {
            _adapter = new ChildCardAdapter(context);
        }

        public void setLsvItems(ListView lsvItems) {
            LsvItems = lsvItems;
            LsvItems.setAdapter(_adapter);
        }

        public void addAllCard(Collection<ChildCardResult> childCards) {
            _adapter.clear();
            _adapter.addAll(childCards);
            _adapter.notifyDataSetChanged();
        }
    }

    private Context mContext;
    private List<ChildInfoResult> mDatas;

    public ChildListAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<>();
    }

    public void add(ChildInfoResult itemData) {
        mDatas.add(itemData);
    }

    public void reload() {

    }

    public void addAll(Collection<ChildInfoResult> collection) {
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
    public ChildInfoResult getItem(int position) {
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
        ChildInfoView itemView = null;
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.item_child_info_view, null);

            itemView = new ChildInfoView(mContext);
            itemView.TxvIdCardNo = (TextView) convertView.findViewById(R.id.txvIdCardNo);
            itemView.TxvName = (TextView) convertView.findViewById(R.id.txvName);
            itemView.setLsvItems((ListView) convertView.findViewById(R.id.lsvItems));

            convertView.setTag(itemView);
        } else {
            itemView = (ChildInfoView) convertView.getTag();
        }

        ChildInfoResult itemData = getItem(position);
        itemView.TxvIdCardNo.setText(itemData.getIdcardno());
        itemView.TxvName.setText(itemData.getName());
        itemView.addAllCard(itemData.getCards());

        if (position > 0) {
            convertView.setPadding(0, OSHelper.dp2px(mContext, 10), 0, 0);
        } else {
            convertView.setPadding(0, 0, 0, 0);
        }

        return convertView;
    }

}
