package like.wx.mp.service.impl;

import like.wx.mp.service.AuthStrategyService;
import org.springframework.stereotype.Service;

/**
 * @author chenaiquan
 */
@Service
public class DefaultAuthStrategyServiceImpl implements AuthStrategyService {

	@Override
	public boolean verify(String key, String token) {
		return false;
	}
}
