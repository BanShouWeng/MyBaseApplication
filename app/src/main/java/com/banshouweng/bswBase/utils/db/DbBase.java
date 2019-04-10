package com.banshouweng.bswBase.utils.db;

import android.content.Context;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.banshouweng.bswBase.utils.CommonUtils;
import com.banshouweng.bswBase.utils.Const;
import com.banshouweng.bswBase.utils.SPUtils;
import com.google.gson.Gson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据表基类
 *
 * @author leiming
 * @date 2018/12/28.
 */
public class DbBase {
    /**
     * 默认更新时间
     */
    final String UPDATE_TIME = "updateTime";

    /**
     * Bean类型以及对应数据库类型
     */
    static final String INT = "integer";
    static final String STRING = "text";
    static final String DOUBLE = "double";
    static final String FLOAT = "float";
    static final String LONG = "bigint";
    static final String SHORT = "smallint";
    static final String BYTE = "tinyint";
    static final String BOOLEAN = "char(5)";

    /**
     * 表列表key，用于表列表缓存
     */
    final String DB_TABLES = "DB_TABLES";
    /**
     * 创建表的SQL列表语句key，用于表列表缓存
     */
    final String DB_SQLS = "DB_SQLS";

    /**
     * 查询结果排序方式：DESC倒序、ASC正序
     */
    public static final String DESC = " desc";
    public static final String ASC = " asc";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({DESC, ASC})
    @interface SortType {
    }

    /**
     * 查询结果排序方式：DESC倒序、ASC正序
     */
    public static final String AND = " and ";
    public static final String OR = " or ";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({AND, OR})
    @interface QueryType {
    }

    /**
     * 表名缓存
     */
    String tableName;
    /**
     * 主键
     */
    ColumnPojo primaryKeyPojo;
    /**
     * 非主键列表
     */
    List<ColumnPojo> columnPojos;
    /**
     * 列属性与列名对照
     */
    Map<String, ColumnPojo> columnMap;

    /**
     * 存储表的列表与创建SQL的列表工具
     */
    private SPUtils spUtils;

    /**
     * 数据库对应bean反射解析
     *
     * @param t   被解析的Bean
     * @param <T> 被解析的泛型
     */
    <T> void reflect(T t) {
        reflect(t.getClass(), t);
    }

    /**
     * 解析表名
     *
     * @param clz 被解析类
     * @param <T> 泛型
     */
    <T> void reflectTableName(Class<T> clz) {
        primaryKeyPojo = null;
        columnMap = null;
        columnPojos = null;
        boolean clzHasAnno = clz.isAnnotationPresent(DbClass.class);
        if (! clzHasAnno) {
            throw new IllegalArgumentException("This class does not implement interfaces @DbClass");
        }
        // 获取类上的注解
        DbClass annotation = (DbClass) clz.getAnnotation(DbClass.class);
        // 输出注解上的属性
        String annotationName = annotation.name();
        tableName = TextUtils.isEmpty(annotationName) ? clz.getSimpleName() : annotationName;
    }

    /**
     * @param <T> 泛型
     */
    <T> void reflectPrimaryKey(T t) {
        columnMap = null;
        columnPojos = null;
        Class clz = t.getClass();
        reflectTableName(clz);
        Field[] fields = clz.getDeclaredFields();
        // 解析字段上是否有注解
        // ps：getDeclaredFields会返回类所有声明的字段，包括private、protected、public，但是不包括父类的
        // getFields:则会返回包括父类的所有的public字段，和getMethods()一样
        for (Field field : fields) {
            field.setAccessible(true);
            // 主键获取
            boolean primaryKeyAnno = field.isAnnotationPresent(PrimaryKey.class);
            if (primaryKeyAnno) {                                   // 主键
                String type = field.getGenericType().toString();
                String name;
                PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);
                //输出注解属性
                name = primaryKeyAnnotation.name();
                if (TextUtils.isEmpty(name)) {
                    // 属性名获取中间量，若去掉可能导致无法获取
                    String nameTemp = field.getName();
                    name = nameTemp;
                }
                try {
                    primaryKeyPojo = new ColumnPojo(field, name, Const.isEmpty(t) ? null : field.get(t), type, true);
                    break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 数据库对应bean反射解析
     *
     * @param clz 被解析的Bean的class
     * @param <T> 被解析的泛型
     */
    <T> void reflect(Class clz) {
        reflect(clz, null);
    }

    /**
     * 数据库对应bean反射解析
     *
     * @param clz 被解析的Bean的class
     * @param t   被解析的Bean
     * @param <T> 被解析的泛型
     */
    private <T> void reflect(Class clz, T t) {
        reflectTableName(clz);
        columnPojos = new ArrayList<>();
        columnMap = new HashMap<>();
        Field[] fields = clz.getDeclaredFields();
        // 解析字段上是否有注解
        // ps：getDeclaredFields会返回类所有声明的字段，包括private、protected、public，但是不包括父类的
        // getFields:则会返回包括父类的所有的public字段，和getMethods()一样
        // 避免主键重复获取
        boolean addedPrimaryKey = false;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Ignore.class)) {          // 忽略参数过滤
                continue;
            }
            // 主键获取
            boolean primaryKeyAnno = field.isAnnotationPresent(PrimaryKey.class);
            String type = field.getGenericType().toString();
            String name;
            if (primaryKeyAnno) {                                   // 主键
                if (addedPrimaryKey) {
                    throw new IllegalArgumentException("Primary key has been added");
                }
                PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);
                //输出注解属性
                name = primaryKeyAnnotation.name();
                if (TextUtils.isEmpty(name)) {
                    // 属性名获取中间量，若去掉可能导致无法获取
                    String nameTemp = field.getName();
                    name = nameTemp;
                }
                try {
                    primaryKeyPojo = new ColumnPojo(field, name, Const.isEmpty(t) ? null : field.get(t), type, true);
                    columnMap.put(name, primaryKeyPojo);
                    addedPrimaryKey = true;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                continue;
            }
            // 必填项获取
            boolean requireAnno = field.isAnnotationPresent(Require.class);
            if (requireAnno) {                                      // 必填项
                Require requireAnnotation = field.getAnnotation(Require.class);
                //输出注解属性
                name = requireAnnotation.name();
                if (TextUtils.isEmpty(name)) {
                    // 属性名获取中间量，若去掉可能导致无法获取
                    String nameTemp = field.getName();
                    name = nameTemp;
                }
                try {
                    ColumnPojo columnPojo = new ColumnPojo(field, name, Const.isEmpty(t) ? null : field.get(t), type, true);
                    columnMap.put(name, columnPojo);
                    columnPojos.add(columnPojo);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                continue;
            }
            // 非必填项获取
            name = field.getName();
            try {
                ColumnPojo columnPojo = new ColumnPojo(field, name, Const.isEmpty(t) ? null : field.get(t), type, true);
                columnMap.put(name, columnPojo);
                columnPojos.add(columnPojo);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 存储数据表名以及创建表的SQL语句
     *
     * @param context   上下文
     * @param tableName 表名
     * @param sql       创建表的SQL
     */
    void saveDB(Context context, String tableName, String sql) {
        if (Const.isEmpty(spUtils)) {
            spUtils = SPUtils.getInstance(context);
        }
        String tableJson = spUtils.getString(DB_TABLES);
        String sqlJson = spUtils.getString(DB_SQLS);
        Gson gson = new Gson();
        if (TextUtils.isEmpty(tableJson) || TextUtils.isEmpty(sqlJson)) {
            spUtils.put(DB_TABLES, gson.toJson(new String[] {tableName}));
            spUtils.put(DB_SQLS, gson.toJson(new String[] {sql}));
        } else {
            String[] tables = new Gson().fromJson(tableJson, String[].class);
            String[] sqls = new Gson().fromJson(sqlJson, String[].class);
            List<String> tableList = new ArrayList<>(Arrays.asList(tables));
            tableList.add(tableName);
            List<String> sqlList = new ArrayList<>(Arrays.asList(sqls));
            sqlList.add(sql);
            spUtils.put(DB_TABLES, gson.toJson(tableList));
            spUtils.put(DB_SQLS, gson.toJson(sqlList));
        }
    }

    /**
     * 获取数据表名以及创建表的SQL语句，用于初始化DBManager
     *
     * @param context 上下文
     * @return 表名列表以及创建对应表的SQL语句
     */
    Map<String, String[]> getDB(Context context) {
        if (CommonUtils.isEmpty(spUtils)) {
            spUtils = CommonUtils.getSPUtils(context);
        }
        Gson gson = new Gson();
        String tableJson = spUtils.getString(DB_TABLES);
        String sqlJson = spUtils.getString(DB_SQLS);
        if (TextUtils.isEmpty(tableJson) || TextUtils.isEmpty(sqlJson)) {
            return null;
        } else {
            String[] tables = new Gson().fromJson(tableJson, String[].class);
            String[] sqls = new Gson().fromJson(sqlJson, String[].class);
            Map<String, String[]> map = new HashMap<>();
            map.put(DB_TABLES, tables);
            map.put(DB_SQLS, sqls);
            return map;
        }
    }

    /**
     * 获取类名
     *
     * @return 当前类名
     */
    protected String getName() {
        return getClass().getSimpleName();
    }
}
