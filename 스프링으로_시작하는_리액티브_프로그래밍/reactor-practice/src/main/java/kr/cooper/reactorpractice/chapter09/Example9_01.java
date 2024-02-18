package kr.cooper.reactorpractice.chapter09;

import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

/**
 * create() Operator를 사용하는 예제
 *  - 일반적으로 Publisher가 단일 쓰레드에서 데이터 생성한다.
 */
@Slf4j
public class Example9_01 {
	public static void main(String[] args) throws InterruptedException {
		int tasks = 6;
		Flux
			.create((FluxSink<String> sink) -> { // create 가 처리해야 할 작업의 갯수만큼 doTasK() 작업을 처리
				IntStream
					.range(1, tasks)
					.forEach(n -> sink.next(doTask(n)));
			})
			.subscribeOn(Schedulers.boundedElastic())
			.doOnNext(n -> log.info("# create(): {}", n))

			.publishOn(Schedulers.parallel()) // subscriber 에게 전달하는 스레드를 지정하는 부분 (thread 추가 - parallel_1)
			.map(result -> result + " success!")
			.doOnNext(n -> log.info("# map(): {}", n))

			.publishOn(Schedulers.parallel()) // subscriber 에게 전달하는 스레드를 지정하는 부분 (thread 추가 - parallel_2)
			.subscribe(data -> log.info("# onNext: {}", data)); // subscriberOn() 에서 지정한 thread 를 사용해서 생성한 데이터를 emit 한다.

		Thread.sleep(500L);
	}

	private static String doTask(int taskNumber) {
		// now tasking.
		// complete to task.
		return "task " + taskNumber + " result";
	}
}
