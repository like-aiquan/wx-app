package like.wx.mp.model;

import java.util.List;
import lombok.Data;

/**
 * @author chenaiquan
 */
@Data
public class MediaInfo {
	private String mediaId;
	private Content content;
	private String updateTime;

	@Data
	public static class Content {
		private List<DraftInfo> newsItem;
	}
}
