package com.banshouweng.bswBase.zxing.decoding;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.banshouweng.bswBase.utils.FileUtils;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.DataMatrixReader;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.Hashtable;

/**
 * 半寿翁
 */
public class AlbumDecoding {

    /**
     * 处理选择的图片
     *
     * @param data 获取的相册图片数据
     */
    public static Result handleAlbumPic(Context context, Intent data) {
        // 获取选中图片的路径
//        String photo_path = UriUtils.getRealPathFromUri(context, data.getData());
        String photo_path = FileUtils.getFilePathByUri(context, data.getData());

        // 进度条
        ProgressDialog mProgress = new ProgressDialog(context);
        mProgress.setMessage("正在扫描...");
        mProgress.setCancelable(false);
        mProgress.show();

        Result result = scanningImage(photo_path);
        mProgress.dismiss();
        return result;

    }

    /**
     * 扫描二维码图片的方法
     *
     * @param path 图片路径
     * @return 扫描结果
     */
    private static Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        // 扫描图片的bitmap
        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        int[] intArray = new int[scanBitmap.getWidth() * scanBitmap.getHeight()];
        scanBitmap.getPixels(intArray, 0, scanBitmap.getWidth(), 0, 0, scanBitmap.getWidth(), scanBitmap.getHeight());
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap.getWidth(), scanBitmap.getHeight(), intArray);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));

        Result result = null;
        // 二维码解码
        QRCodeReader qrCodeReader = new QRCodeReader();
        try {
            result = qrCodeReader.decode(bitmap1, hints);
        } catch (NotFoundException | ChecksumException | FormatException e) {
            e.printStackTrace();
        }
        if (null == result) {
            // DataMatrix解码
            DataMatrixReader dataMatrixReader = new DataMatrixReader();
            try {
                result = dataMatrixReader.decode(bitmap1, hints);
            } catch (NotFoundException | ChecksumException | FormatException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
