package com.lalala.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义的日志注解类
 */
@Retention(RetentionPolicy.RUNTIME)  //修饰注解，是注解的注解，称为元注解。在运行时加入
@Target({ElementType.METHOD}) //该注解是用于描述方法
public @interface AOPLog {
    /**
     *主要包括的类型有：增删改查或者其他都可以用。字数限制在20字以内
     */
    String operatetype() default " "; //操作类型

    /**
     * 对操作的简述，字数限制在100以内
     */
    String operatedesc() default " "; //操作简介
}
