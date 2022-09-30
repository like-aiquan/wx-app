package like.wx.mp.model;

import java.util.List;
import like.wx.common.vo.Pagination;
import lombok.Data;

/**
 * @author chenaiquan
 */
@Data
public class WxPaginationVo<MediaInfo> {
	private Integer totalCount;
	private Integer itemCount;
	private List<MediaInfo> item;
	private String errcode;
	private String errmsg;

	public static <E> Pagination<E> to(WxPaginationVo<E> vo) {
		Pagination<E> pagination = new Pagination<>(vo.getTotalCount(), vo.getItemCount());
		return pagination.finish(vo.getItem(), vo.totalCount);
	}
}
