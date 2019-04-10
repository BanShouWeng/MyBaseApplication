package com.banshouweng.bswBase.utils;

import java.util.List;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
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

    /**
     * 当前对象为空判断
     *
     * @param o 被判断对象
     * @return 是否为空
     */
    public static  boolean isEmpty(Object o) {
        return null == o;
    }

    /**
     * 当前对象存在判断
     *
     * @param o 被判断对象
     * @return 是否存在
     */
    public static  boolean notEmpty(Object o) {
        return null != o;
    }

    /**
     * 获取类名
     *
     * @param clz 需要获取名称的类
     * @return 类名字符串
     */
    public static String getName(Class<?> clz) {
        return clz.getClass().getSimpleName();
    }
}
