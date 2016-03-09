package com.idcard;

import android.text.TextUtils;

public class OcrIdCard {
    private String mName;
    private String mSex;
    private String mFolk;
    private String mBirthDay;
    private String mAddress;
    private String mCardNum;
    private String mIssue;
    private String mPeriod;
    private byte[] mPortrait;
    private String mCardImage;
    private int mRotation;

    public boolean isEmpty() {
        return TextUtils.isEmpty(mCardNum);
    }

    public void setNull() {
        mName = "";
        mSex = "";
        mFolk = "";
        mAddress = "";
        mBirthDay = "";
        mCardNum = "";
        mIssue = "";
        mPeriod = "";
        mPortrait = null;
        mCardImage = null;
        mRotation = 0;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(String sex) {
        this.mSex = sex;
    }

    public String getFolk() {
        return mFolk;
    }

    public void setFolk(String folk) {
        this.mFolk = folk;
    }

    public String getBirthDay() {
        return mBirthDay;
    }

    public void setBirthDay(String birthDay) {
        mBirthDay = birthDay;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getCardNum() {
        return mCardNum;
    }

    public void setCardNum(String cardNum) {
        mCardNum = cardNum;
    }

    public String getIssue() {
        return mIssue;
    }

    public void setIssue(String issue) {
        this.mIssue = issue;
    }

    public String getPeriod() {
        return mPeriod;
    }

    public void setPeriod(String period) {
        this.mPeriod = period;
    }

    public void setPortrait(byte[] data, int length) {
        mPortrait = new byte[length];
        System.arraycopy(data, 0, mPortrait, 0, length);
    }

    public byte[] getPortrait() {
        return this.mPortrait;
    }

    public void setCardImage(String imgPath) {
        mCardImage = imgPath;
    }

    public String getCardImage() {
        return mCardImage;
    }

    public void setRotation(int rotation) {
        mRotation = rotation;
    }

    public int getRotation() {
        return mRotation;
    }
}
