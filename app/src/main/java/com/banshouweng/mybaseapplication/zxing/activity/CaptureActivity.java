package com.banshouweng.mybaseapplication.zxing.activity;

import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.activity.BaseActivity;
import com.banshouweng.mybaseapplication.utils.Logger;
import com.banshouweng.mybaseapplication.zxing.MessageIDs;
import com.banshouweng.mybaseapplication.zxing.camera.CameraManager;
import com.banshouweng.mybaseapplication.zxing.decoding.CaptureActivityHandler;
import com.banshouweng.mybaseapplication.zxing.decoding.InactivityTimer;
import com.banshouweng.mybaseapplication.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;

///**
// * Initial the camera
// * @author Ryan.Tang
// */
//public class CaptureActivity extends Activity implements Callback {

/**
 * Initial the camera
 * 扫描二维码界面
 */
public class CaptureActivity extends BaseActivity implements Callback {
    public static final String QR_RESULT = "RESULT";

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    // private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    CameraManager cameraManager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("扫一扫");
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        // CameraManager.init(getApplication());
        cameraManager = new CameraManager(getApplication());

        viewfinderView.setCameraManager(cameraManager);

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        cameraManager.closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_capture;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void formatViews() {

    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle() {

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            cameraManager.openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public void handleDecode(Result obj, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        handlerScanResult(obj);
//		showResult(obj, barcode);
    }

    @NonNull
    private void handlerScanResult(Result rawResult) {
        String resultString = rawResult.getText();
        Logger.i("toast", "resultString = " + resultString);
        restartPreviewAfterDelay(0L);
    }

//    private void showDialog() {
//        //扫描失败，是否重试
//        final android.app.AlertDialog myDialog = new android.app.AlertDialog.Builder(ScanCodeActivity.this).create();
//
//        myDialog.show();
//        myDialog.getWindow().setContentView(R.layout.dialog_scan_tip);
//        myDialog.getWindow().findViewById(R.id.tv_login_ok)
//                .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        restartPreviewAfterDelay(0L);
//                        myDialog.dismiss();
//                    }
//                });
//
//        myDialog.getWindow().findViewById(R.id.tv_login_cancel)
//                .setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        myDialog.dismiss();
//                        finish();
//                    }
//                });
//    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(MessageIDs.restart_preview, delayMS);
        }
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            try {
                AssetFileDescriptor fileDescriptor = getAssets().openFd("qrbeep.ogg");
                this.mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
                this.mediaPlayer.setVolume(0.1F, 0.1F);
                this.mediaPlayer.prepare();
            } catch (IOException e) {
                this.mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}