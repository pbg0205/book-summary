package kr.cooper.reactorpractice.chapter08;

import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Unbounded request 일 경우, Downstream 에 Backpressure Latest 전략을 적용하는 예제
 *  - Downstream 으로 전달 할 데이터가 버퍼에 가득 찰 경우,
 *    버퍼 밖에서 대기하는 가장 나중에(최근에) emit 된 데이터부터 버퍼에 채우는 전략
 */
@Slf4j
public class Example8_4 {
	public static void main(String[] args) throws InterruptedException {
		Flux
			.interval(Duration.ofMillis(1L))
			.onBackpressureLatest()
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
