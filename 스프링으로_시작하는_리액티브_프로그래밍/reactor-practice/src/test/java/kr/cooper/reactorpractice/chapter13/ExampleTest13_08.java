package kr.cooper.reactorpractice.chapter13;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * StepVerifier 활용 예제
 *  -검증에 소요되는 시간을 제한한다.
 */
public class ExampleTest13_08 {
	@Test
	public void getCOVID19CountTest() {
		StepVerifier
			.create(TimeBasedTestExample.getCOVID19Count(
					Flux.interval(Duration.ofMinutes(1)).take(1)
				)
			)
			.expectSubscription()
			.expectNextCount(11)
			.expectComplete()
			.verify(Duration.ofSeconds(3)); // 3초 내에 기댓값의 평가가 끝나지 않으면 시간 초과로 간주하겠다.
	}
}
