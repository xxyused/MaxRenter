package com.maxcloud.renter.service.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 描    述：服务器返回的楼栋信息。
 * 作    者：向晓阳
 * 时    间：2016/2/29
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class BuildInfoResult {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("serverid")
    @Expose
    private String serverid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("des")
    @Expose
    private String des;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("address_scope")
    @Expose
    private String addressScope;
    @SerializedName("neighbourhood")
    @Expose
    private String neighbourhood;
    @SerializedName("floor_count")
    @Expose
    private String floorCount;
    @SerializedName("gps_x")
    @Expose
    private String gpsX;
    @SerializedName("gps_y")
    @Expose
    private String gpsY;
    @SerializedName("gps_z")
    @Expose
    private String gpsZ;
    @SerializedName("gps_type")
    @Expose
    private String gpsType;
    @SerializedName("address_scope_detail")
    @Expose
    private String addressScopeDetail;
    @SerializedName("incharger_name")
    @Expose
    private String inchargerName;
    @SerializedName("incharger_phone")
    @Expose
    private String inchargerPhone;
    @SerializedName("qr_code_path")
    @Expose
    private String qrCodePath;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The serverid
     */
    public String getServerid() {
        return serverid;
    }

    /**
     * @param serverid The serverid
     */
    public void setServerid(String serverid) {
        this.serverid = serverid;
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
     * @return The path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path The path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return The addressScope
     */
    public String getAddressScope() {
        return addressScope;
    }

    /**
     * @param addressScope The address_scope
     */
    public void setAddressScope(String addressScope) {
        this.addressScope = addressScope;
    }

    /**
     * @return The neighbourhood
     */
    public String getNeighbourhood() {
        return neighbourhood;
    }

    /**
     * @param neighbourhood The neighbourhood
     */
    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    /**
     * @return The floorCount
     */
    public String getFloorCount() {
        return floorCount;
    }

    /**
     * @param floorCount The floor_count
     */
    public void setFloorCount(String floorCount) {
        this.floorCount = floorCount;
    }

    /**
     * @return The gpsX
     */
    public String getGpsX() {
        return gpsX;
    }

    /**
     * @param gpsX The gps_x
     */
    public void setGpsX(String gpsX) {
        this.gpsX = gpsX;
    }

    /**
     * @return The gpsY
     */
    public String getGpsY() {
        return gpsY;
    }

    /**
     * @param gpsY The gps_y
     */
    public void setGpsY(String gpsY) {
        this.gpsY = gpsY;
    }

    /**
     * @return The gpsZ
     */
    public String getGpsZ() {
        return gpsZ;
    }

    /**
     * @param gpsZ The gps_z
     */
    public void setGpsZ(String gpsZ) {
        this.gpsZ = gpsZ;
    }

    /**
     * @return The gpsType
     */
    public String getGpsType() {
        return gpsType;
    }

    /**
     * @param gpsType The gps_type
     */
    public void setGpsType(String gpsType) {
        this.gpsType = gpsType;
    }

    /**
     * @return The addressScopeDetail
     */
    public String getAddressScopeDetail() {
        return addressScopeDetail;
    }

    /**
     * @param addressScopeDetail The address_scope_detail
     */
    public void setAddressScopeDetail(String addressScopeDetail) {
        this.addressScopeDetail = addressScopeDetail;
    }

    /**
     * @return The inchargerName
     */
    public String getInchargerName() {
        return inchargerName;
    }

    /**
     * @param inchargerName The incharger_name
     */
    public void setInchargerName(String inchargerName) {
        this.inchargerName = inchargerName;
    }

    /**
     * @return The inchargerPhone
     */
    public String getInchargerPhone() {
        return inchargerPhone;
    }

    /**
     * @param inchargerPhone The incharger_phone
     */
    public void setInchargerPhone(String inchargerPhone) {
        this.inchargerPhone = inchargerPhone;
    }

    /**
     * @return The qrCodePath
     */
    public String getQrCodePath() {
        return qrCodePath;
    }

    /**
     * @param qrCodePath The qr_code_path
     */
    public void setQrCodePath(String qrCodePath) {
        this.qrCodePath = qrCodePath;
    }
}
