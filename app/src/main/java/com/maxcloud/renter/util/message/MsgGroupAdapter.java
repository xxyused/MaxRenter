package com.maxcloud.renter.util.message;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.entity.user.MsgContent;
import com.maxcloud.renter.entity.user.MsgLabel;
import com.maxcloud.renter.entity.user.MsgGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAX-XXY on 2016/3/2.
 */
public class MsgGroupAdapter extends BaseAdapter {
    private static class MsgGroupView {
        private TextView mTxvCount;
        private ImageView mImgIcon;
        private TextView mTxvTitle;
        private TextView mTxvDate;
        private TextView mTxvText;

        public MsgGroupView(View parent) {
            mTxvCount = (TextView) parent.findViewById(R.id.txvCount);
            mImgIcon = (ImageView) parent.findViewById(R.id.imgIcon);
            mTxvTitle = (TextView) parent.findViewById(R.id.txvTitle);
            mTxvDate = (TextView) parent.findViewById(R.id.txvDate);
            mTxvText = (TextView) parent.findViewById(R.id.txvText);
        }

        public void reset(MsgGroup msgGroup) {
            MsgLabel labels[] = MsgContent.parseContent(msgGroup.getContent());
            StringBuilder strBuilder = new StringBuilder();
            for (MsgLabel viewItem : labels) {
                strBuilder.append(viewItem.getText());
            }

            int count = msgGroup.getCount();
            if ("System".equals(msgGroup.getAccount())) {
                mImgIcon.setImageResource(R.mipmap.ic_msg_max_head);
            } else {
                mImgIcon.setImageResource(R.mipmap.ic_msg_def_head);
            }
            mTxvCount.setText(String.valueOf(count));
            mTxvTitle.setText(msgGroup.getName());
            mTxvDate.setText(String.format("%tF", msgGroup.getSendTime()));
            mTxvText.setText(strBuilder.toString());

            if (count > 0) {
                mTxvCount.setVisibility(View.VISIBLE);
            } else {
                mTxvCount.setVisibility(View.GONE);
            }
        }
    }

    private Context mContext;
    private List<String> mFromIds;
    private List<MsgGroup> mGroupList;

    public MsgGroupAdapter(Context content) {
        mGroupList = new ArrayList<>();
        mFromIds = new ArrayList<>();
        mContext = content;
    }

    @Override
    public int getCount() {
        return mGroupList.size();
    }

    @Override
    public MsgGroup getItem(int position) {
        return mGroupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(MsgGroup item) {
        String fromId = item.getAccount();
        if (mFromIds.contains(fromId)) {
            for (MsgGroup msgGroup : mGroupList) {
                if (fromId.equals(msgGroup.getAccount())) {
                    msgGroup.merge(item);
                    return;
                }
            }
        } else {
            mGroupList.add(item);
            mFromIds.add(fromId);
        }
    }

    public void clear() {
        mGroupList.clear();
        mFromIds.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MsgGroupView itemView;

        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.item_message_group, null);

            itemView = new MsgGroupView(convertView);
            convertView.setTag(itemView);
        } else {
            itemView = (MsgGroupView) convertView.getTag();
        }

        itemView.reset(getItem(position));

        return convertView;
    }
}
