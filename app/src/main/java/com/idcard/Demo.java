package com.idcard;

import android.content.Context;
import android.graphics.Bitmap;

public class Demo {
	static {
		System.loadLibrary("IDCARDDLL");
	}

	public static int FIELD_NAME = 0;
	public static int FIELD_SEX = 1;
	public static int FIELD_FOLK = 2;
	public static int FIELD_BIRTHDAY = 3;
	public static int FIELD_ADDRESS = 4;
	public static int FIELD_NUM = 5;
	public static int FIELD_ISSUE = 6;
	public static int FIELD_PERIOD = 7;

	public static int T_ONLY_CARD_NUM = 0x0001;// 设置是否只识别卡号
	public static int T_SET_HEADIMG = 0x0002;// 设置是否要截取人头像信息
	public static int T_SET_PRINTFLOG = 0x0003;// 设置是否保存log信息
	public static int T_SET_LOGPATH = 0x0004;// 设置保存log保存文件位置
	public static int T_SET_OPENORCLOSE_LOGPATH = 0x0005;// 打开关闭保存log文件功能
	public static int T_SET_HEADIMGBUFMODE = 0x0006;// 设置人头像模式 0=
														// 原始形式(便于android
														// ios直接加载)
														// 1=BASE64加密形式(便于sdk网络传输)

	public native static int RECOCRBoot(Context context);// 引擎初始化 返回值1：正常
															// -1：未绑定设备 100：时间过期
															// 0：初始化引擎失败

	public native static int LoadImage(String path);// 通过路径加载图片

	public native static int RECOCR();// 识别入口

	public native static int FreeImage();// 释放图像内存

	public native static byte[] GetOCRStringBuf();// 获取全部识别信息

	public native static byte[] GetCopyrightInfo();// 获取版权信息

	public native static byte[] GetVersion();// 获取版本信息

	public native static byte[] GetOCRFieldStringBuf(int field);// 获取各个栏目的信息

	public native static int TerminateOCRHandle();// 释放引擎内存

	public native static int LoadMemBitMap(Bitmap map);// 加载Bitmap流

	public native static int SaveImage(String path);// 保存图片

	public native static int SetLOGPath(String path); // 设置log 保存位置

	public native static byte[] GetHeadImgBuf(); // 获取人头像信息流 得先打开开关

	public native static int GetHeadImgBufSize();// 获取人头像信息流大小

	public native static int SetParam(int Param, int value);// 设置各种系统参数开关函数
}
