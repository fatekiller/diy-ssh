package net.liuchenfei.diyhibernate;

import java.lang.annotation.*;

/**
 * Created by liuchenfei on 2017/5/9.
 * 数据表名称，默认是类名
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface Table {
    String tableName() default "className";

    String columnName() default "fieldName";
}
