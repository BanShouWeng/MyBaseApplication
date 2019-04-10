package com.banshouweng.bswBase.ui.activity;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import com.banshouweng.bswBase.R;
import com.banshouweng.bswBase.base.BaseBean;
import com.banshouweng.bswBase.base.activity.BaseLayoutActivity;
import com.banshouweng.bswBase.netWork.NetUtils;
import com.banshouweng.bswBase.utils.Const;

import java.util.Map;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class TestActivity extends BaseLayoutActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, TextureView.SurfaceTextureListener {

    //    private Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
    private String path = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    private final String Tag = MainActivity.class.getSimpleName();

    private MediaPlayer mediaPlayer;
    private Surface surface;
    private TextureView textureView;
    private ImageView imageView;

    private Handler handler = new Handler();

    private NetUtils netUtils;

    private final Runnable mTicker = new Runnable() {
        public void run() {
            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);

            handler.postAtTime(mTicker, next);//延迟一秒再次执行runnable,就跟计时器一样效果
        }
    };

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle("TestActivity");
        if (! TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        netUtils = new NetUtils(mContext,netRequestCallBack);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void findViews() {
//        videoView = getView(R.id.video_view);
        imageView = getView(R.id.image);
        textureView = getView(R.id.video_view);
    }

    @Override
    protected void formatViews() {
        setOnClickListener(R.id.image);
        textureView.setSurfaceTextureListener(this);
//        textureView.setSurfaceTextureListener(this);
//        videoView.setMediaController(new MediaController(this));
//        videoView.setVideoPath(path);
//        videoView.requestFocus();
//        videoView.start();
    }

    @Override
    protected void formatData() {

//        try {
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setOnBufferingUpdateListener(this);
//            mediaPlayer.setOnCompletionListener(this);
//            mediaPlayer.setDataSource(path);
//            mediaPlayer.setSurface(surface);
//            mediaPlayer.prepareAsync();
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
////                    mediaPlayer.setDisplay(surfaceHolder);
//                    mediaPlayer.start();
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (Const.notEmpty(bundle)) {
            title = bundle.getString("title");
        }
    }

    @Override
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                Bitmap bitmap = textureView.getBitmap();
                imageView.setImageBitmap(bitmap);
                break;
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        surface = new Surface(surfaceTexture);
        new PlayerVideoThread().start();//开启一个线程去播放视频
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        surface = null;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @Override
    public void onClick(View v) {

    }

    private class PlayerVideoThread extends Thread {
        @Override
        public void run() {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(path);//设置播放资源(可以是应用的资源文件／url／sdcard路径)
                mediaPlayer.setSurface(surface);//设置渲染画板
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放类型
                mediaPlayer.setOnCompletionListener(TestActivity.this);//播放完成监听
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//预加载监听
                    @Override
                    public void onPrepared(MediaPlayer mp) {//预加载完成
                        mediaPlayer.start();//开始播放
                        handler.post(mTicker);//更新进度
                    }
                });
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private NetUtils.NetRequestCallBack netRequestCallBack = new NetUtils.NetRequestCallBack() {
        @Override
        public void success(String action, BaseBean t, Map tag) {
            switch (action) {

            }
        }

        @Override
        public void error(String action, Throwable e, Map tag) {

        }
    };
}
