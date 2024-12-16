package com.yuyou.zizaiyou.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})  //注解可用于类或者方法
@Retention(RetentionPolicy.RUNTIME) //注解在运行时仍然可用，可以通过反射获取注解信息。
public @interface LoginRequired {
	// 这个注解用来标记需要登录的接口方法

}
