package like.wx.mp.model;

import lombok.Data;

/**
 * @author chenaiquan
 */
@Data
public class DraftInfo {
	private String title;
	private String author;
	private String digest;
	private String content;
	private String contentSourceUrl;
	private String thumbMediaId;
	private String showCoverPic;
	private String needOpenComment;
	private String onlyFansCanComment;
	private String url;
}
