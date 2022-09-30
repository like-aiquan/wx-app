package like.wx.mp.constant;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenaiquan
 */
@Configuration
public class WxApp {
	@Value("${wx.mp.app-id}")
	private String appId;
	@Value("${wx.mp.esa-key}")
	private String esaKey;
	@Value("${wx.mp.token}")
	private String token;
	@Value("${wx.mp.app-secret}")
	private String appSecret;

	public static final String BASE_URL = "https://api.weixin.qq.com";
	public static String APP_ID;
	public static String ESA_KEY;
	public static String TOKEN;
	public static String APP_SECRET;

	public static String APP_ACCESS_TOKEN;

	public static synchronized void setAppAccessToken(String appAccessToken) {
		APP_ACCESS_TOKEN = appAccessToken;
	}

	@PostConstruct
	private void init() {
		APP_ID = appId;
		ESA_KEY = esaKey;
		TOKEN = token;
		APP_SECRET = appSecret;
	}
}
