package com.greatmap.digital.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解拦截Controller
 * @author greatmap
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented    
public  @interface SystemLog {
    String description()  default "";
}
