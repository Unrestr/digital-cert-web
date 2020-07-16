package com.greatmap.digital.intercepters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识哪些方法或类被加解密切面拦截
 * @author zxc
 * @date 2019-11-22
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResolveThirdParams {
    /**
     * 是否进对响应加密
     */
    boolean encryptResponse() default true;
}
