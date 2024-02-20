package kr.cooper.reactorpractice.chapter13;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

/**
 * 오동작 하는 TestPublisher 예제
 */
public class ExampleTest13_19 {
	@Test
	public void divideByTwoTest() {
		TestPublisher<Integer> source = TestPublisher.create();
		// 데이터 값이 null 이라도 정상 동작하는 TestPublisher 를 생성한다.
		//        TestPublisher<Integer> source = TestPublisher.createNoncompliant(TestPublisher.Violation.ALLOW_NULL);

		StepVerifier
			.create(GeneralTestExample.divideByTwo(source.flux()))
			.expectSubscription()
			.then(() -> {
				getDataSource().stream()
					.forEach(data -> source.next(data));
				source.complete();
			})
			.expectNext(1, 2, 3, 4, 5)
			.expectComplete()
			.verify();
	}

	private static List<Integer> getDataSource() {
		return Arrays.asList(2, 4, 6, 8, null);
	}
}
