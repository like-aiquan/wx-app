package like.wx.mp.api;

import like.wx.auth.AuthPermission;
import like.wx.common.vo.R;
import like.wx.mp.service.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenaiquan
 */
@RestController
@RequestMapping("/wx/draft")
public class DraftController {

	@Autowired
	private DraftService draftService;

	@GetMapping("/list")
	@AuthPermission(key = "draft.list.view")
	public R list(Integer page, Integer size) {
		return R.success(this.draftService.list(page, size));
	}
}
