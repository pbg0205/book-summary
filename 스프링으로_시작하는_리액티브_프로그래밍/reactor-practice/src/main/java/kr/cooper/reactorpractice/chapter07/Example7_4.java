package kr.cooper.reactorpractice.chapter07;

import java.net.URI;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Example7_4 {
	public static void main(String[] args) throws InterruptedException {
		URI worldTimeUri = UriComponentsBuilder.newInstance().scheme("http")
			.host("worldtimeapi.org")
			.port(80)
			.path("/api/timezone/Asia/Seoul")
			.build()
			.encode()
			.toUri();

		Mono<String> mono = getWorldTime(worldTimeUri).cache(); // cold to hot (캐싱된 데이터 반환)
		mono.subscribe(dateTime -> log.info("# dateTime 1: {}", dateTime));
		Thread.sleep(2000);
		mono.subscribe(dateTime -> log.info("# dateTime 2: {}", dateTime));

		Thread.sleep(2000);
	}

	private static Mono<String> getWorldTime(URI worldTimeUri) {
		return WebClient.create()
			.get()
			.uri(worldTimeUri)
			.retrieve()
			.bodyToMono(String.class)
			.map(response -> {
				DocumentContext jsonContext = JsonPath.parse(response);
				String dateTime = jsonContext.read("$.datetime");
				return dateTime;
			});
	}
}
