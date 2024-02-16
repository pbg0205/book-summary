package kr.cooper.reactorpractice.chapter06;

import reactor.core.publisher.Mono;

/**
 * Mono 기본 개념 예제
 *  - 원본 데이터의 emit 없이 onComplete signal 만 emit 한다.
 */
public class Example6_2 {
	public static void main(String[] args) {
		Mono
			.empty()
			.subscribe(
				none -> System.out.println("# emitted onNext signal"), // empty 는 onComplete() signal 을 전달
				error -> {},
				() -> System.out.println("# emitted onComplete signal")
			);
	}
}
