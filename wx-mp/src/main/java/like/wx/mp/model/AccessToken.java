package like.wx.mp.model;

import lombok.Data;

/**
 * @author chenaiquan
 */
@Data
public class AccessToken {
	private String accessToken;
	private String expiresIn;
	private String errmsg;
	private Integer errcode;
}
