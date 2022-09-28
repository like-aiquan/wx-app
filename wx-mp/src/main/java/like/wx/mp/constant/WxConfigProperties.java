package like.wx.mp.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chenaiquan
 */
@Data
@ConfigurationProperties(prefix = "wx.mp.app")
public class WxConfigProperties {
	private String appId;
	private String esaKey;
	private String token;
}
