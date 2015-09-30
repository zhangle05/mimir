package com.jotunheim.mimir.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jotunheim.mimir.domain.data.RoleAccessLevel;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {

	/**
	 * 是否要求登录，false表示用户无需登录
	 * @return
	 */
	boolean required() default true;

	/**
	 * 访问角色
	 * @return
	 */
	int role() default RoleAccessLevel.USER;
	
}
