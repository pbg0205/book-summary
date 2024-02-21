package kr.cooper.reactorpractice.chapter15.book.router_function;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import kr.cooper.reactorpractice.chapter15.book.dto.BookDto;
import kr.cooper.reactorpractice.chapter15.book.filter.BookRouterFunctionFilter;
import reactor.core.publisher.Mono;

@Configuration
public class BookRouterFunction {
	// 함수형 요청 핸들러는 라우팅 해주는 RouterFunction
	@Bean
	public RouterFunction routerFunction() {
		return RouterFunctions
			.route(GET("/v1/router/books/{book-id}"),
				(ServerRequest request) -> this.getBook(request))
			.filter(new BookRouterFunctionFilter());
	}

	public Mono<ServerResponse> getBook(ServerRequest request) {
		return ServerResponse
			.ok()
			.body(Mono.just(BookDto.Response.builder()
				.bookId(Long.parseLong(request.pathVariable("book-id")))
				.bookName("Advanced Reactor")
				.author("Tom")
				.isbn("222-22-2222-222-2").build()), BookDto.Response.class);
	}
}
