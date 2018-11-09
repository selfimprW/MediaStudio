package com.media.studio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2018/11/9 21:23<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class VideoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_video, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        TextView mVideoInfoTv = root.findViewById(R.id.video_info);
        ImageView mCoverIv = root.findViewById(R.id.cover);

        MediaHelper helper = new MediaHelper();
        helper.setDataSource(readAssetsToCache());
        mVideoInfoTv.setText(helper.toString());
        mCoverIv.setImageBitmap(helper.getFrameAtTime());
        helper.release();
    }

    public String readAssetsToCache() {
        InputStream is = null;
        FileOutputStream fos = null;
        String outPath = getActivity().getCacheDir() + File.separator + "test.mp4";
        try {
            File outFile = new File(outPath);
            is = getActivity().getAssets().open("test.mp4");
            fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
        } catch (IOException e) {
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
