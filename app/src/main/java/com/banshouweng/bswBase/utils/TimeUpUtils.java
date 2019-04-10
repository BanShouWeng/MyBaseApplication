package com.banshouweng.bswBase.utils;

import android.support.annotation.IntDef;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author leiming
 * @date 2019/1/16.
 */
public class TimeUpUtils {
    public static final int TIME_UP_CLICK = 55;
    public static final int TIME_UP_JUMP = 56;
    private static SparseArray<Long> lastSaveTimes;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TIME_UP_CLICK, TIME_UP_JUMP})
    @interface TimeUpType {
    }

    public static boolean isTimeUp(@TimeUpType int type, long judgeTime) {
        return isTimeUp(type, judgeTime, 500L);
    }

    public static boolean isTimeUp(@TimeUpType int type, long judgeTime, long intervalTime) {
        if (Const.isEmpty(lastSaveTimes)) {
            lastSaveTimes = new SparseArray<>();
        }
        Long tempTime = lastSaveTimes.get(type);
        if (Const.isEmpty(tempTime)) {
            lastSaveTimes.put(type, judgeTime);
            return true;
        } else {
            CommonUtils.log().i("上次点击时间：" + judgeTime + "  本次点击时间：" + tempTime + "  时间间隔：" + (judgeTime - tempTime));
            if (judgeTime - tempTime > intervalTime) {
                lastSaveTimes.put(type, judgeTime);
                return true;
            } else {
                return false;
            }
        }
    }
}
