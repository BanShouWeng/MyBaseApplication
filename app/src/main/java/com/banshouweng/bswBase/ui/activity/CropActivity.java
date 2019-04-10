package com.banshouweng.bswBase.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.banshouweng.bswBase.App;
import com.banshouweng.bswBase.R;

import java.io.File;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class CropActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PICK_REQUEST_CODE = 0X1;
    public static final int GET_REQUEST_CODE = 0X2;
    public static final int CROP_REQUEST_CODE = 0X3;
    public static final int PICTURE_REQUEST_CODE = 0x4;

    private ImageView image;
    private EditText editText;

    private Uri mImageUri = Uri.fromFile(new File(App.getInstance().storageUrl + "temp.jpg"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        image = (ImageView) findViewById(R.id.image);
        editText = (EditText) findViewById(R.id.edittext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                startActionPick();
                break;

            case R.id.button2:
                startActionGetContent();
                break;

            case R.id.button3:
                takePicture();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = mImageUri;
        if (data == null) {
            Log.e("fuck", 111 + "-->data:null");
        } else {
            uri = data.getData();
            Object object = data.getParcelableExtra("data");
            if (uri == null) {
                Log.e("fuck", requestCode + "-->uri:null");
            } else {
                Log.e("fuck", requestCode + "-->data:" + uri.getPath());
            }
            if (object == null) {
                Log.e("fuck", requestCode + "-->object:null");
            } else {
                Log.e("fuck", requestCode + "-->data:" + object);
            }
        }
        int width = Integer.parseInt(editText.getText().toString());
        switch (requestCode) {
            case PICK_REQUEST_CODE:
                cropImageUri(uri, width, width, CROP_REQUEST_CODE);
                break;
            case GET_REQUEST_CODE:
                cropImageUri(uri, width, width, CROP_REQUEST_CODE);
                break;
            case PICTURE_REQUEST_CODE:
                cropImageUri(uri, width, width, CROP_REQUEST_CODE);
                break;
            case CROP_REQUEST_CODE:
                image.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
                break;

            default:
                break;
        }

    }

    private void startActionPick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra("return-data", true);// 无论是否设置都不会返回bitmap对象
        startActivityForResult(intent, PICK_REQUEST_CODE);
    }

    private void startActionGetContent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("return-data", true);// 无论是否设置都不会返回bitmap对象
        startActivityForResult(intent, GET_REQUEST_CODE);
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);// 拍照后保存的路径
        startActivityForResult(intent, PICTURE_REQUEST_CODE);
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");// 裁剪图像
        intent.putExtra("crop", "true"); // 没发现什么不同
        intent.putExtra("aspectX", 1);// 裁剪框的X比例
        intent.putExtra("aspectY", 1);// 裁剪框的Y比例

        intent.putExtra("outputX", outputX);// 输出图像的像素宽
        intent.putExtra("outputY", outputY);// 输出图像的像素高

        // 是否保持比例,当你裁剪的大小和outputx和outputy不一致是是否缩放比例
        // 如果不缩放比例，如果输出像素过小则只会显示一部分。过大同理
        intent.putExtra("scale", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);// 输出裁剪后的图像

        intent.putExtra("return-data", true);// 是否返回bitmap图像，（uri是我们穿进去的所以不需要返回）

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }
}
