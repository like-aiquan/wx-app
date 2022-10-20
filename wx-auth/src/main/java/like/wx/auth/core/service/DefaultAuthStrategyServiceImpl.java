package like.wx.auth.core.service;

/**
 * default 只给出了流程，具体的实现需要自定义（可以继承此类 实现自己的方法）
 */
public class DefaultAuthStrategyServiceImpl implements AuthStrategyService {

	@Override
	public boolean verify(String key, String token) {
		if (!this.tokenVerify(token)) {
			return false;
		}

		// 根据 token 获取实际的详细信息
		token = this.subToken(token);

		// 根据详细信息 校验
		return this.permissionVerify(key, token);
	}

	protected boolean permissionVerify(String key, String token) {
		return true;
	}

	protected String subToken(String token) {
		return token;
	}

	protected boolean tokenVerify(String token) {
		return true;
	}
}
