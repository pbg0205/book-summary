package kr.cooper.reactorpractice.chapter07;

import java.time.Duration;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * Hot Sequence 예제
 */
@Slf4j
public class Example7_2 {
	public static void main(String[] args) throws InterruptedException {
		List<String> signers = List.of("Singer A", "Singer B", "Singer C", "Singer D", "Singer E");

		log.info("# Begin concert:");
		Flux<String> concertFlux =
			Flux
				.fromIterable(signers)
				.delayElements(Duration.ofSeconds(1))
				.share(); // cold to hot

		concertFlux.subscribe(
			singer -> log.info("# Subscriber1 is watching {}'s song", singer)
		);

		Thread.sleep(2500);

		concertFlux.subscribe(
			singer -> log.info("# Subscriber2 is watching {}'s song", singer)
		);

		Thread.sleep(3000);
	}
}
