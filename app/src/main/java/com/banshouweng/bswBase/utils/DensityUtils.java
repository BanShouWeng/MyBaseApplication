/*
 * Copyright (C) 2013  WhiteCat 白猫 (www.thinkandroid.cn)
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
package com.banshouweng.bswBase.utils;

import android.content.Context;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class DensityUtils {

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取图形验证码宽
     *
     * @param context 上下文信息
     * @return 图形验证码宽度
     */
    public static int getValidateCodeWidth(Context context) {
        return DensityUtils.dp2px(context, 90);
    }

    /**
     * 获取图形验证码高
     *
     * @param context 上下文信息
     * @return 图形验证码高度
     */
    public static int getValidateCodeHeight(Context context) {
        return DensityUtils.dp2px(context, 33);
    }

    /**
     * 获取图形验证码字体大小
     *
     * @param context 上下文信息
     * @return 图形验证码字体大小
     */
    public static int getValidateCodeFontSize(Context context) {
        return DensityUtils.dp2px(context, 23);
    }
}
