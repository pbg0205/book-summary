package kr.cooper.reactorpractice.chapter13;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;

/**
 * StepVerifier Backpressure 테스트 예제
 */
public class ExampleTest13_12 {
	@Test
	public void generateNumberTest() {
		StepVerifier
			.create(BackpressureTestExample.generateNumber(), 1L)
			.thenConsumeWhile(num -> num >= 1)
			.expectError() // 에러를 기대한다.
			.verifyThenAssertThat() // 검증을 트리거하고 난 후, 추가적인 Assertion 을 할 수 있따.
			.hasDroppedElements();

	}
}
