package kr.cooper.reactorpractice.chapter13;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * StepVerifier Record 테스트 예제
 */
public class ExampleTest13_16 {
	@Test
	public void getCountryTest() {
		StepVerifier
			.create(RecordTestExample.getCapitalizedCountry(
				Flux.just("korea", "england", "canada", "india")))
			.expectSubscription()
			.recordWith(ArrayList::new) // emit 된 데이터에 대한 기록을 시작
			.thenConsumeWhile(country -> !country.isEmpty()) // Predicate 과 일치하는 데이터는 다음 단계에서 소비할 수 있도록 함
			.consumeRecordedWith(countries -> { // 컬렉션에 기록된 데이터를 소비한다.
				assertThat(
					countries.stream().allMatch(country -> Character.isUpperCase(country.charAt(0))), is(true)
				);
			})
			.expectComplete()
			.verify();
	}
}
