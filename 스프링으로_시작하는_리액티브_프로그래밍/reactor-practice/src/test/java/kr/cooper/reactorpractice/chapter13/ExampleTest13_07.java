package kr.cooper.reactorpractice.chapter13;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

/**
 * StepVerifier 활용 예제
 * - 주어진 시간을 앞당겨서 테스트 한다.
 */
public class ExampleTest13_07 {
	@Test
	public void getCOVID19CountTest() {
		StepVerifier
			.withVirtualTime( // 가상 스케줄러의 제어를 받도록 해준다.
				() -> TimeBasedTestExample.getCOVID19Count(Flux.interval(Duration.ofHours(1)).take(1))
			)
			.expectSubscription()
			.then(
				() -> VirtualTimeScheduler
					.get()
				.advanceTimeBy(Duration.ofHours(1))) // 시간을 1시간 당기는 작업을 수행한다.
			.expectNextCount(11)
			.expectComplete()
			.verify();
	}
}
