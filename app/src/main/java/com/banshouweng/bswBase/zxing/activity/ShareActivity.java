package com.banshouweng.bswBase.zxing.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.banshouweng.bswBase.R;
import com.banshouweng.bswBase.base.activity.BaseActivity;
import com.banshouweng.bswBase.base.activity.BaseLayoutActivity;
import com.banshouweng.bswBase.utils.DensityUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 场所名展示页
 *
 * @author leiming
 * @date 2018/1/17
 */
public class ShareActivity extends BaseLayoutActivity {

    private ImageView shareCodeImg;
    private TextView renovateQrCode;
    private String placeId;
    private String shareType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.QR_code);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    protected void findViews() {
        shareCodeImg = getView(R.id.share_code);
        renovateQrCode = getView(R.id.renovate_qrCode);
    }

    @Override
    protected void formatViews() {
        generateQrCode();
        renovateQrCode.setText(Html.fromHtml("<u>" + getResources().getString(R.string.renovate_qrCode) + "</u>"));
        setOnClickListener(R.id.renovate_qrCode);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (bundle != null) {
            placeId = bundle.getString("placeId");
            shareType = bundle.getString("share_type");
        }
    }

    @Override
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.renovate_qrCode:
                generateQrCode();
                break;
        }
    }

    private void generateQrCode() {
        // @@场所编码#到期时间戳（毫秒）#分享类型（1联系人；2关注者）@@
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        String shareCode = "@@" + placeId + "#" + calendar.getTime().getTime() + "#" + shareType + "@@";
        shareCodeImg.setImageBitmap(generateBitmap(shareCode, DensityUtils.dp2px(mContext, 300), DensityUtils.dp2px(mContext, 300)));
    }

    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}
