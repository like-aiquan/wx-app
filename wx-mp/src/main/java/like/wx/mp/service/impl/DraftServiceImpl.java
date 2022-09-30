package like.wx.mp.service.impl;

import java.util.Optional;
import like.wx.common.vo.Pagination;
import like.wx.mp.constant.WxApp;
import like.wx.mp.model.DraftListForm;
import like.wx.mp.model.MediaInfo;
import like.wx.mp.model.WxPaginationVo;
import like.wx.mp.service.DraftService;
import like.wx.mp.utils.Http;
import org.springframework.stereotype.Service;

/**
 * @author chenaiquan
 */
@Service
public class DraftServiceImpl implements DraftService {
	private static final String list = "/cgi-bin/draft/batchget?access_token=%s";

	@Override
	public Pagination<?> list(Integer page, Integer size) {
		page = Optional.ofNullable(page).orElse(Integer.MAX_VALUE);
		size = Optional.ofNullable(size).orElse(Integer.MAX_VALUE);

		DraftListForm body = new DraftListForm();
		body.setCount(size);
		body.setOffset(page * size);
		String uri = String.format(list, WxApp.APP_ACCESS_TOKEN);
		return WxPaginationVo.to(Http.asyncPost(WxApp.BASE_URL, uri, body, WxPaginationVo.class));
	}

}
