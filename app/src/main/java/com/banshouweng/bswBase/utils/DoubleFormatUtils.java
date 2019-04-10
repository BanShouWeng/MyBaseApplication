package com.banshouweng.bswBase.utils;

import java.math.BigDecimal;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
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
