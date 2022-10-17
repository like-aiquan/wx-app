package like.wx.mp.interceptor;

import java.util.Objects;
import like.wx.mp.anno.AuthPermission;
import like.wx.mp.constant.AuthStrategy;
import like.wx.mp.service.AuthStrategyService;
import like.wx.mp.service.impl.DefaultAuthStrategyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author chenaiquan
 */
@Component
public class AuthInterceptor implements WebFilter {
	@Autowired
	private RequestMappingHandlerMapping handlerMapping;
	@Autowired
	private DefaultAuthStrategyServiceImpl defaultAuthStrategyService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return handlerMapping.getHandler(exchange)
				.switchIfEmpty(chain.filter(exchange))
				.flatMap(handlerMethod -> {
					if (!(handlerMethod instanceof HandlerMethod)) {
						return chain.filter(exchange);
					}

					HandlerMethod handler = (HandlerMethod) handlerMethod;
					// 方法
					AuthPermission ano = AnnotationUtils.findAnnotation(handler.getMethod(), AuthPermission.class);
					// 类
					if (Objects.isNull(ano)) {
						ano = AnnotationUtils.findAnnotation(handler.getBeanType(), AuthPermission.class);
					}

					if (Objects.isNull(ano) || !ano.enabled()) {
						return chain.filter(exchange);
					}

					// TODO token verify
					boolean accept = this.route(ano.strategy())
							.verify(ano.key(), exchange.getRequest().getHeaders().getFirst("TOKEN"));

					if (accept) {
						return chain.filter(exchange);
					}
					return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "FORBIDDEN"));
				});
	}

	/**
	 * 校验策略路由
	 */
	private AuthStrategyService route(AuthStrategy strategy) {
		switch (strategy) {
			case A:
			case B:
			case C:
			case D:
			default:
				return defaultAuthStrategyService;
		}
	}
}
