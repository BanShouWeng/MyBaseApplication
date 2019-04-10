package com.banshouweng.bswBase.base;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class BaseBean implements Serializable {
    /**
     * 获取当前Bean对象类名
     *
     * @return 类名字符串
     */
    protected String getName() {
        return getClass().getSimpleName();
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
