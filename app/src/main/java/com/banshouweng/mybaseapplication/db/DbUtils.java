package com.banshouweng.mybaseapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.banshouweng.mybaseapplication.bean.StudentBean;

import java.util.ArrayList;

/**
 * Created by leiming on 2017/9/11.
 */

public class DbUtils {
    private final String TAG = "TestDB";
    public static final String STUDENT_TABLE = "student_table";
    private String dbName = "test.db";// 数据库名
    private int version = 1;// 版本号
    private DBManager mDBManager;
    private ArrayList<StudentBean> localDataInfos = new ArrayList<>();

    public DbUtils(Context context) {
        mDBManager = new DBManager(context);
        String studentSql = mDBManager.addPrimaryKey().addText("student_phone").addText("student_password").addText("student_name").getSql();
        String[] tables = {STUDENT_TABLE};
        String[] slqs = {studentSql};
        mDBManager.create(dbName, version, tables, slqs);
    }

    public static DbUtils init(Context context) {
        return new DbUtils(context);
    }

    /**
     * 增加新数据
     *
     * @param contentValues 数据源
     * @param tableName     表名
     */
    public void addData(String tableName, ContentValues contentValues) {
        mDBManager.mInsert(tableName, "device_id", contentValues);
        Log.i(TAG, "delData:增加了一条数据 ");
    }

    /**
     * 删除一条数据
     *
     * @param device_id
     * @param tableName 表名
     */
    public void delData(String tableName, String device_id) {
        mDBManager.mDelete(tableName, "device_id=?", new String[]{device_id});
        Log.i(TAG, "delData:删除了一条数据 ");
    }

    /**
     * 清空表中的内容
     *
     * @param tableName 表名
     */
    public void delTable(String tableName) {
        mDBManager.mDeleteTable(tableName);
    }

    /**
     * 是否是空表
     *
     * @param tableName 表名
     * @return true 是空表
     */
    public boolean isEmptyTable(String tableName) {
        if (mDBManager.getDataNum(tableName) > 0) {
            return false;
        }
        return true;
    }

    /**
     * 数据库是否存在要查询的这条数据
     *
     * @param columnName 查询的字段
     * @param data       查询的数据
     * @param tableName  表名
     * @return
     */
    public boolean hasThisData(String tableName, String columnName, String data) {
        return mDBManager.hasThisData(tableName, columnName, data);
    }

    /**
     * 本地数据库是否存在这条student_account
     *
     * @param data      查询的数据
     * @param tableName 表名
     * @return true 有这条数据
     */
    public boolean hasThisMenuID(String tableName, String data) {
        return mDBManager.hasThisData(tableName, "student_phone", data);
    }

    /**
     * 本地数据库是否存在这条账户
     *
     * @param data 查询的数据
     * @return studentBean 用户账号
     */
    public StudentBean hasThisStudent(String[] data) {
        return mDBManager.hasThisStudent(STUDENT_TABLE, new String[]{"student_phone", "student_password"}, data);
    }

    /**
     * 修改一条数据的内容
     *
     * @param values      数据
     * @param whereclause 条件
     * @param tableName   表名
     */
    public void modifyData(String tableName, ContentValues values, String whereclause) {
        mDBManager.mUpdate(tableName, values, whereclause);
        Log.i(TAG, "modifyData: 修改了一条数据");
    }
}
