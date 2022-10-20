package like.wx.auth.core.sevlet;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import like.wx.auth.AuthPermission;
import like.wx.auth.core.route.AuthStrategyRoute;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author chenaiquan
 */
public class ServletAuthInterceptor implements HandlerInterceptor {
	private final String tokenHeader;
	private final AuthStrategyRoute route;

	public ServletAuthInterceptor(String tokenHeader, AuthStrategyRoute route) {
		this.route = route;
		this.tokenHeader = tokenHeader;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handlerMethod) {
		if (!(handlerMethod instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handler = (HandlerMethod) handlerMethod;
		// 方法
		AuthPermission ano = AnnotationUtils.findAnnotation(handler.getMethod(), AuthPermission.class);
		// 类
		if (Objects.isNull(ano)) {
			ano = AnnotationUtils.findAnnotation(handler.getBeanType(), AuthPermission.class);
		}

		if (Objects.isNull(ano) || !ano.enabled()) {
			return true;
		}

		return this.route.route(ano.strategy())
				.verify(ano.key(), request.getHeader(this.tokenHeader));
	}
}
