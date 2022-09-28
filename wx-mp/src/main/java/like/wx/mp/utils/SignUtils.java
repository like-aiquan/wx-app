package like.wx.mp.utils;

import static like.wx.mp.utils.WxCryptWrapper.crypt;

import like.wx.common.utils.aes.AesException;

/**
 * @author chenaiquan
 */
public final class SignUtils {
	public static boolean sign(String signature, String timestamp, String nonce) {
		try {
			crypt.verifyUrl(signature, timestamp, nonce, "");
		}
		catch (AesException e) {
			return false;
		}
		return true;
	}
}
