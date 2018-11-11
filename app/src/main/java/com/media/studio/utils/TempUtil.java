package com.media.studio.utils;

import com.media.studio.MSApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2018/11/11 22:59<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class TempUtil {

    /**
     * @param name assets 文件名
     * @return 缓存文件路径
     */
    public static String readAssetsToCache(String name) {
        InputStream is = null;
        FileOutputStream fos = null;
        String outPath = MSApp.getContext().getCacheDir() + File.separator + name;
        try {
            File outFile = new File(outPath);
            is = MSApp.getContext().getAssets().open(name);
            fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
        } catch (Exception e) {
            outPath = null;
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
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
        return outPath;
    }
}
