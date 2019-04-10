package com.banshouweng.bswBase.utils.db;

import android.database.Cursor;
import android.text.TextUtils;

import com.banshouweng.bswBase.utils.CommonUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author leiming
 * @date 2019/1/2.
 */
public class DbQuery<T> extends DbBase {
    /**
     * 数据库工具类
     */
    private DbUtils dbUtils;

    /**
     * 目标类
     */
    private Class<T> clz;

    /**
     * 搜索参数
     */
    private List<Param> queryList;

    private String sortString = null;
    private String queryType = AND;

    /**
     * @param dbUtils 数据库工具类
     * @param clz     待查询类
     */
    DbQuery(DbUtils dbUtils, Class<T> clz) {
        this.dbUtils = dbUtils;
        this.clz = clz;
    }

    /**
     * 初始化查询map
     */
    private void initQueryMap() {
        if (CommonUtils.isEmpty(queryList)) {
            queryList = new ArrayList<>();
        }
    }

    public DbQuery<T> setQueryType(@QueryType String queryType) {
        this.queryType = queryType;
        return this;
    }

    /**
     * 添加参数：String
     *
     * @param key   key
     * @param value value
     * @return 查询类
     */
    public DbQuery<T> putParams(String key, String value) {
        initQueryMap();
        queryList.add(new Param(key, value));
        return this;
    }

    /**
     * 添加参数：Long
     *
     * @param key   key
     * @param value value
     * @return 查询类
     */
    public DbQuery<T> putParams(String key, Long value) {
        putParams(key, String.valueOf(value));
        return this;
    }

    /**
     * 添加参数：Integer
     *
     * @param key   key
     * @param value value
     * @return 查询类
     */
    public DbQuery<T> putParams(String key, Integer value) {
        putParams(key, String.valueOf(value));
        return this;
    }

    /**
     * 添加参数：Short
     *
     * @param key   key
     * @param value value
     * @return 查询类
     */
    public DbQuery<T> putParams(String key, Short value) {
        putParams(key, String.valueOf(value));
        return this;
    }

    /**
     * 添加参数：Boolean
     *
     * @param key   key
     * @param value value
     * @return 查询类
     */
    public DbQuery<T> putParams(String key, Boolean value) {
        putParams(key, String.valueOf(value));
        return this;
    }

    public DbQuery<T> sort(String key, @SortType String sortType) {
        sortString = key.concat(sortType);
        return this;
    }

    /**
     * 查询数据库中是否存在对应数据
     *
     * @param t   待验证Bean
     * @param <T> 泛型
     * @return 是否存在
     */
    @SuppressWarnings("TypeParameterHidesVisibleType")
    <T> boolean hasBean(T t) {
        reflectPrimaryKey(t);
        try {
            Cursor cursor = dbUtils.mDbManager.mQuery(tableName, null
                    , primaryKeyPojo.getName().concat(" like ?")
                    , new String[] {primaryKeyPojo.getValueString()}
                    , null, null, dbUtils.UPDATE_TIME.concat(DESC));
            int count = cursor.getCount();
            if (! cursor.isClosed()) {
                cursor.close();
            }
            return count != 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * 获取数据库中的第一项
     *
     * @return 第一项Bean
     */
    public T getFirst() {
        Map<String, String[]> tableMap = getDB(dbUtils.mContext);
        if (CommonUtils.isEmpty(tableMap)) {
            return null;
        }
        reflect(clz);
        Cursor cursor = getCursor();
        while (cursor.moveToNext()) {
            try {
                Constructor c0 = clz.getDeclaredConstructor();
                c0.setAccessible(true);
                Object o = c0.newInstance();
                insertValue(o, cursor);
                if (! cursor.isClosed()) {
                    cursor.close();
                }
                //noinspection unchecked
                return (T) o;
            } catch (InstantiationException e) {
                CommonUtils.log().e(getName(), e);
                return null;
            } catch (InvocationTargetException e) {
                CommonUtils.log().e(getName(), e);
                return null;
            } catch (NoSuchMethodException e) {
                CommonUtils.log().e(getName(), e);
                return null;
            } catch (IllegalAccessException e) {
                CommonUtils.log().e(getName(), e);
                return null;
            }
        }
        return null;
    }

    /**
     * 获取满足搜索条件的全部Bean列表
     *
     * @return Bean列表
     */
    public List<T> getAll() {
        Map<String, String[]> tableMap = getDB(dbUtils.mContext);
        if (CommonUtils.isEmpty(tableMap)) {
            return null;
        }
        reflect(clz);
        Cursor cursor = getCursor();
        List<T> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {
                Constructor c0 = clz.getDeclaredConstructor();
                c0.setAccessible(true);
                Object o = c0.newInstance();
                insertValue(o, cursor);
                //noinspection unchecked
                list.add((T) o);
            } catch (InstantiationException e) {
                CommonUtils.log().e(getName(), e);
            } catch (InvocationTargetException e) {
                CommonUtils.log().e(getName(), e);
            } catch (NoSuchMethodException e) {
                CommonUtils.log().e(getName(), e);
            } catch (IllegalAccessException e) {
                CommonUtils.log().e(getName(), e);
            }
        }
        if (! cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    /**
     * 获取数据库游标
     *
     * @return 满足条件的数据库游标
     */
    private Cursor getCursor() {
        StringBuilder selectionBuffer = new StringBuilder();
        List<String> selectionArgList = new ArrayList<>();

        if (CommonUtils.judgeListNull(queryList) != 0) {
            for (Param param : queryList) {
                selectionBuffer.append(param.key).append("=?".concat(queryType));
                selectionArgList.add(param.value);
            }
            String selection = selectionBuffer.toString();
            String[] selectionArgs = new String[selectionArgList.size()];
            CommonUtils.log().i(selection);
            CommonUtils.log().i(selectionArgList.toString());
            return dbUtils.mDbManager.mQuery(tableName, null, selection.substring(0, selection.length() - (AND.equals(queryType) ? 5 : 4))
                    , selectionArgList.toArray(selectionArgs), null, null, TextUtils.isEmpty(sortString) ? dbUtils.UPDATE_TIME.concat(DESC) : sortString);

        } else {
            return dbUtils.mDbManager.mQuery(tableName, null, null
                    , null, null, null, TextUtils.isEmpty(sortString) ? dbUtils.UPDATE_TIME.concat(DESC) : sortString);
        }
    }

    /**
     * 将数据库中的数据插入到Bean对象中
     *
     * @param o       Bean对象
     * @param mCursor 数据库结果游标
     * @throws IllegalAccessException 转换异常
     */
    private void insertValue(Object o, Cursor mCursor) throws IllegalAccessException {
        for (String key : columnMap.keySet()) {
            ColumnPojo pojo = columnMap.get(key);
            switch (pojo.getType()) {
                case INT:
                    pojo.getField().set(o, mCursor.getInt(mCursor.getColumnIndex(key)));
                    break;
                case STRING:
                    pojo.getField().set(o, mCursor.getString(mCursor.getColumnIndex(key)));
                    break;
                case DOUBLE:
                    pojo.getField().set(o, mCursor.getDouble(mCursor.getColumnIndex(key)));
                    break;
                case FLOAT:
                    pojo.getField().set(o, mCursor.getFloat(mCursor.getColumnIndex(key)));
                    break;
                case LONG:
                    pojo.getField().set(o, mCursor.getLong(mCursor.getColumnIndex(key)));
                    break;
                case SHORT:
                    pojo.getField().set(o, mCursor.getShort(mCursor.getColumnIndex(key)));
                    break;
                case BYTE:
                    pojo.getField().set(o, Byte.valueOf(mCursor.getString(mCursor.getColumnIndex(key))));
                    break;
                case BOOLEAN:
                    pojo.getField().set(o, Boolean.valueOf(mCursor.getString(mCursor.getColumnIndex(key))));
                    break;
            }
        }
    }

    class Param {
        String key;
        String value;

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
