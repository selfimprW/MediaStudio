package com.media.studio;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.media.studio.gif.GifUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
public class VideoFragment extends Fragment implements View.OnClickListener {

    private Button mExtractThumbBtn;
    private Button mMakeGifIv;
    private RecyclerView mFrameListRv;
    private ScrollView mContentView;
    private ImageView mGifResultIv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_video, container, false);
        initView(root);
        return root;
    }

    private MediaHelper helper;
    private String mVideoPath;
    private ThumbListAdapter mFrameAdapter;

    private void initView(View root) {
        mContentView = root.findViewById(R.id.scroll_content);
        TextView mVideoInfoTv = root.findViewById(R.id.video_info);
        ImageView mCoverIv = root.findViewById(R.id.cover);
        mExtractThumbBtn = root.findViewById(R.id.extract_thumb);
        mExtractThumbBtn.setOnClickListener(this);
        mMakeGifIv = root.findViewById(R.id.make_gif);
        mMakeGifIv.setOnClickListener(this);
        mGifResultIv = root.findViewById(R.id.gif_result);


        mFrameListRv = root.findViewById(R.id.frame_list);
        mFrameListRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mFrameAdapter = new ThumbListAdapter();
        mFrameListRv.setAdapter(mFrameAdapter);

        mVideoPath = readAssetsToCache();

        helper = new MediaHelper();
        helper.setDataSource(mVideoPath);
        mVideoInfoTv.setText(helper.toString());
        Bitmap bitmap = helper.getFrameAtTime();
        if (bitmap != null && mCoverIv.getLayoutParams() != null) {
            mCoverIv.setImageBitmap(bitmap);
            mCoverIv.getLayoutParams().height = bitmap.getHeight() / 2;
            mCoverIv.getLayoutParams().width = bitmap.getWidth() / 2;
        }
        helper.release();
    }

    private void extractThumb() {
        mExtractThumbBtn.setText("视频缩略图提取中...");
        mExtractThumbBtn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                helper = new MediaHelper();
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
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mFrameAdapter == null || mExtractThumbBtn == null || mContentView == null) {
                            return;
                        }
                        mFrameAdapter.setBitmaps(bitmaps);
                        mExtractThumbBtn.setEnabled(true);
                        mExtractThumbBtn.setText("提取缩略图");

                        mContentView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.extract_thumb) {
            extractThumb();
        } else if (v.getId() == R.id.make_gif) {
            makeGif();

        }
    }

    private void makeGif() {
        try {
            if (mFrameAdapter.getItemCount() <= 0) {
                Toast.makeText(getContext(), "先提取缩略图", Toast.LENGTH_SHORT).show();
                return;
            }
            String gifPath = generateGifFilePath(System.currentTimeMillis() + "");
            gifPath = GifUtil.createGifByBitmaps(gifPath, mFrameAdapter.getBitmaps(), 150);

//            Glide.with(getActivity()).load(gifPath).asGif().into(mGifResultIv);

            Glide.with(this).load(gifPath).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                    if (resource instanceof GifDrawable) {
                    //加载一次
//                        ((GifDrawable)resource).setLoopCount(1);
//                    }
                    return false;
                }

                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    return false;
                }
            }).into(mGifResultIv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "gif合成成功", Toast.LENGTH_SHORT).show();
    }

    private String generateGifFilePath(String gifName) throws IOException {
        String dcim = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + File.separator + "mediastudio";
        String gifPath = dcim + File.separator + gifName + ".gif";
        File gifFile = new File(gifPath);
        if (gifFile.exists()) {
            gifFile.delete();
        }
        gifFile.getParentFile().mkdirs();
        gifFile.createNewFile();
        return gifPath;
    }
}
