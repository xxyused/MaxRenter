package com.maxcloud.renter.util.message;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.entity.user.MsgContent;
import com.maxcloud.renter.entity.user.MsgLabel;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.OSHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatAdapter extends BaseAdapter {
    private class LocalImageView {
        private ImageView mView;
        private String mPath;

        public LocalImageView(ImageView imageView, String localPath) {
            mView = imageView;
            mPath = localPath;
        }

        public void showImage() {
            if (TextUtils.isEmpty(mPath)) {
                mView.setImageBitmap(null);
            } else {
                //mView.setImageBitmap(BitmapLoader.getBitmapFromFile(mPath));
            }
        }
    }

    private List<MsgContent> mNotifies;
    private Context mContext;
    private Map<String, String> mImageBuffer;
    private Date mTipTime;

    public ChatAdapter(Context context) {
        mNotifies = new ArrayList<MsgContent>();
        mImageBuffer = new HashMap<String, String>();
        mContext = context;
    }

//    public NotifyKey[] getAllKeys() {
//        int count = mNotifies.size();
//        NotifyKey[] keys = new NotifyKey[count];
//        for (int i = 0; i < count; i++) {
//            keys[i] = mNotifies.get(i).getNotify();
//        }
//
//        return keys;
//    }

    @Override
    public int getCount() {
        return mNotifies.size();
    }

    @Override
    public MsgContent getItem(int position) {
        if (position < 0 || position >= getCount()) {
            return null;
        }

        return mNotifies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(int index, MsgContent notifyView) {
        mNotifies.add(index, notifyView);
    }

    public void addItem(MsgContent notify) {
        mNotifies.add(notify);
    }

    public void clear() {
        mNotifies.clear();
    }

    public void recycle() {
        try {
            for (Map.Entry<String, String> item : mImageBuffer.entrySet()) {
                File file = new File(item.getValue());
                if (file.exists()) {
                    file.delete();
                }
            }

            mImageBuffer.clear();
        } catch (Exception e) {
            L.e("recycle", e);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MsgContent notify = getItem(position);
        if (notify.isMyselfSend()) {
            convertView = View.inflate(mContext, R.layout.item_chat_send_text, null);

            //notify.setUiView(convertView);
        } else {
            convertView = View.inflate(mContext, R.layout.item_chat_receive_text, null);
        }

        // 显示时间
        TextView msgTime = (TextView) convertView.findViewById(R.id.txvSendTime);
        Date nowDate = notify.getCreateTime();
        if (position != 0) {
            Date lastDate = getItem(position - 1).getCreateTime();
            // 如果两条消息之间的间隔超过一分钟则显示时间
            if (nowDate.getTime() - lastDate.getTime() > 1 * 60 * 1000) {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                msgTime.setText(format1.format(nowDate));
                msgTime.setVisibility(View.VISIBLE);
            } else {
                msgTime.setVisibility(View.GONE);
            }
        } else {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            msgTime.setText(format1.format(nowDate));
        }

        LinearLayout layoutContent = (LinearLayout) convertView.findViewById(R.id.layoutContent);
        layoutContent.removeAllViews();

        StringBuilder strBuilder = new StringBuilder();
        for (MsgLabel viewItem : notify.getmItems()) {
            switch (viewItem.getType()) {
                case MsgLabel.TYPE_ATTN:
                    //strBuilder.append(HtmlHelper.getHyperLinkTip(textToHtmlText(viewItem.getText())));
                    break;
                case MsgLabel.TYPE_IMAGE:
                    if (strBuilder.length() > 0) {
                        View textView = createTextView(strBuilder.toString());

                        layoutContent.addView(textView);
                        strBuilder.delete(0, strBuilder.length());
                    }

                    ImageView imageView = createImageView(viewItem);

                    layoutContent.addView(imageView);
                    break;
                default:
                    strBuilder.append(textToHtmlText(viewItem.getText()));
                    break;
            }
        }

        if (strBuilder.length() > 0) {
            View textView = createTextView(strBuilder.toString());

            layoutContent.addView(textView);
            strBuilder.delete(0, strBuilder.length());
        }

        View view = convertView.findViewById(R.id.layoutItem);
        view.setTag(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();

                onItemClick(position);
            }
        });

        if (position == (getCount() - 1)) {
            int padding = OSHelper.dp2px(mContext, 16);
            convertView.setPadding(0, padding, 0, padding);
        }

        return convertView;
    }

    protected void onItemClick(int position) {

    }

    private String textToHtmlText(CharSequence text) {
        return text.toString().replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;").replaceAll("'", "&apos;")
                .replaceAll(" ", "&nbsp;").replaceAll("\n", "<br>");
    }

    private View createTextView(String htmlText) {
        int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wrapContent, wrapContent);

        TextView textView = new TextView(mContext);
        textView.setIncludeFontPadding(false);
        textView.setLayoutParams(params);
        textView.setText(Html.fromHtml(htmlText));
        textView.setTextSize(16);
        textView.setPadding(0, 0, 0, 0);
        textView.setTextColor(Color.parseColor("#474443"));

        return textView;
    }

    private ImageView createImageView(MsgLabel notifyItem) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, OSHelper.dp2px(mContext, 122));
        params.gravity = Gravity.CENTER_HORIZONTAL;

        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setAdjustViewBounds(true);

        new AsyncTask<Object, Void, LocalImageView>() {
            @Override
            protected LocalImageView doInBackground(Object... params) {
                ImageView imgView = (ImageView) params[0];
                MsgLabel notifyItem = (MsgLabel) params[1];

                String localPath = null;
                try {
                    String ossPath = notifyItem.getValue();
                    if (mImageBuffer.containsKey(ossPath)) {
                        localPath = mImageBuffer.get(ossPath);
                    } else {
                        //localPath = PublicOss.getChatImage(ossPath, true);
                        mImageBuffer.put(ossPath, localPath);
                    }
                } catch (Exception e) {
                    L.e("ChatAdapter.loadOssImage", e);

                    localPath = null;
                }

                return new LocalImageView(imgView, localPath);
            }

            @Override
            protected void onPostExecute(LocalImageView imageView) {
                imageView.showImage();
            }
        }.execute(imageView, notifyItem);

        return imageView;
    }

    private ImageView createImageViewFromLocal(String imgPath) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 300);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setAdjustViewBounds(true);
        //imageView.setImageBitmap(BitmapLoader.getBitmapFromFile(imgPath));

        return imageView;
    }
}
