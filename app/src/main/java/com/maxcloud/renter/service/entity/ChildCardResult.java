package com.maxcloud.renter.service.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：小孩卡信息。
 * 作    者：向晓阳
 * 时    间：2016/3/7
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class ChildCardResult {
    @SerializedName("cardNo")
    @Expose
    private String cardNo;
    @SerializedName("cardType")
    @Expose
    private int cardType;
    @SerializedName("buildings")
    @Expose
    private List<ChildBuildingResult> buildings = new ArrayList<>();

    /**
     * @return The cardNo
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * @param cardNo The cardNo
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * @return The cardType
     */
    public int getCardType() {
        return cardType;
    }

    /**
     * @param cardType The cardType
     */
    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    /**
     * @return The buildings
     */
    public List<ChildBuildingResult> getBuildings() {
        return buildings;
    }

    /**
     * @param buildings The buildings
     */
    public void setBuildings(List<ChildBuildingResult> buildings) {
        this.buildings = buildings;
    }
}
