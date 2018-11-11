package com.media.studio.utils;

import android.text.TextUtils;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2018/11/9 22:20<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class ParseUtil {
    public static int parseInt(String value) {
        if (!TextUtils.isEmpty(value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static long parseLong(String value) {
        if (!TextUtils.isEmpty(value)) {
            try {
                return Long.parseLong(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static float parseFloat(String value) {
        if (!TextUtils.isEmpty(value)) {
            try {
                return Float.parseFloat(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static double parseDouble(String value) {
        if (!TextUtils.isEmpty(value)) {
            try {
                return Double.parseDouble(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
