package like.wx.mp.auto;

import com.fasterxml.jackson.databind.ObjectMapper;
import like.wx.auth.core.reactive.ReactiveAuthInterceptor;
import like.wx.auth.core.route.AuthStrategyRoute;
import like.wx.mp.service.MpAuthStrategyRouteExtend;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import like.wx.common.utils.json.JsonUtil;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.WebFilter;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class CommonAutoConfiguration {

	@Bean
	@Primary
	@ConditionalOnMissingBean
	public ObjectMapper objectMapper() {
		// 替换 springboot 默认的 json 配置
		return JsonUtil.copy();
	}

	@Bean
	@ConditionalOnMissingBean(AuthStrategyRoute.class)
	public AuthStrategyRoute authStrategyRoute() {
		return new MpAuthStrategyRouteExtend();
	}

	@Bean
	@ConditionalOnClass(WebFilter.class)
	@ConditionalOnBean(AuthStrategyRoute.class)
	public ReactiveAuthInterceptor authInterceptor(RequestMappingHandlerMapping handlerMapping,
			AuthStrategyRoute authStrategyRoute) {
		return new ReactiveAuthInterceptor(handlerMapping, authStrategyRoute, "TOKEN");
	}

	@Bean
	@ConditionalOnMissingClass(WebFilter.class)
	@ConditionalOnBean(AuthStrategyRoute.class)
	public ReactiveAuthInterceptor servletAuthInterceptor(RequestMappingHandlerMapping handlerMapping,
			AuthStrategyRoute authStrategyRoute) {
		return new ReactiveAuthInterceptor(handlerMapping, authStrategyRoute, "TOKEN");
	}

}
