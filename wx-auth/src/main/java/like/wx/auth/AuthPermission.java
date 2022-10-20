package like.wx.auth;


import static like.wx.auth.AuthStrategy.D;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenaiquan
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthPermission {

	boolean enabled() default true;

	String key() default "";

	int strategy() default D;
}
