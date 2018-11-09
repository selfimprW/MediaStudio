package com.media.studio.gif;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.media.studio.gif.encoder.AnimatedGifEncoder;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2018/11/7 16:33<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class GifUtil {

    /**
     * @param filePath gif路径
     * @param bitmaps  bitmap
     * @param delayMs  gif时间
     * @return gif文件
     */
    public static String createGifByBitmaps(String filePath, List<Bitmap> bitmaps, int delayMs) {
        if (bitmaps == null || bitmaps.size() == 0) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        FileOutputStream fos = null;
        try {
            baos = new ByteArrayOutputStream();
            AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
            gifEncoder.start(baos);
            gifEncoder.setRepeat(0);
            gifEncoder.setDelay(delayMs);
            for (Bitmap bitmap : bitmaps) {
                bitmap = zoomImg(bitmap);
                gifEncoder.addFrame(bitmap);
                Log.e("GifUtil", "width:" + bitmap.getWidth() + ",height:" + bitmap.getHeight());
            }
            gifEncoder.finish();
            fos = new FileOutputStream(filePath);
            baos.writeTo(fos);
            baos.flush();
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("GifUtil", "make gif exception:" + e);
            filePath = null;
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }

    private static Bitmap zoomImg(Bitmap bm) {
        // 计算缩放比例
        float scaleWidth = 1 / 2f;
        float scaleHeight = 1 / 2f;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }
}
