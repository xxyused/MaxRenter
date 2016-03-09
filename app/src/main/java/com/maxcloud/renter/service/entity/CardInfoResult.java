package com.maxcloud.renter.service.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 描    述：服务器返回的卡信息。
 * 作    者：向晓阳
 * 时    间：2016/2/29
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class CardInfoResult extends BaseResult {
    @SerializedName("doorid")
    @Expose
    private int doorid;
    @SerializedName("cardno")
    @Expose
    private long cardno;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("cardtype")
    @Expose
    private int cardtype;
    @SerializedName("personid")
    @Expose
    private int personid;
    @SerializedName("extenddata")
    @Expose
    private String extenddata;

    /**
     * @return The doorid
     */
    public int getDoorid() {
        return doorid;
    }

    /**
     * @param doorid The doorid
     */
    public void setDoorid(int doorid) {
        this.doorid = doorid;
    }

    /**
     * @return The cardno
     */
    public long getCardno() {
        return cardno;
    }

    /**
     * @param cardno The cardno
     */
    public void setCardno(long cardno) {
        this.cardno = cardno;
    }

    /**
     * @return The time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return The cardtype
     */
    public int getCardtype() {
        return cardtype;
    }

    /**
     * @param cardtype The cardtype
     */
    public void setCardtype(int cardtype) {
        this.cardtype = cardtype;
    }

    /**
     * @return The personid
     */
    public int getPersonid() {
        return personid;
    }

    /**
     * @param personid The personid
     */
    public void setPersonid(int personid) {
        this.personid = personid;
    }

    /**
     * @return The extenddata
     */
    public String getExtenddata() {
        return extenddata;
    }

    /**
     * @param extenddata The extenddata
     */
    public void setExtenddata(String extenddata) {
        this.extenddata = extenddata;
    }
}
