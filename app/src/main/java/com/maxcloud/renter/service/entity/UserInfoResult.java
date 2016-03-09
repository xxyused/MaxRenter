package com.maxcloud.renter.service.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MAX-XXY on 2016/2/24.
 */
public class UserInfoResult extends BaseResult {
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
    @SerializedName("count")
    @Expose
    private String count;
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
    private String idCardNo;
    @SerializedName("appliedRange")
    @Expose
    private String appliedRange;

    /**
     * @return The index
     */
    public String getIndex() {
        return index;
    }

    /**
     * @param index The index
     */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * @return The account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account The account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return The sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex The sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return The count
     */
    public String getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(String count) {
        this.count = count;
    }

    /**
     * @return The createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime The create_time
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return The modifyTime
     */
    public String getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime The modify_time
     */
    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The phone
     */
    public String getPhoneNo() {
        return phone;
    }

    /**
     * @param phone The phone
     */
    public void setPhoneNo(String phone) {
        this.phone = phone;
    }

    /**
     * @return The des
     */
    public String getDes() {
        return des;
    }

    /**
     * @param des The des
     */
    public void setDes(String des) {
        this.des = des;
    }

    /**
     * @return The birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * @param birthday The birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * @return The weixin
     */
    public String getWeixin() {
        return weixin;
    }

    /**
     * @param weixin The weixin
     */
    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    /**
     * @return The qq
     */
    public String getQq() {
        return qq;
    }

    /**
     * @param qq The qq
     */
    public void setQq(String qq) {
        this.qq = qq;
    }

    /**
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return The extendData
     */
    public String getExtendData() {
        return extendData;
    }

    /**
     * @param extendData The extend_data
     */
    public void setExtendData(String extendData) {
        this.extendData = extendData;
    }

    /**
     * @return The certificateType
     */
    public String getCertificateType() {
        return certificateType;
    }

    /**
     * @param certificateType The certificate_type
     */
    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    /**
     * @return The idCardNo
     */
    public String getIdCardNo() {
        return idCardNo;
    }

    /**
     * @param idCardNo The idCardNo
     */
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    /**
     * @return The appliedRange
     */
    public String getAppliedRange() {
        return appliedRange;
    }

    /**
     * @param appliedRange The appliedRange
     */
    public void setAppliedRange(String appliedRange) {
        this.appliedRange = appliedRange;
    }
}
