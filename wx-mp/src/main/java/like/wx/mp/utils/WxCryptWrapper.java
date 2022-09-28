package like.wx.mp.utils;

import like.wx.common.utils.aes.WXBizMsgCrypt;
import like.wx.mp.constant.WxApp;

/**
 * @author chenaiquan
 */
public class WxCryptWrapper {
	public static final WXBizMsgCrypt crypt;

	static {
		crypt = new WXBizMsgCrypt(WxApp.TOKEN, WxApp.ESA_KEY, WxApp.APP_ID);
	}
}
