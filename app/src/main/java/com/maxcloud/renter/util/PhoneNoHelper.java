package com.maxcloud.renter.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描    述：电话号码帮助类，提供电话号码的相关操作（获取当前手机电话号码、格式判断和格式化手机号）。
 * 作    者：向晓阳
 * 时    间：2016/2/25
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class PhoneNoHelper {

    private static Pattern mPattern = Pattern.compile("^(1[3,4,5,7,8][0-9])\\d{8}");

    public static Boolean IsValidMobileNo(String mobileNo) {
        if (AppHelper.isDebug()) {
            return true;
        } else {
            Matcher m = mPattern.matcher(mobileNo);
            return m.matches();
        }
    }

    public static String getCurrentMobileNo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            String number = tm.getLine1Number();
            if (number.length() > 11) {
                return number.substring(number.length() - 11);
            }
            return number;
        } catch (Exception ex) {
            return null;
        }
    }

    private static char mPlaceholder = '-';

    /**
     * 判断字符是否为占位符。
     *
     * @param c 要判断的字符。
     * @return 如果是占位符，则返回 true ，否则，返回 false 。
     */
    public static boolean isPlaceholder(char c) {
        return c == mPlaceholder;
    }

    /**
     * 获取占位符个数。
     *
     * @param phoneNo 要查找的字符串。
     * @return 占位符个数。
     */
    public static int getPlaceholderCount(CharSequence phoneNo) {
        int placeholderCount = 0;
        for (int i = 0; i < phoneNo.length(); i++) {
            if (isPlaceholder(phoneNo.charAt(i))) {
                placeholderCount++;
            }
        }

        return placeholderCount;
    }

    /**
     * 格式化手机号码，返回格式化后的手机号码。
     *
     * @param phoneNo 要格式化的手机号码。
     * @return 格式化后的手机号码。
     */
    public static String formatPhoneNo(CharSequence phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) {
            return null;
        }

        StringBuffer buffer = new StringBuffer(phoneNo);
        int index = 0;
        while (index < buffer.length()) {
            if (!isPlaceholderIndex(index) && isPlaceholder(buffer.charAt(index))) {
                buffer.deleteCharAt(index);
            } else {
                index++;
            }
        }

        index = 0;
        while (index < buffer.length()) {
            if (isPlaceholderIndex(index)) {
                if (!isPlaceholder(buffer.charAt(index))) {
                    buffer.insert(index, String.valueOf(mPlaceholder));
                }
            }
            index++;
        }

        index = buffer.length() - 1;
        if (index >= 0 && isPlaceholder(buffer.charAt(index))) {
            buffer.deleteCharAt(index);
        }

        return buffer.toString();
    }

    /**
     * 格式化手机号码，返回占位符个数。
     *
     * @param editable 要格式化的手机号码。
     * @return 占位符个数。
     */
    public static int formatPhoneNo(Editable editable) {
        int index = 0;
        while (index < editable.length()) {
            if (!isPlaceholderIndex(index) && isPlaceholder(editable.charAt(index))) {
                editable.delete(index, index + 1);
            } else {
                index++;
            }
        }

        index = 0;
        int placeholderCount = 0;
        while (index < editable.length()) {
            if (isPlaceholderIndex(index)) {
                if (!isPlaceholder(editable.charAt(index))) {
                    editable.insert(index, String.valueOf(mPlaceholder));
                }
                placeholderCount++;
            }
            index++;
        }

        index = editable.length() - 1;
        if (index >= 0 && isPlaceholder(editable.charAt(index))) {
            editable.delete(index, index + 1);
        }

        return placeholderCount;
    }

    /**
     * 解析手机号码，去除占位符。
     *
     * @param fmtText 要解析的手机号码。
     * @return 去除占位符后的手机号码。
     */
    public static String parsePhoneNo(CharSequence fmtText) {
        if (null == fmtText) {
            return "";
        }

        int index = 0;
        StringBuffer buffer = new StringBuffer(fmtText);
        while (index < buffer.length()) {
            if (isPlaceholder(buffer.charAt(index))) {
                buffer.deleteCharAt(index);
            } else {
                index++;
            }
        }

        return buffer.toString();
    }

    // 判断当前索引位置是否应该为点位符。
    private static boolean isPlaceholderIndex(int index) {
        return (index == 3 || index == 8);
    }
}
