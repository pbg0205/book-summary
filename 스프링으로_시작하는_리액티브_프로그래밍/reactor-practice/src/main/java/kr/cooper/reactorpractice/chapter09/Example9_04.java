package kr.cooper.reactorpractice.chapter09;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/**
 * Sinks.One 예제
 *  - emit 된 데이터 중에서 단 하나의 데이터만 Subscriber에게 전달한다. 나머지 데이터는 Drop 됨.
 */
@Slf4j
public class Example9_04 {
	public static void main(String[] args) throws InterruptedException {
		Sinks.One<String> sinkOne = Sinks.one();
		Mono<String> mono = sinkOne.asMono();

		sinkOne.emitValue("Hello Reactor", FAIL_FAST);
		sinkOne.emitValue("Hi Reactor", FAIL_FAST); // FAIT_FAST 는 처음 emit 한 데이터만 emit 되어 나머지 Data 는 drop 된다.
		// sinkOne.emitValue(null, FAIL_FAST);

		mono.subscribe(data -> log.info("# Subscriber1 {}", data));
		mono.subscribe(data -> log.info("# Subscriber2 {}", data));
	}
}
