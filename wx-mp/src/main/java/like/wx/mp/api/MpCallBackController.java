package like.wx.mp.api;

import like.wx.common.vo.R;
import like.wx.mp.model.DraftListForm;
import like.wx.mp.utils.SignUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * call back
 *
 * @author chenaiquan
 */
@RestController
@RequestMapping("/wx/callback")
public class MpCallBackController {

	@GetMapping("/pass")
	public String pass(String signature, String timestamp, String nonce, String echostr) {
		if (!SignUtils.sign(signature, timestamp, nonce)) {
			throw new RuntimeException("error signature!");
		}
		return echostr;
	}

	@PostMapping("/test")
	public R test(@RequestBody DraftListForm form, @RequestParam("access_token") String accessToken) {
		System.out.println(accessToken);
		return R.success(form);
	}
}
