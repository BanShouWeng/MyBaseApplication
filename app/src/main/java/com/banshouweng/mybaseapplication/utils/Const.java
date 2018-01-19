package com.banshouweng.mybaseapplication.utils;

import java.util.List;

/**
 * Created by leiming on 2017/10/13.
 */

public class Const {
    /**
     * 判断集合的长度
     *
     * @param list 所要判断的集合
     * @return 集合的大小，若为空也则返回0
     */
    public static int judgeListNull(List list) {
        if (list == null || list.size() == 0) {
            return 0;
        } else {
            return list.size();
        }
    }

    /**
     * 判断集合的长度
     *
     * @param list 索要获取长度的集合
     * @return 该集合的长度
     */
    public static <T extends Object> int judgeListNull(T[] list) {
        if (list == null || list.length == 0) {
            return 0;
        } else {
            return list.length;
        }
    }
}
