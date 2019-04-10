package com.banshouweng.bswBase.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 数据库管理类
 * Created by leiming on 2017/9/11.
 */

public class DbManager {
    private final String TAG = "DbManager";

    private Context context;
    private SQLiteDatabase mSQLiteDatabase;
    private TableHelper mSQLiteOpenHelper;
    private boolean isSqlSuccessed = false;

    DbManager(Context context) {
        super();
        this.context = context;
    }

    /**
     * 创建多表数据库
     *
     * @param dbName     数据库名称
     * @param version    数据库版本
     * @param tableNames 数据库表名
     * @param sqls       sql语句
     */
    void create(String dbName, int version, String[] tableNames, String[] sqls) {
        if (isSqlSuccessed || sqls.length > 0) {
            for (String sql : sqls) {
                if (! isLegalSql(sql)) {
                    Log.e(TAG, "Sql语句不合法");
                }
            }
            if (mSQLiteOpenHelper == null) {
                mSQLiteOpenHelper = new TableHelper(context, dbName, version, tableNames, sqls);
            } else {
                mSQLiteOpenHelper.createNewTable(tableNames, sqls);
            }
        } else {
            Log.e(TAG, "Sql语句不合法");
        }
    }

    /**
     * 是否为合法Sql语句
     */
    private boolean isLegalSql(String sql) {
        if (sql != null && sql.length() > 1) {
            return "(".equals(sql.charAt(0) + "") && ")".equals(sql.charAt(sql.length() - 1) + "");
        }
        return false;
    }

    /**
     * 执行一条sql语句
     *
     * @param sql 待执行sql语句
     */
    public void mExecSQL(String sql) {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        mSQLiteDatabase.execSQL(sql);
        closeAll();
    }

    /**
     * 增加数据
     *
     * @param tableName      表名
     * @param nullColumnHack 非空字段名
     * @param values         数据源
     */
    void mInsert(String tableName, String nullColumnHack, ContentValues values) {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        mSQLiteDatabase.insert(tableName, nullColumnHack, values);
        closeAll();
    }

    /**
     * 删除数据
     *
     * @param tableName   表名
     * @param whereClause （eg:"_id=?"）
     * @param whereArgs   （eg:new String[] { "01" } ）
     */
    public void mDelete(String tableName, String whereClause, String[] whereArgs) {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        mSQLiteDatabase.delete(tableName, whereClause, whereArgs);
        Log.e(TAG, "已清空" + tableName + "表".equals(whereClause));
        closeAll();
    }

    /**
     * 更新
     *
     * @param tableName   表名
     * @param values      更新的数据
     * @param whereClause （eg:"_id=?"）
     * @param whereArgs   （eg:new String[] { "01" } ）
     */
    public void mUpdate(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        mSQLiteDatabase.update(tableName, values, whereClause, whereArgs);
        closeAll();
    }

    /**
     * 查询
     *
     * @param tableName     表名
     * @param columns       搜索的列表
     * @param selection     筛选条件（eg:"_id=?"）
     * @param selectionArgs 筛选的内容 （eg:new String[] { "01" } ）
     * @param groupBy       分组
     * @param having        分组
     * @param orderBy       排序
     * @return mCursor 游标
     */
    Cursor mQuery(String tableName, String[] columns, String selection,
                  String[] selectionArgs, String groupBy, String having,
                  String orderBy) {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        return mSQLiteDatabase.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    /**
     * 查询全部(查询后需要在调用的类中手动调用closeAll()方法来关闭全部函数)
     *
     * @param tableName 表名
     * @param orderBy   排序方式（asc升序，desc降序）
     * @return mCursor 游标
     */
    public Cursor mQueryAll(String tableName, String orderBy) {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        return mSQLiteDatabase.query(tableName, null, null, null, null, null, orderBy);
    }

    /**
     * 从数据库中删除表
     *
     * @param tableName 表名
     */
    public void mDropTable(String tableName) {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        mSQLiteDatabase.execSQL("drop table if exists " + tableName);
        Log.e(TAG, "已删除" + tableName + "表");
        closeAll();
    }

    /**
     * 删除表中的全部数据
     *
     * @param tableName 表名
     */
    public void mDeleteTable(String tableName) {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        mSQLiteDatabase.execSQL("delete from " + tableName);
        Log.e(TAG, "已清空" + tableName + "表");
        closeAll();
    }

    /**
     * 判断某张表是否存在
     *
     * @param tableName 表名
     * @return true 存在
     */
    boolean isTableExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor mCursor = null;
        try {
            mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "' ";
            mCursor = mSQLiteDatabase.rawQuery(sql, null);
            if (mCursor.moveToNext()) {
                int count = mCursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, tableName + "表不存在");
        }
        if (mCursor != null && ! mCursor.isClosed()) {
            mCursor.close();
        } else {
            Log.e(TAG, "closeAll: mCursor已关闭");
        }
        closeAll();
        return result;
    }

    /**
     * 关闭全部
     */
    private void closeAll() {
        if (mSQLiteOpenHelper != null) {
            mSQLiteOpenHelper.close();
        } else {
            Log.e(TAG, "closeAll: mSQLiteOpenHelper已关闭");
        }
        if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen()) {
            mSQLiteDatabase.close();
        } else {
            Log.e(TAG, "closeAll: mSQLiteDatabase已关闭");
        }
    }

    SqlMaker getSqlMaker() {
        return new SqlMaker();
    }

    class SqlMaker {
        private StringBuffer mSqlBuffer;

        SqlMaker() {
            mSqlBuffer = new StringBuffer();
            Log.i(getClass().getSimpleName(), mSqlBuffer.toString());
        }

        /**
         * 添加主键
         */
        void addPrimaryKey(String name, String type) {
            mSqlBuffer.append(name.concat(" ").concat(type).concat(" primary key,"));
            Log.i(getClass().getSimpleName(), mSqlBuffer.toString());
        }

        /**
         * 创建TEXT型字段
         *
         * @param key 字段名
         */
        public void add(String key, String type) {
            mSqlBuffer.append(key.concat(" ").concat(type).concat(","));
            Log.i(getClass().getSimpleName(), mSqlBuffer.toString());
        }

        /**
         * 获取SQL语句
         */
        String getSql() {
            String sql = null;
            if (mSqlBuffer.length() > 0) {
                sql = mSqlBuffer.toString();
                sql = sql.substring(0, sql.length() - 1);
                sql = "(" + sql + ")";
                Log.i(getClass().getSimpleName(), sql);
                mSqlBuffer = new StringBuffer();
                isSqlSuccessed = true;
            }
            return sql;
        }
    }
}
