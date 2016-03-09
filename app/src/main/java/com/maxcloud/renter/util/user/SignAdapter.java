package com.maxcloud.renter.util.user;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxcloud.renter.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SignAdapter extends BaseAdapter {
    public static class SignItemData {
        public JSONObject Value;
        public int ImageRes;
        public String Name;
    }

    private class SignItemView {
        public ImageView ImgSign;
        public TextView TxvName;
        public View LineDivider;
    }

    private Context mContext;
    private List<SignItemData> mDatas;

    public SignAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<SignItemData>();
    }

    public void add(SignItemData itemData) {
        mDatas.add(itemData);
    }

    public void reload() {

    }

    public void addAll(Collection<SignItemData> collection) {
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
    public SignItemData getItem(int position) {
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
        SignItemView itemView = null;
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.item_sign_name, null);

            itemView = new SignItemView();
            itemView.ImgSign = (ImageView) convertView.findViewById(R.id.imgSign);
            itemView.TxvName = (TextView) convertView.findViewById(R.id.txvName);
            itemView.LineDivider = convertView.findViewById(R.id.lineDivider);

            convertView.setTag(itemView);
        } else {
            itemView = (SignItemView) convertView.getTag();
        }

        SignItemData itemData = getItem(position);
        itemView.ImgSign.setImageResource(itemData.ImageRes);
        itemView.TxvName.setText(itemData.Name);
        if (position < (getCount() - 1)) {
            itemView.LineDivider.setVisibility(View.VISIBLE);
        } else {
            itemView.LineDivider.setVisibility(View.GONE);
        }

        return convertView;
    }

}
