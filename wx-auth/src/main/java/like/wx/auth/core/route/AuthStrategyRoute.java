package like.wx.auth.core.route;

import static like.wx.auth.AuthStrategy.D;

import like.wx.auth.core.service.AuthStrategyService;
import like.wx.auth.core.service.DefaultAuthStrategyServiceImpl;

/**
 * @author chenaiquan
 */
public class AuthStrategyRoute {
	/**
	 * 校验策略路由
	 */
	public AuthStrategyService route(int strategy) {
		switch (strategy) {
			case D:
			default:
				return new DefaultAuthStrategyServiceImpl();
		}
	}
}
