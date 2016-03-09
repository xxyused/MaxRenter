package com.maxcloud.renter.entity.build;

/**
 * 描    述：
 * 作    者：向晓阳
 * 时    间：2016/3/3
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class CardInfo {
    private long cardno;
    private String time;
    private int cardtype;
    private int personid;

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
}
