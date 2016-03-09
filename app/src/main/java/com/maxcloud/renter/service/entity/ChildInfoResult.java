package com.maxcloud.renter.service.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：服务器返回的小孩信息。
 * 作    者：向晓阳
 * 时    间：2016/3/3
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class ChildInfoResult {
    @SerializedName("idcardno")
    @Expose
    private String idCardNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cards")
    @Expose
    private List<ChildCardResult> cards = new ArrayList<>();

    /**
     * @return The idcardno
     */
    public String getIdcardno() {
        return idCardNo;
    }

    /**
     * @param idCardNo The idcardno
     */
    public void setIdcardno(String idCardNo) {
        this.idCardNo = idCardNo;
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
     * @return The cards
     */
    public List<ChildCardResult> getCards() {
        return cards;
    }

    /**
     * @param cards The cards
     */
    public void setCards(List<ChildCardResult> cards) {
        this.cards = cards;
    }
}
