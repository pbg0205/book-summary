package kr.cooper.reactorpractice.chapter08;

import org.reactivestreams.Subscription;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class Example8_1 {
	public static void main(String[] args) {
		Flux.range(1, 5)
			.doOnRequest(data -> log.info("# doOnRequest: {}", data)) // Subscriber 가 요청한 데이터 갯수를 확인
			.subscribe(new BaseSubscriber<>() {
				@Override
				protected void hookOnSubscribe(final Subscription subscription) {
					request(1);
				}

				@SneakyThrows
				@Override
				protected void hookOnNext(final Integer value) {
					Thread.sleep(2000L);
					log.info("# hookOnNext: {}", value);
					request(1);
				}
			});
	}
}
