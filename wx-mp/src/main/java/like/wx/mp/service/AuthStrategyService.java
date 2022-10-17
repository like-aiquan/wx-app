package like.wx.mp.service;

import like.wx.mp.constant.AuthStrategy;
import like.wx.mp.service.impl.AuthStrategyServiceImpl;

/**
 * @author chenaiquan
 */
public interface AuthStrategyService {

	/**
	 * 校验策略路由
	 */
	static AuthStrategyService route(AuthStrategy strategy) {
		switch (strategy) {
			default:
				return new AuthStrategyServiceImpl();
		}
	}

	/**
	 * 校验器
	 */
	default boolean verify(String key, String token) {
		return false;
	}
}
