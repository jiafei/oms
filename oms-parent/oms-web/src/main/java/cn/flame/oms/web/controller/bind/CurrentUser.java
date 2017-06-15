package cn.flame.oms.web.controller.bind;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.flame.oms.web.controller.Constants;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
	/**
	 * 当前用户对象在request中的Attribute
	 *
	 * @return
	 */
	String value() default Constants.CURRENT_USER;
}