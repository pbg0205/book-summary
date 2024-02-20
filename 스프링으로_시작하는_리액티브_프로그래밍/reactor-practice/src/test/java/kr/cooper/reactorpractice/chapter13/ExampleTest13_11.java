package kr.cooper.reactorpractice.chapter13;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;

/**
 * StepVerifier Backpressure 테스트 예제
 */
public class ExampleTest13_11 {
	@Test
	public void generateNumberTest() {
		StepVerifier
			.create(BackpressureTestExample.generateNumber(), 1L)
			// emit 되는 데이터를 소비하고 있지만, 예상한 것보다 더 많은 데이터를 수신함으로써 결국에는 오버플로가 발생한 것
			.thenConsumeWhile(num -> num >= 1)
			.verifyComplete();
	}
}
