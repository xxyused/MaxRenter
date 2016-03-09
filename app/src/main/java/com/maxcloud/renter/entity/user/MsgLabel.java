package com.maxcloud.renter.entity.user;

import com.maxcloud.renter.util.L;

import org.json.JSONObject;

/**
 * Created by MAX-XXY on 2016/3/2.
 */
public class MsgLabel {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_ATTN = 1;
    public static final int TYPE_IMAGE = 2;

    public static final MsgLabel fromJson(String jsonStr) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            int type = json.optInt("type", 0);
            switch (type) {
                case TYPE_ATTN:
                    String tel = json.optString("tel");
                    String name = json.optString("name", tel);

                    return new MsgLabel(type, name, tel);
                case TYPE_IMAGE:
                    return new MsgLabel(type, "[图片]", json.optString("src"));
            }
        } catch (Exception e) {
            L.e("NotifyViewItem.parseContent", e);
        }

        return new MsgLabel(jsonStr);
    }

    public static final String toJsonString(int type, String value) {
        try {
            JSONObject json = new JSONObject();
            json.put("type", type);
            switch (type) {
                case TYPE_ATTN:
                    json.put("name", value);
                    json.put("tel", value);
                case TYPE_IMAGE:
                    json.put("src", value);
            }

            return json.toString();
        } catch (Exception e) {
            L.e("NotifyViewItem.toJsonString", e);
        }

        return null;
    }

    public static final String toJsonString(int type, String text, String value) {
        try {
            JSONObject json = new JSONObject();
            json.put("type", type);
            switch (type) {
                case TYPE_ATTN:
                    json.put("name", text);
                    json.put("tel", value);
                case TYPE_IMAGE:
                    json.put("src", value);
            }

            return json.toString();
        } catch (Exception e) {
            L.e("NotifyViewItem.toJsonString", e);
        }

        return null;
    }

    private int mType;
    private CharSequence mText;
    private String mValue;

    public int getType() {
        return mType;
    }

    public CharSequence getText() {
        return mText;
    }

    public String getValue() {
        return mValue;
    }

    public MsgLabel(CharSequence text) {
        mType = TYPE_TEXT;
        mText = text;
    }

    private MsgLabel(int type, String text, String value) {
        mType = type;
        mText = text;
        mValue = value;
    }
}
