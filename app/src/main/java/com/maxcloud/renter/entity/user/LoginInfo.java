package com.maxcloud.renter.entity.user;

import android.text.TextUtils;

import com.maxcloud.renter.entity.build.DoorInfo;
import com.maxcloud.renter.util.FileHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：登录信息对象。
 * 作    者：向晓阳
 * 时    间：2016/2/17
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class LoginInfo implements Serializable {
    private static LoginInfo _loginInfo;

    /**
     * 获取本地保存的登录信息。
     *
     * @return
     */
    public static LoginInfo get() {
        if (null == _loginInfo) {
            try {
                if (FileHelper.existsUserData()) {
                    File file = new File(FileHelper.getUserData());
                    ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
                    _loginInfo = (LoginInfo) oin.readObject();
                    oin.close();
                }
            } catch (Exception e) {
                //这里不能输出日志，因为日志里会调用get()，如果这里输出日志的话，会导致死循环。
                //L.e("get", e);
            }
        }

        if (null == _loginInfo) {
            _loginInfo = new LoginInfo();
        }
        return _loginInfo;
    }

    /**
     * 清除登录信息。
     */
    public static void clear() {
        _loginInfo = null;
    }

    private static final long serialVersionUID = 2L;

    private String _account;
    private int _useVer;
    private int _useFunc;
    private String _phoneNo;
    private String _name;
    private String _idCardNo;
    private String _cityCode;
    private String _cityName;
    private String _signName;
    private String _portraitStamp;
    private String _idCardStamp;
    private List<DoorInfo> _doors = new ArrayList<>();

    /**
     * 获取账号。
     *
     * @return 账号。
     */
    public String getAccount() {
        if (TextUtils.isEmpty(_account)) {
            if (TextUtils.isEmpty(_phoneNo)) {
                return "unknown";
            } else {
                return _phoneNo;
            }
        } else {
            return _account;
        }
    }

    /**
     * 设置账号。
     *
     * @param account 账号。
     * @return
     */
    public LoginInfo setAccount(String account) {
        _account = account;
        return this;
    }

    /**
     * 获取手机号。
     *
     * @return 手机号。
     */
    public String getPhoneNo() {
        return _phoneNo;
    }

    /**
     * 设置手机号。
     *
     * @param phoneNo 手机号。
     * @return
     */
    public LoginInfo setPhoneNo(String phoneNo) {
        _phoneNo = phoneNo;
        return this;
    }

    /**
     * 获取姓名。
     *
     * @return 姓名。
     */
    public String getName() {
        return _name;
    }

    /**
     * 设置姓名。
     *
     * @param name 姓名。
     * @return
     */
    public LoginInfo setName(String name) {
        _name = name;
        return this;
    }

    /**
     * 获取身份证号。
     *
     * @return 身份证号。
     */
    public String getIdCardNo() {
        return _idCardNo;
    }

    /**
     * 设置身份证号。
     *
     * @param idCardNo 身份证号。
     * @return
     */
    public LoginInfo setIdCardNo(String idCardNo) {
        _idCardNo = idCardNo;
        return this;
    }

    /**
     * 获取要查看天气的城市编号。
     *
     * @return 城市编号。
     */
    public String getCityCode() {
        return _cityCode;
    }

    /**
     * 设置要查看天气的城市编号。
     *
     * @param cityCode 城市编号。
     * @return
     */
    public LoginInfo setCityCode(String cityCode) {
        _cityCode = cityCode;
        return this;
    }

    /**
     * 获取要查看天气的城市名称。
     *
     * @return 城市名称。
     */
    public String getCityName() {
        return _cityName;
    }

    /**
     * 设置要查看天气的城市名称。
     *
     * @param cityName 城市名称。
     * @return
     */
    public LoginInfo setCityName(String cityName) {
        _cityName = cityName;
        return this;
    }

    /**
     * 获取星座名称。
     *
     * @return 星座名称。
     */
    public String getSignName() {
        return _signName;
    }

    /**
     * 设置星座名称。
     *
     * @param signName 星座名称。
     * @return 本身。
     */
    public LoginInfo setSignName(String signName) {
        _signName = signName;
        return this;
    }

    /**
     * 获取头像时间戳。
     *
     * @return 头像时间戳。
     */
    public String getPortraitStamp() {
        return _portraitStamp;
    }

    /**
     * 设置头像时间戳。
     *
     * @param portraitStamp 头像时间戳。
     * @return
     */
    public LoginInfo setPortraitStamp(String portraitStamp) {
        _portraitStamp = portraitStamp;
        return this;
    }

    /**
     * 获取身份证照时间戳。
     *
     * @return 身份证照时间戳。
     */
    public String getIdCardStamp() {
        return _idCardStamp;
    }

    /**
     * 设置身份证照时间戳。
     *
     * @param idCardStamp 身份证照时间戳。
     * @return
     */
    public LoginInfo setIdCardStamp(String idCardStamp) {
        _idCardStamp = idCardStamp;
        return this;
    }

    /**
     * 获取可操作的门列表。
     *
     * @return
     */
    public List<DoorInfo> getDoors() {
        return _doors;
    }

    private LoginInfo() {
    }

    /**
     * 添加可操作的门。
     *
     * @param doorInfo 门信息。
     */
    public void addDoor(DoorInfo doorInfo) {
        if (null == _doors) {
            return;
        }

        _doors.add(doorInfo);
    }

    /**
     * 清除可操作的门列表。
     */
    public void clearDoor() {
        if (null != _doors) {
            _doors.clear();
        }
    }

    /**
     * 保存登录信息到本地。
     */
    public void save() {
        try {
            File file = new File(FileHelper.getUserData());
            if (file.exists()) {
                file.delete();
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

//                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                        ObjectOutputStream out = new ObjectOutputStream(baos);
//                                        out.writeObject(loginInfo);
//                                        out.close();
//
//                                        byte[] data = baos.toByteArray();
//                                        if (data.length > 0) {
//
//                                        }
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(this);
            out.close();
        } catch (Exception e) {
        }
    }
}
