package like.wx.mp.service;

/**
 * @author chenaiquan
 */
public interface AuthStrategyService {

	boolean verify(String key, String token);
}
