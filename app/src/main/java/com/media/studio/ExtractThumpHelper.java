package com.media.studio;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2018/11/11 23:02<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class ExtractThumpHelper {

    public static List<Bitmap> extract(String mVideoPath) {
        MediaHelper helper = new MediaHelper();
        helper.setDataSource(mVideoPath);
        long start = System.currentTimeMillis();
        long duration = helper.getDuration();
        long range = 500;
        long number = duration / range;
        if (number >= 20) {
            number = 20;
            range = duration / 20;
        }
        final List<Bitmap> bitmaps = new ArrayList<>();
        Bitmap temp;
        for (long i = 0; i < number; i++) {
            temp = helper.getFrameAtTime(i * range * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
            if (temp == null) {
                continue;
            }
            bitmaps.add(temp);
        }
        helper.release();
        Log.w("wjc", bitmaps.size() + "," + (System.currentTimeMillis() - start));
        return bitmaps;
    }
}
