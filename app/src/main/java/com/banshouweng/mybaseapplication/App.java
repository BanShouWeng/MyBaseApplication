package com.banshouweng.mybaseapplication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class App extends Application {
    private static App app;
    private static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static App getInstence() {
        return app;
    }

    public static SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = app.getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = preferences.edit();


        edit.putString("putString", "string").apply();

        edit.remove("putString").apply();

        edit.clear().apply();

        return preferences;

    }

}
