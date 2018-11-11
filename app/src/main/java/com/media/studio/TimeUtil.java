package com.media.studio;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2018/11/11 16:11<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class TimeUtil {
    public static String durationFormat(long duration) {
        long days = duration / (60 * 60 * 24);
        long hours = (duration % (60 * 60 * 24)) / (60 * 60);
        long minutes = (duration % (60 * 60)) / 60;
        long seconds = duration % 60;
        if (days > 0) {
            return days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒";
        } else if (hours > 0) {
            return hours + "小时" + minutes + "分钟" + seconds + "秒";
        } else if (minutes > 0) {
            return minutes + "分钟" + seconds + "秒";
        } else {
            return seconds + "秒";
        }
    }
}
