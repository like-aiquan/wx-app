package like.wx.mp.job;

import static like.wx.mp.utils.Http.get;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import like.wx.mp.constant.WxApp;
import like.wx.mp.model.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * access token 刷新器
 *
 * @author chenaiquan
 */
@Slf4j
@Component
public class AccessTokenRefresher {
	private static final String refresh_access_token = "/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
	/**
	 * 1.5 小时刷新一次
	 */
	private static final int refresh = 15 * 60 * 60 * 100;

	/**
	 * 集群模式下 不能这样刷新，需要加分布式锁 且 access token 需要存起来 各处机器都需要去查询才能用
	 */
	@Scheduled(fixedDelay = refresh, initialDelay = 100)
	public void refreshAccessToken() {
		AccessToken response = null;
		for (; ; ) {
			try {
				response = refresh();
				if (response == null) {
					throw new RuntimeException("error request!");
				}
				if (response.getErrcode() != null && response.getErrcode() != 0) {
					throw new RuntimeException(response.getErrmsg());
				}
				break;
			}
			catch (Exception e) {
				log.error("retry with message [{}]", e.getMessage(), e);
				try {
					TimeUnit.SECONDS.sleep(10);
				}
				catch (InterruptedException ex) {
					log.error("current thread collapsed.");
					break;
				}
			}
		}

		if (Objects.isNull(response)) {
			refreshAccessToken();
		}
		else {
			log.info("refresh access token success!");
			WxApp.setAppAccessToken(response.getAccessToken());
		}
	}

	private AccessToken refresh() {
		String uri = String.format(refresh_access_token, WxApp.APP_ID, WxApp.APP_SECRET);
		return get(WxApp.BASE_URL, uri, AccessToken.class);
	}
}
