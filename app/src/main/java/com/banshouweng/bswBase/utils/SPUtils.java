package com.banshouweng.bswBase.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */

public class SPUtils {
    private SharedPreferences preferences = null;
    private static SPUtils utils;

    public SPUtils(Context context) {
        initPreferences(context);
    }

    public static SPUtils getInstance(Context context) {
        if (utils == null) {
            utils = new SPUtils(context);
        }
        return utils;
    }

    private void initPreferences(Context context) {
        if (preferences == null) {
            preferences = context.getApplicationContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        }
    }

    public void put(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void put(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public void put(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public void put(String key, Activity activity) {
        preferences.edit().putString(key, activity.getClass().getName()).apply();
    }

    public void put(String key, Class clazz) {
        preferences.edit().putString(key, clazz.getName()).apply();
    }

    public void put(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }

    public void put(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public void put(String key, Set<String> values) {
        preferences.edit().putStringSet(key, values).apply();
    }

    public String getString(String key, String defaultString) {
        return preferences.getString(key, defaultString);
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultBoolean) {
        return preferences.getBoolean(key, defaultBoolean);
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public int getInt(String key, int defaultInt) {
        return preferences.getInt(key, defaultInt);
    }

    public float getFloat(String key, float defaultFloat) {
        return preferences.getFloat(key, defaultFloat);
    }

    public Class getClass(String key) throws ClassNotFoundException {
        return Class.forName(key);
    }

    public float getFloat(String key) {
        return preferences.getFloat(key, 0);
    }

    public long getLong(String key) {
        return preferences.getLong(key, 0);
    }

    public long getLong(String key, long defaultLong) {
        return preferences.getLong(key, defaultLong);
    }

    public Set<String> getStringSet(String key) {
        return preferences.getStringSet(key, null);
    }

    public Set<String> getStringSet(String key, Set<String> defaultStringSet) {
        return preferences.getStringSet(key, defaultStringSet);
    }

    public Map<String, ?> getStringSet() {
        return preferences.getAll();
    }

    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    public void clear() {
        preferences.edit().clear().apply();
    }
}
