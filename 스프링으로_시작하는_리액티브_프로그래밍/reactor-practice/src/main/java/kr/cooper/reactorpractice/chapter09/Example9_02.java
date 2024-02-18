package kr.cooper.reactorpractice.chapter09;

import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

/**
 * create() Operator를 사용하는 예제
 *  - 일반적으로 Publisher가 단일 쓰레드에서 데이터 생성한다.
 */
@Slf4j
public class Example9_02 {
	public static void main(String[] args) throws InterruptedException {
		int tasks = 6;

		Sinks.Many<String> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
		Flux<String> fluxView = unicastSink.asFlux();

		IntStream
			.range(1, tasks)
			.forEach(n -> {
				try {
					new Thread(() -> {
						unicastSink.emitNext(doTask(n), Sinks.EmitFailureHandler.FAIL_FAST);
						log.info("# emitted: {}", n);
					}).start();
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					log.error(e.getMessage());
				}
			});

		fluxView
			.publishOn(Schedulers.parallel())
			.map(result -> result + " success!")
			.doOnNext(n -> log.info("# map(): {}", n))
			.publishOn(Schedulers.parallel())
			.subscribe(data -> log.info("# onNext: {}", data));

		Thread.sleep(200L);
	}

	private static String doTask(int taskNumber) {
		// now tasking.
		// complete to task.
		return "task " + taskNumber + " result";
	}
}
