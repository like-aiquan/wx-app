package like.wx.mp.service;

import like.wx.auth.core.route.AuthStrategyRoute;
import like.wx.auth.core.service.AuthStrategyService;
import like.wx.auth.core.service.DefaultAuthStrategyServiceImpl;

/**
 * @author chenaiquan
 */
public class MpAuthStrategyRouteExtend extends AuthStrategyRoute {
	@Override
	public AuthStrategyService route(int strategy) {
		switch (strategy) {
			// TODO
			default:
				return new DefaultAuthStrategyServiceImpl();
		}
	}
}
