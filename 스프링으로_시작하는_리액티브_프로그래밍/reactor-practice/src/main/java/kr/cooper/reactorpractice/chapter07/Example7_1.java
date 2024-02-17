package kr.cooper.reactorpractice.chapter07;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Example7_1 {
	public static void main(String[] args) throws InterruptedException {
		Flux<String> coldFlux = Flux.fromIterable(List.of("KOREA", "JAPAN", "CHINESE"))
			.map(String::toLowerCase);

		coldFlux.subscribe(country -> log.info("# Subscriber1: {}", country));
		System.out.println("----------------------------------------------------------------------");
		Thread.sleep(2000L);
		coldFlux.subscribe(country -> log.info("# Subscriber2: {}", country));
	}
}
