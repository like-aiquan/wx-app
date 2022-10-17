package like.wx.mp.anno;

import static like.wx.mp.constant.AuthStrategy.D;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import like.wx.mp.constant.AuthStrategy;

/**
 * @author chenaiquan
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthPermission {

	boolean enabled() default true;

	String key() default "";

	AuthStrategy strategy() default D;
}
