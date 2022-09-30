package like.wx.mp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author chenaiquan
 */
@EnableScheduling
@SpringBootApplication
public class WxMpApplication {
	public static void main(String[] args) {
		SpringApplication.run(WxMpApplication.class, args);
	}
}
