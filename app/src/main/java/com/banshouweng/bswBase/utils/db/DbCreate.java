package com.banshouweng.bswBase.utils.db;

import android.util.Log;

import com.banshouweng.bswBase.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 表创建类
 *
 * @author leiming
 * @date 2019/1/2.
 */
public class DbCreate<T> extends DbBase {
    /**
     * 数据库工具
     */
    private DbUtils dbUtils;
    /**
     * 表名
     */
    private String tableName;

    /**
     * 列集合
     */
    private List<Column> columns;

    /**
     * 数据库创建类
     *
     * @param dbUtils   数据库工具类
     * @param tableName 表名
     */
    DbCreate(DbUtils dbUtils, String tableName) {
        this.dbUtils = dbUtils;
        this.tableName = tableName;
    }

    /**
     * 主键设置
     *
     * @param name 主键名
     * @param type 主键类型
     */
    void addPrimaryKey(String name, String type) {
        columns = new ArrayList<>();
        columns.add(new Column(name, type, true));
    }

    /**
     * 添加列属性
     *
     * @param name 列名
     * @param type 列类型
     */
    public void add(String name, String type) {
        columns.add(new Column(name, type, false));
    }

    /**
     * 创建数据表
     */
    void create() {
        DbManager.SqlMaker sqlMaker = dbUtils.mDbManager.getSqlMaker();
        Boolean hasUpdateTime = false;
        for (Column column : columns) {
            if (column.valueName.equals(UPDATE_TIME)) {
                hasUpdateTime = true;
            }
            if (column.primaryKey) {
                sqlMaker.addPrimaryKey(column.valueName, column.valueType);
            } else {
                sqlMaker.add(column.valueName, column.valueType);
            }
        }
        if (! hasUpdateTime) {
            sqlMaker.add(dbUtils.UPDATE_TIME, LONG);
        }
        String table = tableName;
        String slq = sqlMaker.getSql();
        CommonUtils.log().i("table = ".concat(table).concat(" ***** slqs = ").concat(slq));
        saveDB(dbUtils.mContext, tableName, slq);
        dbUtils.mDbManager.create(dbUtils.dbName, dbUtils.version, new String[] {table}, new String[] {slq});
    }

    /**
     * 列对象
     */
    class Column {
        Column(String name, String type, boolean primaryKey) {
            this.valueName = name;
            this.valueType = type;
            this.primaryKey = primaryKey;
        }

        /**
         * 列名
         */
        private String valueName;
        /**
         * 列类型
         */
        private String valueType;
        /**
         * 是否是主键
         */
        private boolean primaryKey;
    }
}