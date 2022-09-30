package like.wx.mp.service;

import like.wx.common.vo.Pagination;
import like.wx.mp.model.MediaInfo;

/**
 * @author chenaiquan
 */
public interface DraftService {

	/**
	 * 草稿箱列表
	 */
	Pagination<?> list(Integer page, Integer size);
}
