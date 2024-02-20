package kr.cooper.reactorpractice.chapter13;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * StepVerifier 활용 예제
 *  - 지정된 대기 시간동안 이벤트가 없을을 확인한다.
 */
public class ExampleTest13_09 {
	@Test
	public void getVoteCountTest() {
		StepVerifier
			.withVirtualTime( //  StepVerifier 메서드 체인들이 VirtualTimeScheduler 제어를 받도록 했다.
				() -> TimeBasedTestExample.getVoteCount(Flux.interval(Duration.ofMinutes(1)))
			)
			.expectSubscription()
			// 시간을 지정하면 지정한 시간 동안 어떤 이벤트도 발생하지 않을 것이라고 기대하는 동시에 지정한 시간만큼 시간을 앞당긴다
			.expectNoEvent(Duration.ofMinutes(1))
			.expectNoEvent(Duration.ofMinutes(1))
			.expectNoEvent(Duration.ofMinutes(1))
			.expectNoEvent(Duration.ofMinutes(1))
			.expectNoEvent(Duration.ofMinutes(1))
			.expectNextCount(5)
			.expectComplete()
			.verify();
	}
}
