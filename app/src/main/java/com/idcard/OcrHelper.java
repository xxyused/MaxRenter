package com.idcard;

import android.content.Context;

import com.maxcloud.renter.MainApplication;
import com.maxcloud.renter.R;
import com.maxcloud.renter.util.FileHelper;
import com.maxcloud.renter.util.L;

import java.io.File;
import java.io.IOException;

public class OcrHelper {
    private static Object mLock = new Object();

    public static OcrIdCard recIdCard(String imgPath) throws IOException {
        int ret;
        Context context = MainApplication.getContext();

        try {
            synchronized (mLock) {
                File logDir = new File(FileHelper.getLogDirectory());
                if (!logDir.exists()) {
                    logDir.mkdirs();
                }

                // 1.初始化引擎
                ret = Demo.RECOCRBoot(context);
                if (ret != 1) {
                    throw new IOException(context.getString(R.string.ocr_boot_error, ret));
                }
                Demo.SetLOGPath(FileHelper.getLogDirectory());
                // 打开头像截取
                Demo.SetParam(Demo.T_SET_HEADIMG, 1);
                // 设置为保存log信息
                Demo.SetParam(Demo.T_SET_PRINTFLOG, 1);
                //  打开保存log文件功能
                Demo.SetParam(Demo.T_SET_OPENORCLOSE_LOGPATH, 1);

                // 2.加载图片
                ret = Demo.LoadImage(imgPath);
                if (ret != 1) {
                    throw new IOException(context.getString(R.string.ocr_load_image_error, ret));
                }

                // 3.识别信息
                ret = Demo.RECOCR();
                if (ret != 1) {
                    throw new IOException(context.getString(R.string.ocr_ocr_error, ret));
                }

                OcrIdCard cardInfo = new OcrIdCard();
                cardInfo.setCardImage(imgPath);
                cardInfo.setName(GetFieldString(Demo.FIELD_NAME));
                cardInfo.setSex(GetFieldString(Demo.FIELD_SEX));
                cardInfo.setFolk(GetFieldString(Demo.FIELD_FOLK));
                cardInfo.setBirthDay(GetFieldString(Demo.FIELD_BIRTHDAY));
                cardInfo.setAddress(GetFieldString(Demo.FIELD_ADDRESS));
                cardInfo.setCardNum(GetFieldString(Demo.FIELD_NUM));
                cardInfo.setIssue(GetFieldString(Demo.FIELD_ISSUE));
                cardInfo.setPeriod(GetFieldString(Demo.FIELD_PERIOD));

                byte[] data = Demo.GetHeadImgBuf();
                int size = Demo.GetHeadImgBufSize();
                if (size > 0 && null != data) {
                    cardInfo.setPortrait(data, size);
                }

                return cardInfo;
            }
        } finally {
            try {
                // 4.释放图像
                ret = Demo.FreeImage();
                if (ret != 1) {
                    L.e("templateImage", new IOException(context.getString(R.string.ocr_free_image_error, ret)));
                }
            } catch (Exception e) {
                L.e("freeImage", e);
            }

            try {
                // 5.释放OCR
                ret = Demo.TerminateOCRHandle();
                if (ret != 1) {
                    L.e("templateImage", new IOException(context.getString(R.string.ocr_terminate_error, ret)));
                }
            } catch (Exception e) {
                L.e("terminate", e);
            }

            System.gc();
        }
    }

    // 取得字段信息。
    private static String GetFieldString(int field) {
        byte[] buf = Demo.GetOCRFieldStringBuf(field);
        String str = null;
        try {
            str = new String(buf, "GB2312");
        } catch (Exception e) {
            L.e("getOCRFieldStringBuf", e);
        }
        return str;
    }
}
