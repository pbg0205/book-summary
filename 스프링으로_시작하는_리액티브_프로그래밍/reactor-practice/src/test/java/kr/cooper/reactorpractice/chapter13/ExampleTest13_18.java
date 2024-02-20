package kr.cooper.reactorpractice.chapter13;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

/**
 * 정상동작 하는 TestPublisher 예제
 */
public class ExampleTest13_18 {
	@Test
	public void divideByTwoTest() {
		TestPublisher<Integer> source = TestPublisher.create(); // 원하는 상황을 미세하게 재연하며 테스트 할 수 있다.

		StepVerifier
			.create(GeneralTestExample.divideByTwo(source.flux()))
			.expectSubscription()
			.then(() -> source.emit(2, 4, 6, 8, 10)) //  테스트에 필요한 데이터를 emit
			.expectNext(1, 2, 3, 4)
			.expectError()
			.verify();
	}
}
