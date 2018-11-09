package com.media.studio;

import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2018/11/9 21:24<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class MediaHelper {

    private MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    public void setDataSource(String path) {
        try {
            retriever.setDataSource(path);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "width:" + getWH()[0]
                + "\nheight:" + getWH()[1]
                + "\nduration:" + getDuration()
                + "\nmimeType:" + getMimeType()
                + "\nartist:" + getArtist()
                + "\ntitle:" + getTitle()
                + "\ndata:" + getData()
                + "\nlocation:" + getLocation()
                + "\nbitrate:" + getBitrate()
                + "\nclosestSync:" + getClosestSync();
    }

    /**
     * @return 获取宽高信息，0是宽，1是高
     */
    public int[] getWH() {
        int[] wh = new int[2]; //0w,1h
        wh[0] = ParseUtil.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        wh[1] = ParseUtil.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        int rotation = getRotationAngle();
        if (rotation == 90 || rotation == 270) {
            wh[0] = wh[0] ^ wh[1];
            wh[1] = wh[0] ^ wh[1];
            wh[0] = wh[0] ^ wh[1];
        }
        return wh;
    }

    public long getDuration() {
        return ParseUtil.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    public String getBitrate() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
    }

    /**
     * @return 获取媒体类型
     */
    public String getMimeType() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
    }

    public String getArtist() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
    }

    public String getTitle() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
    }


    public static final String VIDEO_META_DATE_MASK = "yyyyMMdd'T'hhmmss.SSS'Z'";

    public String getData() {
        long violationDate;
        try {
            SimpleDateFormat metaDateFormat = new SimpleDateFormat(VIDEO_META_DATE_MASK, Locale.CHINA);
            violationDate= metaDateFormat.parse(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            violationDate= 0;
        }

        SimpleDateFormat editTextDateFormat = new SimpleDateFormat(EDIT_TEXT_MASK,Locale.CHINA);
        return  editTextDateFormat.format(violationDate);
    }

    public static final String EDIT_TEXT_MASK = "yyyy.MM.dd";

    public String getAlbumartist() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
    }

    public String getCaptureFramerate() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE);
    }

    public LatLng getLocation() {
        String location = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION);
        LatLng latLng = new LatLng(0, 0);
        if (!TextUtils.isEmpty(location)) {
            location = location.replace("/", "");
            String[] geoData = location.substring(1).split("[+]"); //TODO Caution hard divide by +
            latLng.lat = ParseUtil.parseDouble(geoData[0]);
            latLng.lon = ParseUtil.parseDouble(geoData[1]);
        }
        return latLng;
    }

    public static class LatLng {
        public double lat;
        public double lon;

        public LatLng(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        public String toString() {
            return "{" +
                    "lat=" + lat +
                    ", lon=" + lon +
                    '}';
        }
    }

    /**
     * @return 用於檢索數據源創建或修改時的年份
     */
    public String getYeah() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
    }

    public String getClosestSync() {
        return retriever.extractMetadata(MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
    }

    public String getClosest() {
        return retriever.extractMetadata(MediaMetadataRetriever.OPTION_CLOSEST);
    }

    public String getPreviousSync() {
        return retriever.extractMetadata(MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
    }

    public String getNextSync() {
        return retriever.extractMetadata(MediaMetadataRetriever.OPTION_NEXT_SYNC);
    }

    //    此選項用於getFrameAtTime(long、int)，以檢索與最近(在時間)或給定時間最接近的數據源相關聯的同步(或鍵)框架。
//    String CLOSEST_SYNC = mmr.extractMetadata(MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
    //該選項用於getFrameAtTime(long、int)，用於檢索與最近或給定時間最接近的數據源相關的幀(不一定是關鍵幀)。
//    String CLOSEST = mmr.extractMetadata(MediaMetadataRetriever.OPTION_CLOSEST);
    //這個選項用於getFrameAtTime，以檢索與在給定時間之前或在給定時間內的數據源相關聯的同步(或鍵)框架。
//    String PREVIOUS_SYNC = mmr.extractMetadata(MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);

    public int getRotationAngle() {
        return ParseUtil.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
    }

    public void release() {
        retriever.release();
    }


}
