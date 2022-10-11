package like.wx.mp;

import like.wx.mp.model.DraftListForm;
import like.wx.mp.utils.Http;
import org.junit.jupiter.api.Test;

/**
 * @author chenaiquan
 */
public class HttpTest {

	@Test
	void post() {
		DraftListForm form = new DraftListForm();
		form.setCount(Integer.MAX_VALUE);
		form.setOffset(Integer.MAX_VALUE);
		form.setNoContent(1);
		String uri = "/cgi-bin/draft/batchget?access_token=61_oQ2L9IH5vLbJ5o57SJRMA_7jlaIWssorCKNvTDIFBfKihg9V7CCtsG4vOjYHD1Gu8WGLku5SVTi5wbQ-eWSJVNLYIAhJ58rj9pwZFcAiB7zbyj-x2pCMYii1020XMUIOBLHa9WOm8ArrmtweVNLcACAJBZ";
		String baseUrl = "https://api.weixin.qq.com";
		System.out.println("response: " + Http.post(baseUrl, uri, form, Object.class));
	}

}
