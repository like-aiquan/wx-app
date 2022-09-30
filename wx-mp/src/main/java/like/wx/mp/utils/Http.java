package like.wx.mp.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import like.wx.common.utils.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

/**
 * reactor http util
 *
 * @author chenaiquan
 */
@Slf4j
public class Http {
	private static final HttpClient HTTP_CLIENT;
	/**
	 * TODO change it， do not use new pool，must in a common thread pool submit task
	 */
	private static final ScheduledExecutorService pool;
	private static boolean logBody = true;

	static {
		ConnectionProvider provider = ConnectionProvider.builder("http").build();
		HTTP_CLIENT = HttpClient.create(provider)
				.responseTimeout(Duration.ofSeconds(5))
				.doOnRequestError(
						(req, e) -> {
							log.error("request failed. retry this! {}", e.getMessage(), e);
						})
				.doOnResponseError(
						(res, e) -> {
							log.error("why response error? {}", e.getMessage(), e);
						})
				.doAfterResponseSuccess(
						(res, conn) -> {
							if (!HttpResponseStatus.OK.equals(res.status())) {
								throw new RuntimeException(
										"status not support! " + res.status() + " path:" + res.uri());
							}
						});

		ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("http-thread-pool-%d").build();
		pool = Executors.newScheduledThreadPool(1, threadFactory);
	}

	public boolean isLogBody() {
		return logBody;
	}

	public Http setLogBody(boolean logBody) {
		Http.logBody = logBody;
		return this;
	}

	/**
	 * synchronized post conn, body not allow is a string with the escape character
	 */
	public static <T> T post(String baseUrl, String uri, Object body, Class<T> clazz) {
		return post(baseUrl, uri, null, body, clazz, false);
	}

	/**
	 * async post conn, body not allow is a string with the escape character
	 */
	public static <T> T asyncPost(String baseUrl, String uri, Object body, Class<T> clazz) {
		return post(baseUrl, uri, null, body, clazz, true);
	}

	/**
	 * maybe need to customize the header and change whether the synchronization, body not allow is a string with the
	 * escape character
	 */
	public static <T> T post(String baseUrl, String uri,
			Map<String, Object> headers, Object body, Class<T> clazz, boolean async) {
		String response = async ? asyncPost(baseUrl, uri, headers, body) : post(baseUrl, uri, headers, body);
		return JsonUtil.read(clazz, response);
	}

	/**
	 * body not allow is a string with the escape character
	 */
	public static <T> String post(String baseUrl, String uri, Map<String, Object> headers, T body) {
		return httpPost(baseUrl, uri, headers, body);
	}

	/**
	 * body not allow is a string with the escape character
	 */
	public static <T> String asyncPost(String baseUrl, String uri, Map<String, Object> headers, T body) {
		// TODO maybe reactor limit， invoke this get first result should be in a UnBlocking Thread;
		// TODO @{link reactor.core.publisher.BlockingSingleSubscriber.blockingGet()}
		CompletableFuture<String> future = CompletableFuture.supplyAsync(
				() -> httpPost(baseUrl, uri, headers, body), pool
		);
		return asyncResponse(future);
	}

	private static String asyncResponse(CompletableFuture<String> future) {
		try {
			return future.get();
		}
		catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * body not allow is a string with the escape character
	 */
	public static <T> String httpPost(String baseUrl, String uri, Map<String, Object> headers, T body) {
		try {
			String data = JsonUtil.write(body);
			if (logBody) {
				log.info("http post send to [{}], body {}", baseUrl + uri, data);
			}
			return HTTP_CLIENT
					.baseUrl(baseUrl)
					.headers(buildHeaders(headers))
					.headers(postDefaultHeaders(headers, data))
					.post()
					.uri(uri)
					.send(ByteBufFlux.fromString(Mono.just(data)))
					.responseContent()
					.aggregate()
					.asString()
					.block();
		}
		catch (Exception e) {
			log.error(e.toString(), e);
			return "";
		}
	}

	/**
	 * post 请求特殊的 header
	 */
	private static Consumer<HttpHeaders> postDefaultHeaders(Map<String, Object> headers, String data) {
		headers = Optional.ofNullable(headers).orElse(new HashMap<>(1));
		Map<String, Object> finalHeaders = headers;
		return header -> {
			// post 请求一定要带上 "Content-Length" 否则会报错 412
			finalHeaders.put("Content-Length", data.getBytes().length);
		};
	}

	/**
	 * synchronized get conn
	 */
	public static <T> T get(String baseUrl, String uri, Class<T> clazz) {
		return get(baseUrl, uri, null, clazz, false);
	}

	/**
	 * async get coon
	 */
	public static <T> T asyncGet(String baseUrl, String uri, Class<T> clazz) {
		return get(baseUrl, uri, null, clazz, true);
	}

	public static <T> T get(String baseUrl, String uri, Map<String, Object> headers, Class<T> clazz, boolean async) {
		String response = async ? asyncGet(baseUrl, uri, headers) : get(baseUrl, uri, headers);
		return JsonUtil.read(clazz, response);
	}

	public static String get(String baseUrl, String uri, Map<String, Object> headers) {
		try {
			return HTTP_CLIENT
					.baseUrl(baseUrl)
					.headers(buildHeaders(headers))
					.get()
					.uri(uri)
					.responseContent()
					.aggregate()
					.asString(StandardCharsets.UTF_8)
					.block(Duration.ofSeconds(5));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return "";
		}
	}

	private static Consumer<? super HttpHeaders> buildHeaders(Map<String, Object> headers) {
		if (headers == null || headers.isEmpty()) {
			headers = new HashMap<>(1);
		}
		headers.put("Content-Type", "application/json");
		Map<String, Object> finalHeaders = headers;
		return header -> finalHeaders.forEach(header::add);
	}

	private static String asyncGet(String baseUrl, String uri, Map<String, Object> headers) {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(
				() -> get(baseUrl, uri, headers), pool
		);
		return asyncResponse(future);
	}

}
