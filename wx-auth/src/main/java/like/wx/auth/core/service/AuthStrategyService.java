package like.wx.auth.core.service;

/**
 * @author chenaiquan
 */
public interface AuthStrategyService {

	/**
	 * 自定义 只需要实现这个类即可
	 */
	boolean verify(String key, String token);
}
