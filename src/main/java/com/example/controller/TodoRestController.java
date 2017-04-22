package com.example.controller;

import com.example.domain.Todo;
import com.example.mapper.TodoMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RequestMapping("/todos")
@RestController
public class TodoRestController {

	private final TodoMapper mapper;

	TodoRestController(TodoMapper mapper) {
		this.mapper = mapper;
	}

	@GetMapping
	ResponseEntity<Flux<Todo>> getTodos() {

		Flux<Todo> flux = Flux.push(fluxSink -> {
			mapper.collect(resultContext -> fluxSink.next(resultContext.getResultObject()));
			fluxSink.complete();
		});

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_STREAM_JSON)
				.body(flux.publishOn(Schedulers.elastic()).subscribeOn(Schedulers.elastic()));
	}

}
