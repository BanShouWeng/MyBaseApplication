package com.banshouweng.mybaseapplication.utils;

import java.math.BigDecimal;

/**
 * Created by leiming on 2018/2/24.
 */

public class DoubleFormatUtils {
    public static double setDoubleScale(double formatDouble, int scale) {
        /*
         * ROUND_HALF_UP，最后一位四舍五入
         */
        return new BigDecimal(formatDouble).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double setDoubleScale(String formatDouble, int scale) {
        return new BigDecimal(formatDouble).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
