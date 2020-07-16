package com.greatmap.digital.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gaorui
 * @create 2020-07-01 16:21
 */
@Target({ElementType.METHOD}) // 方法注解
@Retention(RetentionPolicy.RUNTIME) // 运行时可见
public @interface DcLog {
    // 记录日志的操作类型
    String operateType();
}
