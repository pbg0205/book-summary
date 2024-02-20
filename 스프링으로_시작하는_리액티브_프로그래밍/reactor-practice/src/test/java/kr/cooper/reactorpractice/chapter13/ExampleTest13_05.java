package kr.cooper.reactorpractice.chapter13;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

/**
 * StepVerifier 활용 예제
 */
public class ExampleTest13_05 {
	@Test
	public void takeNumberTest() {
		Flux<Integer> source = Flux.range(0, 1000);
		StepVerifier
			.create(GeneralTestExample.takeNumber(source, 500),
				StepVerifierOptions.create().scenarioName("Verify from 0 to 499"))
			.expectSubscription() // 구독이 발생했음을 기대한다.
			.expectNext(0) // 숫자 0이 emit 되었음을 기대한다.
			.expectNextCount(499) // 498개의 숫자가 emit 되었음을 기대한다.
			.expectNext(500) // 숫자 500 이 emit 되었음을 기대한다.
			.expectComplete()
			.verify();
	}
}
