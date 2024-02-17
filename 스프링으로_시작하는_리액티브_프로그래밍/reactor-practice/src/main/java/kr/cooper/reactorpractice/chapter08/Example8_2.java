package kr.cooper.reactorpractice.chapter08;

import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Example8_2 {
	public static void main(String[] args) throws InterruptedException {
		Flux
			.interval(Duration.ofMillis(1L))
			.onBackpressureError()
			.doOnNext(data -> log.info("# doOnNext: {}", data))
			.publishOn(Schedulers.parallel())
			.subscribe(data -> {
					try {
						Thread.sleep(5L);
					} catch (InterruptedException e) {}
					log.info("# onNext: {}", data);
				},
				error -> log.error("# onError", error));

		Thread.sleep(2000L);
	}
}
