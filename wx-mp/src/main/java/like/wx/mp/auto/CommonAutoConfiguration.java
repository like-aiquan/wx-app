package like.wx.mp.auto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import like.wx.common.utils.json.JsonUtil;

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

}
