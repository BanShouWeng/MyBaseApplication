package com.banshouweng.bswBase.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by leiming on 2017/9/11.
 */

public class TableHelper extends SQLiteOpenHelper {
    private final String TAG = "MultiTableHelper";
    private String[] tableNames;
    private String[] sqls;

    /**
     * 初始化构造函数
     *
     * @param context
     * @param name    数据库名
     * @param factory 游标工厂（基本不用）
     * @param version 版本号
     */
    public TableHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 初始化构造函数
     *
     * @param context
     * @param dbName     数据库名
     * @param version    版本号
     * @param tableNames 表名
     * @param sqls       SQL语句
     */
    public TableHelper(Context context, String dbName, int version, String[] tableNames, String[] sqls) {
        this(context, dbName, null, version);
        this.tableNames = tableNames;
        this.sqls = sqls;
    }

    // 当调用SQLiteDatabase中的getWritableDatabase()函数的时候会检测表是否存在，如果不存在onCreate将被调用创建表,否则将不会在被调用。
    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db != null) {
            for (int i = 0; i < tableNames.length; i++) {
                Log.d(TAG, "tableName =" + tableNames[i]);
                Log.d(TAG, "sql=" + sqls[i]);
                db.execSQL("create table if not exists " + tableNames[i] + sqls[i]);
            }
        }
    }

    public void createNewTable(String[] tableNames, String[] sqls) {
        SQLiteDatabase db = getWritableDatabase();
        for (int i = 0; i < tableNames.length; i++) {
            Log.d(TAG, "tableName =" + tableNames[i]);
            Log.d(TAG, "sql=" + sqls[i]);
            db.execSQL("create table if not exists " + tableNames[i] + sqls[i]);
        }
    }

    // 版本更新
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "oldVersion=" + oldVersion);
        Log.d(TAG, "newVersion=" + newVersion);
        if (db != null) {
            // 如果表存在就删除
            for (int i = 0; i < tableNames.length; i++) {
                db.execSQL("drop table if exists " + tableNames[i]);
            }
            // 重新初始化
            onCreate(db);
        }
    }
}
