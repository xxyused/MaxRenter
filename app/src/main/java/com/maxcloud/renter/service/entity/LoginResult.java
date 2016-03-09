package com.maxcloud.renter.service.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 描    述：服务器返回的登录信息。
 * 作    者：向晓阳
 * 时    间：2016/1/29
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class LoginResult extends BaseResult implements Parcelable {
    @SerializedName("index")
    @Expose
    private String index;
    @SerializedName("account")
    @Expose
    private String account;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("modify_time")
    @Expose
    private String modifyTime;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("des")
    @Expose
    private String des;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("weixin")
    @Expose
    private String weixin;
    @SerializedName("qq")
    @Expose
    private String qq;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("extend_data")
    @Expose
    private String extendData;
    @SerializedName("certificate_type")
    @Expose
    private String certificateType;
    @SerializedName("certificate")
    @Expose
    private String certificate;
    @SerializedName("appliedRange")
    @Expose
    private String appliedRange;
    @SerializedName("nowtime")
    @Expose
    private String nowtime;
    @SerializedName("doors_result")
    @Expose
    private String doorResult;
    @SerializedName("doors_list")
    @Expose
    private List<List<String>> doors;

    /**
     *
     * @return
     * The index
     */
    public String getIndex() {
        return index;
    }

    /**
     *
     * @param index
     * The index
     */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     *
     * @return
     * The account
     */
    public String getAccount() {
        return account;
    }

    /**
     *
     * @param account
     * The account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The state
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     * The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return
     * The sex
     */
    public String getSex() {
        return sex;
    }

    /**
     *
     * @param sex
     * The sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     *
     * @return
     * The createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     *
     * @param createTime
     * The create_time
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     *
     * @return
     * The modifyTime
     */
    public String getModifyTime() {
        return modifyTime;
    }

    /**
     *
     * @param modifyTime
     * The modify_time
     */
    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     * The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     * The des
     */
    public String getDes() {
        return des;
    }

    /**
     *
     * @param des
     * The des
     */
    public void setDes(String des) {
        this.des = des;
    }

    /**
     *
     * @return
     * The birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     *
     * @param birthday
     * The birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     *
     * @return
     * The weixin
     */
    public String getWeixin() {
        return weixin;
    }

    /**
     *
     * @param weixin
     * The weixin
     */
    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    /**
     *
     * @return
     * The qq
     */
    public String getQq() {
        return qq;
    }

    /**
     *
     * @param qq
     * The qq
     */
    public void setQq(String qq) {
        this.qq = qq;
    }

    /**
     *
     * @return
     * The address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     * The extendData
     */
    public String getExtendData() {
        return extendData;
    }

    /**
     *
     * @param extendData
     * The extend_data
     */
    public void setExtendData(String extendData) {
        this.extendData = extendData;
    }

    /**
     *
     * @return
     * The certificateType
     */
    public String getCertificateType() {
        return certificateType;
    }

    /**
     *
     * @param certificateType
     * The certificate_type
     */
    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    /**
     *
     * @return
     * The certificate
     */
    public String getCertificate() {
        return certificate;
    }

    /**
     *
     * @param certificate
     * The certificate
     */
    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    /**
     *
     * @return
     * The appliedRange
     */
    public String getAppliedRange() {
        return appliedRange;
    }

    /**
     *
     * @param appliedRange
     * The appliedRange
     */
    public void setAppliedRange(String appliedRange) {
        this.appliedRange = appliedRange;
    }

    /**
     *
     * @return
     * The nowtime
     */
    public String getNowtime() {
        return nowtime;
    }

    /**
     *
     * @param nowtime
     * The nowtime
     */
    public void setNowtime(String nowtime) {
        this.nowtime = nowtime;
    }

    /**
     * @return The doorResult
     */
    public String getDoorResult() {
        return doorResult;
    }

    /**
     * @param doorResult The door_result
     */
    public void setDoorResult(String doorResult) {
        this.doorResult = doorResult;
    }

    /**
     * @return The doors
     */
    public List<List<String>> getDoors() {
        return doors;
    }

    /**
     * @param doors The doors
     */
    public void setDoors(List<List<String>> doors) {
        this.doors = doors;
    }

    public static final Parcelable.Creator<LoginResult> CREATOR = new Parcelable.Creator<LoginResult>() {
        public LoginResult createFromParcel(Parcel source) {
            LoginResult mLoginResult = new LoginResult();
            mLoginResult.setCode(source.readInt());
            mLoginResult.setError(source.readString());
            mLoginResult.doorResult = source.readString();
            mLoginResult.doors = source.readArrayList(ClassLoader.getSystemClassLoader());

            return mLoginResult;
        }

        public LoginResult[] newArray(int size) {
            return new LoginResult[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getCode());
        dest.writeString(this.getError());
        dest.writeString(this.doorResult);
        dest.writeList(this.doors);
    }
}
