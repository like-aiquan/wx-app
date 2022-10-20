package like.wx.auth.core.reactive;

import java.util.Objects;
import like.wx.auth.AuthPermission;
import like.wx.auth.core.route.AuthStrategyRoute;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
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
public class ReactiveAuthInterceptor implements WebFilter {
	private final RequestMappingHandlerMapping handlerMapping;
	private final AuthStrategyRoute route;
	private final String tokenHeader;

	public ReactiveAuthInterceptor(RequestMappingHandlerMapping handlerMapping, AuthStrategyRoute route, String tokenHeader) {
		this.handlerMapping = handlerMapping;
		this.route = route;
		this.tokenHeader = tokenHeader;
	}

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

					boolean accept = this.route.route(ano.strategy())
							.verify(ano.key(), exchange.getRequest().getHeaders().getFirst(this.tokenHeader));

					if (accept) {
						return chain.filter(exchange);
					}
					return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "FORBIDDEN"));
				});
	}
}
