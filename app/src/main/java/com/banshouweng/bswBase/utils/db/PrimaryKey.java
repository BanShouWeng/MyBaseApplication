package com.banshouweng.bswBase.utils.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leiming
 * @date 2019/1/2.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface PrimaryKey {
    /**
     * 主键别名设置
     *
     * @return 别名
     */
    public String name() default "";
}
