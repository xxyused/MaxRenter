package com.maxcloud.renter.entity.build;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by MAX-XXY on 2016/2/17.
 */
public class DoorInfo implements Serializable, Cloneable {
    /**
     * @author Administrator
     */
    public static class SortComparator implements Comparator<DoorInfo> {

        public SortComparator() {

        }

        @Override
        public int compare(DoorInfo lhs, DoorInfo rhs) {
            String lName = lhs.getName();
            String rName = rhs.getName();

            if (null == lName) {
                return null == rName ? 0 : 1;
            }
            int nameCompare = lhs.getName().compareTo(rhs.getName());
            if (nameCompare == 0) {
                String lFullName = lhs.getFullName();
                String rFullName = rhs.getFullName();

                if (null == lFullName) {
                    return null == rFullName ? 0 : 1;
                }

                return lhs.getFullName().compareTo(rhs.getFullName());
            }

            return nameCompare;
        }

    }

    @SerializedName("ServerId")
    @Expose
    private String mServerId;
    @SerializedName("BuildingId")
    @Expose
    private int mBuildingId;
    @SerializedName("Id")
    @Expose
    private int mId;
    @SerializedName("Name")
    @Expose
    private String mName;
    @SerializedName("FullName")
    @Expose
    private String mFullName;
    @SerializedName("OpenCountTotal")
    @Expose
    private int mOpenCountTotal;
    @SerializedName("OpenCountRest")
    @Expose
    private int mOpenCountRest = -1;

    /**
     * 获取服务器的唯一标识（因为Java里的UUID无法和GUID对应，所以直接用byte数组）。
     *
     * @return
     */
    public String getServerId() {
        return mServerId;
    }

    /**
     * 设置服务器的唯一标识。
     *
     * @param serverId
     */
    public DoorInfo setServerId(String serverId) {
        if (mServerId != serverId) {
            mServerId = serverId;
        }

        return this;
    }

    /**
     * 获取门所在楼栋ID。
     *
     * @return
     */
    public int getBuildingId() {
        return mBuildingId;
    }

    /**
     * 设置门所在楼栋ID。
     *
     * @return
     */
    public DoorInfo setBuildingId(int buildingId) {
        if (mBuildingId != buildingId) {
            mBuildingId = buildingId;
        }

        return this;
    }

    /**
     * 获取门id。
     *
     * @return 门id。
     */
    public int getId() {
        return mId;
    }

    /**
     * 设置门id。
     *
     * @param id 新的门id。
     * @return 返回本身。
     */
    public DoorInfo setId(int id) {
        if (mId != id) {
            mId = id;
        }

        return this;
    }

    /**
     * 获取门名称。
     *
     * @return 门名称。
     */
    public String getName() {
        return mName;
    }

    /**
     * 设置门名称。
     *
     * @param name 新的门名称。
     * @return 返回本身。
     */
    public DoorInfo setName(String name) {
        if (mName != name) {
            mName = name;
        }

        return this;
    }

    /**
     * 获取门全称。
     *
     * @return 门全称。
     */
    public String getFullName() {
        return mFullName;
    }

    /**
     * 设置门全称。
     *
     * @param fullName 新的门全称。
     * @return 返回本身。
     */
    public DoorInfo setFullName(String fullName) {
        if (mFullName != fullName) {
            mFullName = fullName;
        }

        return this;
    }

    /**
     * 获取可开门次数。
     *
     * @return 可开门次数
     */
    public int getOpenCountTotal() {
        return mOpenCountTotal;
    }

    /**
     * 设置可开门次数。
     *
     * @param openCountTotal 可开门次数
     */
    public void setOpenCountTotal(int openCountTotal) {
        mOpenCountTotal = openCountTotal;
    }

    /**
     * 获取剩余可开门次数。
     *
     * @return 剩余可开门次数
     */
    public int getOpenCountRest() {
        return mOpenCountRest;
    }

    /**
     * 设置剩余可开门次数。
     *
     * @param openCountRest 剩余可开门次数
     */
    public void setOpenCountRest(int openCountRest) {
        mOpenCountRest = openCountRest;
    }

    public DoorInfo() {
    }

    public Object clone() {
        DoorInfo newItem = new DoorInfo();
        newItem.mServerId = mServerId;
        newItem.mId = mId;
        newItem.mBuildingId = mBuildingId;
        newItem.mName = mName;
        newItem.mFullName = mFullName;
        newItem.mOpenCountTotal = mOpenCountTotal;
        newItem.mOpenCountRest = mOpenCountRest;

        return newItem;
    }

    @Override
    public String toString() {
        return "DoorInfo{" +
                "ServerId='" + mServerId + '\'' +
                ", BuildingId=" + mBuildingId +
                ", Id=" + mId +
                ", Name='" + mName + '\'' +
                ", FullName='" + mFullName + '\'' +
                ", OpenCountTotal=" + mOpenCountTotal +
                ", OpenCountRest=" + mOpenCountRest +
                '}';
    }
}
