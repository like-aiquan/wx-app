package like.wx.mp.model;

import lombok.Data;

/**
 * @author chenaiquan
 */
@Data
public class DraftListForm {
	private Integer count;
	private Integer offset;
	private Integer noContent = 0;
}
