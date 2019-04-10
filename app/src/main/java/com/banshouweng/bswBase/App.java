package com.banshouweng.bswBase;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;

import com.banshouweng.bswBase.crash.CrashHandler;

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
    public static String storageUrl = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
        crashHandler.init(getApplicationContext());
    }

    public static App getInstance() {
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

    /**
     * 获取内部存储路径
     *
     * @return 路径
     */
    public String getDirPath() {
        return getApplicationContext().getFilesDir().getAbsolutePath();
    }

}
