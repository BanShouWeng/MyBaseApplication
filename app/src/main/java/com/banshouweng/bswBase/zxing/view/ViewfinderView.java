/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.banshouweng.bswBase.zxing.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.banshouweng.bswBase.R;
import com.banshouweng.bswBase.zxing.camera.CameraManager;
import com.google.zxing.ResultPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {
    /**
     * 刷新界面的时间
     */
    private static final long ANIMATION_DELAY = 30L;
    private static final int OPAQUE = 0xFF;
    private static final int MAX_RESULT_POINTS = 5;
    /**
     * 四个绿色边角对应的长度
     */
    private int ScreenRate;

    /**
     * 四个绿色边角对应的宽度
     */
    private static final int CORNER_WIDTH = 5;
    /**
     * 扫描框中的中间线的宽度
     */
    private static final int MIDDLE_LINE_WIDTH = 6;

    /**
     * 扫描框中的中间线的与扫描框左右的间隙
     */
    private static final int MIDDLE_LINE_PADDING = 5;

    /**
     * 中间那条线每次刷新移动的距离
     */
    private static final int SPEEN_DISTANCE = 3;

    /**
     * 手机的屏幕密度
     */
    private static float density;
    /**
     * 字体大小
     */
    private static final int TEXT_SIZE = 14;
    /**
     * 字体距离扫描框下面的距离
     */
    private static final int TEXT_PADDING_TOP = 30;

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 中间滑动线的最顶端位置
     */
    private int slideTop;

    /**
     * 中间滑动线的最底端位置
     */
    private int slideBottom;

    private int frameAdjust = 50;

    /**
     * 将扫描的二维码拍下来，这里没有这个功能，暂时不考虑
     */
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;

    private final int resultPointColor;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;
    private CameraManager cameraManager;
    boolean isFirst;

    public ViewfinderView(Context context) {
        this(context, null);
    }

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every
        // time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskColor = 0x60000000;
        resultColor = 0xb0000000;
        resultPointColor = 0x00000000;
        possibleResultPoints = new ArrayList<ResultPoint>(5);
        lastPossibleResultPoints = null;

        density = context.getResources().getDisplayMetrics().density;
        // 将像素转换成dp
        ScreenRate = (int) (20 * density);
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @Override
    public void onDraw(Canvas canvas) {
        try {
            // 中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
            Rect frame = cameraManager.getFramingRect();
            Rect frame1 = new Rect();
            frame1.top = frame.top + frameAdjust;
            frame1.bottom = frame.bottom - frameAdjust;
            frame1.left = frame.left + frameAdjust;
            frame1.right = frame.right - frameAdjust;
            if (frame == null) {
                return;
            }

            // 初始化中间线滑动的最上边和最下边
            if (!isFirst) {
                isFirst = true;
                slideTop = frame.top;
                slideBottom = frame.bottom;
            }

            // 获取屏幕的宽和高
            int width = canvas.getWidth();
            int height = canvas.getHeight();

            paint.setColor(resultBitmap != null ? resultColor : maskColor);

            // 画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
            // 扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
            canvas.drawRect(0, 0, width, frame1.top, paint);
            canvas.drawRect(0, frame1.top, frame1.left, frame1.bottom + 1, paint);
            canvas.drawRect(frame1.right + 1, frame1.top, width, frame1.bottom + 1, paint);
            canvas.drawRect(0, frame1.bottom + 1, width, height, paint);

            if (resultBitmap != null) {
                paint.setAlpha(OPAQUE);
                canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
            } else {
                paint.setColor(getResources().getColor(R.color.blue_border));
                canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
                        frame.top + CORNER_WIDTH, paint);
                canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
                        + ScreenRate, paint);
                canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
                        frame.top + CORNER_WIDTH, paint);
                canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
                        + ScreenRate, paint);
                canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
                        + ScreenRate, frame.bottom, paint);
                canvas.drawRect(frame.left, frame.bottom - ScreenRate,
                        frame.left + CORNER_WIDTH, frame.bottom, paint);
                canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
                        frame.right, frame.bottom, paint);
                canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
                        frame.right, frame.bottom, paint);


                slideTop += SPEEN_DISTANCE;
                if (slideTop >= frame.bottom) {
                    slideTop = frame.top;
                }
                //canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH / 2, frame.right - MIDDLE_LINE_PADDING, slideTop + MIDDLE_LINE_WIDTH / 2, paint);
                Rect lineRect = new Rect();
                lineRect.left = frame.left;
                lineRect.right = frame.right;
                lineRect.top = slideTop;
                lineRect.bottom = slideTop + 18;
                canvas.drawBitmap(((BitmapDrawable) (getResources().getDrawable(R.mipmap.scan_code_pic))).getBitmap(), null, lineRect, paint);


                //��ɨ����������
                paint.setColor(getResources().getColor(android.R.color.transparent));
                Rect rect = new Rect((int) (TEXT_PADDING_TOP * density),
                        (int) (frame.bottom + (float) TEXT_PADDING_TOP * density * 4),
                        width - (int) (TEXT_PADDING_TOP * density),
                        frame.bottom);
                canvas.drawRect(rect, paint);
                paint.setColor(Color.WHITE);
                paint.setTextSize(TEXT_SIZE * density);
                paint.setAlpha(0x90);
                paint.setTypeface(Typeface.create("System", Typeface.NORMAL));
                paint.setTextAlign(Paint.Align.CENTER);

                Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
                canvas.drawText("扫一扫", rect.centerX(), rect.centerY(), paint);

                Collection<ResultPoint> currentPossible = possibleResultPoints;
                Collection<ResultPoint> currentLast = lastPossibleResultPoints;
                if (currentPossible.isEmpty()) {
                    lastPossibleResultPoints = null;
                } else {
                    possibleResultPoints = new HashSet<ResultPoint>(5);
                    lastPossibleResultPoints = currentPossible;
                    paint.setAlpha(OPAQUE);
                    paint.setColor(resultPointColor);
                    for (ResultPoint point : currentPossible) {
                        canvas.drawCircle(frame.left + point.getX(), frame.top
                                + point.getY(), 6.0f, paint);
                    }
                }
            /*	if (currentLast != null) {
                    *//*paint.setAlpha(OPAQUE / 2);
                    paint.setColor(resultPointColor);
                    for (ResultPoint point : currentLast) {
                        canvas.drawCircle(frame.left + point.getX(), frame.top
                                + point.getY(), 3.0f, paint);
                    }*//*
                }*/


                //ֻˢ��ɨ�������ݣ�����ط���ˢ��
                postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                        frame.right, frame.bottom);

            }
        } catch (Exception e) {
            Log.e("扫一扫", e.getMessage());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }
}
