package com.banshouweng.mybaseapplication;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class App extends Application {
    /**
     * Application对象
     */
    private static App app;
    /**
     * 全局SharedPreferences对象
     */
    private SharedPreferences sharedPreferences;
    /**
     * 外部存储地址
     */
    public String storageUrl;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        storageUrl = Environment.getExternalStorageDirectory().getPath();
    }

    public SharedPreferences getSharedPreferencesInstance() {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        }
        return sharedPreferences;
    }
}
