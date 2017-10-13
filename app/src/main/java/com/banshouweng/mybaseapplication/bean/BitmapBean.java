package com.banshouweng.mybaseapplication.bean;

import android.graphics.Bitmap;

import com.banshouweng.mybaseapplication.base.BaseBean;

/**
 * Created by leiming on 2017/10/11.
 */

public class BitmapBean extends BaseBean {
    private Bitmap bitmap;

    public BitmapBean(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
