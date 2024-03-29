package kr.cooper.reactorpractice.chapter06;

import reactor.core.publisher.Mono;

/**
 * Mono 기본 개념 예제
 * - 1개의 데이터를 생성해서 emit한다.
 */
public class Example6_1 {
	public static void main(String[] args) {
		Mono.just("Hello Reactor")
			.subscribe(
				data -> System.out.println(data + ", # emitted onNext signal"),
				error -> {},
				() -> System.out.println("# emitted onComplete signal")
			);
	}
}
