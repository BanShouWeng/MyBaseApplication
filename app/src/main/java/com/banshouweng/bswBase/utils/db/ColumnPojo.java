package com.banshouweng.bswBase.utils.db;

import java.lang.reflect.Field;

/**
 * 列数据对象
 *
 * @author leiming
 * @date 2019/1/5.
 */
public class ColumnPojo {
    /**
     * 列名（Bean属性名）
     */
    private String name;
    /**
     * 列参数（Bean属性值）
     */
    private Object value;
    /**
     * 属性类型
     */
    private String type;
    /**
     * 属性
     */
    private Field field;
    /**
     * 是否是必填项
     */
    private boolean isRequired;

    /**
     * 构造
     *
     * @param field      属性：int/String/boolean...
     * @param name       参数名称
     * @param value      属性值
     * @param type       属性对应SQL中的类型
     * @param isRequired 是否是必填项
     */
    ColumnPojo(Field field, String name, Object value, String type, boolean isRequired) {
        this.name = name;
        this.value = value;
        this.isRequired = isRequired;
        this.field = field;
        this.type = typeJudge(type);
    }

    Field getField() {
        return field;
    }

    String getValueString() {
        return value.toString();
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public boolean isRequired() {
        return isRequired;
    }

    /**
     * 数据库类型判断
     *
     * @param type 当前属性在bean中的类型
     * @return 对应数据库中的类型
     */
    private String typeJudge(String type) {
        switch (type) {
            case "int":
            case "class java.lang.Integer":
                return DbBase.INT;

            case "long":
            case "class java.lang.Long":
                return DbBase.LONG;

            case "short":
            case "class java.lang.Short":
                return DbBase.SHORT;

            case "byte":
            case "class java.lang.Byte":
                return DbBase.BYTE;

            case "float":
            case "class java.lang.Float":
                return DbBase.FLOAT;

            case "double":
            case "class java.lang.Double":
                return DbBase.DOUBLE;

            case "boolean":
            case "class java.lang.Boolean":
                return DbBase.BOOLEAN;

            case "class java.lang.String":
                return DbBase.STRING;

            default:
                return DbBase.STRING;
        }
    }
}
